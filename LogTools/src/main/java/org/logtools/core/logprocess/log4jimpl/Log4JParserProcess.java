package org.logtools.core.logprocess.log4jimpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.logtools.Const;
import org.logtools.Exception.ParseLogException;
import org.logtools.core.domain.LogEntry;
import org.logtools.core.domain.log4jimpl.Log4jLogEntry;
import org.logtools.core.logprocess.AbsLogProcess;

/**
 * this class refer to LogFilePatternReceiver， it just copy the prase feature to
 * here
 * 
 * Notes:
 * 1, after reading LogFilePatternReceiver, I found I need override process and
 * post method <br>
 * if I ONlY want to convert logEvent to logEntry, I don't need to override
 * process <br>
 * but if I need to get more information,like log is which line in file, I need
 * override the process <br>
 * 
 * @author Chandler.Song
 * 
 */
public class Log4JParserProcess extends AbsLogProcess implements
        Log4jConst {

    private static Logger logger = Logger
            .getLogger(Log4JParserProcessTest.class);
    // all lines other than first line of exception begin with tab followed by
    // 'at' followed by text
    private static final String EXCEPTION_PATTERN = "^\\s+at.*";
    private static final String VALID_DATEFORMAT_CHARS = "GyMwWDdFEaHkKhmsSzZ";
    private static final String VALID_DATEFORMAT_CHAR_PATTERN = "["
            + VALID_DATEFORMAT_CHARS + "]";
    private static final String REGEXP_DEFAULT_WILDCARD = ".*?";
    private static final String REGEXP_GREEDY_WILDCARD = ".*";
    private static final String PATTERN_WILDCARD = "*";
    private static final String NOSPACE_GROUP = "(\\S*\\s*?)";
    private static final String DEFAULT_GROUP = "(" + REGEXP_DEFAULT_WILDCARD
            + ")";
    private static final String GREEDY_GROUP = "(" + REGEXP_GREEDY_WILDCARD
            + ")";
    private static final String MULTIPLE_SPACES_REGEXP = "[ ]+";
    private List<Object> matchingKeywords;
    private final List<String> keywords = new ArrayList<String>();
    private SimpleDateFormat dateFormat;
    private String timestampPatternText;
    private final String[] emptyException = new String[] { "" };
    private boolean appendNonMatches = true;

    private static final String PROP_START = "PROP(";
    private static final String PROP_END = ")";
    private String customLevelDefinitions;

    private final Map<Object, Object> customLevelDefinitionMap = new HashMap<Object, Object>();

    private String timestampFormat = "yyyy-MM-d HH:mm:ss,SSS";
    private String logFormat;
    private String regexp;
    private Pattern regexpPattern;
    private Pattern exceptionPattern;

    Map<String, Object> currentMap;
    List<String> additionalLines;

    public Log4JParserProcess(String log4jregression) {
        super();

        keywords.add(TIMESTAMP);
        keywords.add(LOGGER);
        keywords.add(LEVEL);
        keywords.add(THREAD);
        keywords.add(CLASS);
        keywords.add(FILE);
        keywords.add(LINE);
        keywords.add(METHOD);
        keywords.add(MESSAGE);
        keywords.add(NDC);

        this.initialize(log4jregression);
    }

    public void process(File file) {
        try {
            process(new BufferedReader(new FileReader(file)));
        } catch (IOException e) {
            ParseLogException ex = new ParseLogException();
            e.initCause(e);
            throw ex;
        }
    }

    /**
     * Read, parse and optionally tail the log file, converting entries into
     * logging events.
     * 
     * A runtimeException is thrown if the logFormat pattern is malformed.
     * 
     * @param bufferedReader
     * @throws IOException
     */
    protected void process(BufferedReader bufferedReader) throws IOException {
        Matcher eventMatcher;
        Matcher exceptionMatcher;
        String line;

        LogEntry entry = null;

        while ((line = bufferedReader.readLine()) != null) {
            // skip empty line entries
            eventMatcher = regexpPattern.matcher(line);
            if (line.trim().equals("")) {
                continue;
            }
            exceptionMatcher = exceptionPattern.matcher(line);
            if (eventMatcher.matches()) {
                // build an event from the previous match (held in current map)
                entry = buildEntry();
                if (entry != null) {
                    post(entry);
                }
                currentMap.putAll(processEntry(eventMatcher.toMatchResult()));
            } else if (exceptionMatcher.matches()) {
                // an exception line
                additionalLines.add(line);
            } else {
                // neither...either post an event with the line or append as
                // additional lines
                // if this was a logging event with multiple lines, each line
                // will show up as its own event instead of being
                // appended as multiple lines on the same event..
                // choice is to have each non-matching line show up as its own
                // line, or append them all to a previous event
                if (appendNonMatches) {
                    // hold on to the previous time, so we can do our best to
                    // preserve time-based ordering if the event is a non-match
                    String lastTime = (String) currentMap.get(TIMESTAMP);
                    // build an event from the previous match (held in current
                    // map)
                    if (currentMap.size() > 0) {
                        entry = buildEntry();
                        if (entry != null) {
                            post(entry);

                        }
                    }
                    if (lastTime != null) {
                        currentMap.put(TIMESTAMP, lastTime);
                    }
                    currentMap.put(MESSAGE, line);
                } else {
                    additionalLines.add(line);
                }
            }
        }

        // process last event if one exists
        entry = buildEntry();
        if (entry != null) {
            post(entry);

        }
    }

    public LogEntry buildEntry() {
        if (currentMap.size() == 0) {
            if (additionalLines.size() > 0) {
                for (Iterator<String> iter = additionalLines.iterator(); iter
                        .hasNext();) {
                    logger.info("found non-matching line: " + iter.next());
                }
            }
            additionalLines.clear();
            return null;
        }
        // the current map contains fields - build an event
        int exceptionLine = getExceptionLine();
        String[] exception = buildException(exceptionLine);

        // messages are listed before exceptions in additionallines
        if (additionalLines.size() > 0 && exception.length > 0) {
            currentMap.put(MESSAGE,
                    buildMessage((String) currentMap.get(MESSAGE),
                            exceptionLine));
        }
        LogEntry entry = convertToEntery(currentMap, exception);
        currentMap.clear();
        additionalLines.clear();
        return entry;
    }

    /**
     * Convert a keyword-to-values map to a LoggingEvent
     * 
     * @param fieldMap
     * @param exception
     * 
     * @return logging event
     */
    @SuppressWarnings("unused")
    private LogEntry convertToEntery(Map<String, Object> fieldMap,
            String[] exception) {
        if (fieldMap == null) {
            return null;
        }

        // a logger must exist at a minimum for the event to be processed
        if (!fieldMap.containsKey(LOGGER)) {
            fieldMap.put(LOGGER, "Unknown");
        }
        if (exception == null) {
            exception = emptyException;
        }

        String catalog = null;
        long timeStamp = 0L;
        String level = null;
        String threadName = null;
        Object message = null;
        String ndc = null;
        String className = null;
        String methodName = null;
        String eventFileName = null;
        String line = null;

        catalog = fieldMap.remove(LOGGER).toString();

        if ((dateFormat != null) && fieldMap.containsKey(TIMESTAMP)) {
            try {
                timeStamp = dateFormat.parse(
                        (String) fieldMap.remove(TIMESTAMP))
                        .getTime();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // use current time if timestamp not parseable
        if (timeStamp == 0L) {
            timeStamp = System.currentTimeMillis();
        }

        message = fieldMap.remove(MESSAGE);
        if (message == null) {
            message = "";
        }

        level = (String) fieldMap.remove(LEVEL);
        Level levelImpl;
        if (level == null) {
            levelImpl = Level.DEBUG;
        } else {
            // first try to resolve against custom level definition map, then
            // fall back to regular levels
            levelImpl = (Level) customLevelDefinitionMap.get(level);
            if (levelImpl == null) {
                levelImpl = Level.toLevel(level.trim());
                if (!level.equals(levelImpl.toString())) {
                    // check custom level map
                    if (levelImpl == null) {
                        levelImpl = Level.DEBUG;
                        logger.debug(
                                "found unexpected level: " + level
                                        + ", logger: " + logger.getName()
                                        + ", msg: " + message);
                        // make sure the text that couldn't match a level is
                        // added to the message
                        message = level + " " + message;
                    }
                }
            }
        }

        threadName = (String) fieldMap.remove(THREAD);

        ndc = (String) fieldMap.remove(NDC);

        className = (String) fieldMap.remove(CLASS);

        methodName = (String) fieldMap.remove(METHOD);

        eventFileName = (String) fieldMap.remove(FILE);

        line = (String) fieldMap.remove(LINE);

        Log4jLogEntry entry = new Log4jLogEntry();
        entry.setCatalog(catalog);
        entry.setLevel(level);
        entry.setLine(line);
        entry.setMessage(message.toString());
        entry.setThreadInfo(threadName);
        entry.setTime(new Date(timeStamp));
        return entry;
    }

    public void initialize(String log4jregression) {
        TranslateLogFormat translate = new TranslateLogFormat();

        // translate log regression
        String regression = translate.translateExpression(log4jregression);
        logger.info("regression " + regression);
        this.setLogFormat(regression);
        String timestampFormat = translate.getTimestampFormat();
        if (!StringUtils.isBlank(timestampFormat)) {
            this.setTimestampFormat(timestampFormat);
        }
        this.initialize();

        this.setRegexpPattern(Pattern.compile(this.getRegexp()));
        this.setExceptionPattern(Pattern.compile(EXCEPTION_PATTERN));
    }

    public void initialize() {
        String newPattern = this.getLogFormat();
        List<String> buildingKeywords = new ArrayList<String>();
        matchingKeywords = new ArrayList<Object>();

        currentMap = new HashMap<String, Object>();
        additionalLines = new ArrayList<String>();

        this.updateCustomLevelDefinitionMap();

        if (timestampFormat != null) {
            dateFormat = new SimpleDateFormat(
                    quoteTimeStampChars(timestampFormat));
            timestampPatternText = convertTimestamp();
        }
        int index = 0;
        String current = newPattern;
        // build a list of property names and temporarily replace the property
        // with an empty string,
        // we'll rebuild the pattern later
        List<String> propertyNames = new ArrayList<String>();

        while (index > -1) {
            if (current.indexOf(PROP_START) > -1
                    && current.indexOf(PROP_END) > -1) {
                index = current.indexOf(PROP_START);
                String longPropertyName = current.substring(
                        current.indexOf(PROP_START),
                        current.indexOf(PROP_END) + 1);
                String shortProp = getShortPropertyName(longPropertyName);
                buildingKeywords.add(shortProp);
                propertyNames.add(longPropertyName);
                current = current.substring(longPropertyName.length() + 1
                        + index);
                newPattern = singleReplace(newPattern, longPropertyName,
                        new Integer(buildingKeywords.size() - 1).toString());
            } else {
                // no properties
                index = -1;
            }
        }

        /*
         * we're using a treemap, so the index will be used as the key to ensure
         * keywords are ordered correctly
         * 
         * examine pattern, adding keywords to an index-based map patterns can
         * contain only one of these per entry...properties are the only
         * 'keyword'
         * that can occur multiple times in an entry
         */
        Iterator<String> iter = keywords.iterator();
        while (iter.hasNext()) {
            String keyword = (String) iter.next();
            int index2 = newPattern.indexOf(keyword);
            if (index2 > -1) {
                buildingKeywords.add(keyword);
                newPattern = singleReplace(newPattern, keyword, new Integer(
                        buildingKeywords.size() - 1).toString());
            }
        }

        String buildingInt = "";

        for (int i = 0; i < newPattern.length(); i++) {
            String thisValue = String.valueOf(newPattern.substring(i, i + 1));
            if (isInteger(thisValue)) {
                buildingInt = buildingInt + thisValue;
            } else {
                if (isInteger(buildingInt)) {
                    matchingKeywords.add(buildingKeywords.get(Integer
                            .parseInt(buildingInt)));
                }
                // reset
                buildingInt = "";
            }
        }

        // if the very last value is an int, make sure to add it
        if (isInteger(buildingInt)) {
            matchingKeywords.add(buildingKeywords.get(Integer
                    .parseInt(buildingInt)));
        }

        newPattern = replaceMetaChars(newPattern);

        // compress one or more spaces in the pattern into the [ ]+ regexp
        // (supports padding of level in log files)
        newPattern = newPattern.replaceAll(MULTIPLE_SPACES_REGEXP,
                MULTIPLE_SPACES_REGEXP);
        newPattern = newPattern.replaceAll(Pattern.quote(PATTERN_WILDCARD),
                REGEXP_DEFAULT_WILDCARD);
        // use buildingKeywords here to ensure correct order
        for (int i = 0; i < buildingKeywords.size(); i++) {
            String keyword = (String) buildingKeywords.get(i);
            // make the final keyword greedy (we're assuming it's the message)
            if (i == (buildingKeywords.size() - 1)) {
                newPattern = singleReplace(newPattern, String.valueOf(i),
                        GREEDY_GROUP);
            } else if (TIMESTAMP.equals(keyword)) {
                newPattern = singleReplace(newPattern, String.valueOf(i), "("
                        + timestampPatternText + ")");
            } else if (LOGGER.equals(keyword) || LEVEL.equals(keyword)) {
                newPattern = singleReplace(newPattern, String.valueOf(i),
                        NOSPACE_GROUP);
            } else {
                newPattern = singleReplace(newPattern, String.valueOf(i),
                        DEFAULT_GROUP);
            }
        }
        this.setRegexp(newPattern);
    }

    public String getRegexp() {
        return regexp;
    }

    public void setRegexp(String regexp) {
        this.regexp = regexp;
    }

    public Pattern getRegexpPattern() {
        return regexpPattern;
    }

    public void setRegexpPattern(Pattern regexpPattern) {
        this.regexpPattern = regexpPattern;
    }

    public Pattern getExceptionPattern() {
        return exceptionPattern;
    }

    public void setExceptionPattern(Pattern exceptionPattern) {
        this.exceptionPattern = exceptionPattern;
    }

    public String getTimestampFormat() {
        return timestampFormat;
    }

    public void setTimestampFormat(String timestampFormat) {
        this.timestampFormat = timestampFormat;
    }

    public String getLogFormat() {
        return logFormat;
    }

    public void setLogFormat(String logFormat) {
        this.logFormat = logFormat;
    }

    public boolean isAppendNonMatches() {
        return appendNonMatches;
    }

    public void setAppendNonMatches(boolean appendNonMatches) {
        this.appendNonMatches = appendNonMatches;
    }

    /** copy form LogFilePatternReceiver **/
    /**
     * 
     * @param longPropertyName
     * 
     * @return
     */
    private String getShortPropertyName(String longPropertyName)
    {
        String currentProp = longPropertyName.substring(longPropertyName
                .indexOf(PROP_START));
        String prop = currentProp.substring(0,
                currentProp.indexOf(PROP_END) + 1);
        String shortProp = prop.substring(PROP_START.length(),
                prop.length() - 1);
        return shortProp;
    }

    private String singleReplace(String inputString, String oldString,
            String newString)
    {
        int propLength = oldString.length();
        int startPos = inputString.indexOf(oldString);
        if (startPos == -1)
        {
            logger.info(
                    "string: " + oldString + " not found in input: "
                            + inputString + " - returning input");
            return inputString;
        }
        if (startPos == 0)
        {
            inputString = inputString.substring(propLength);
            inputString = newString + inputString;
        } else {
            inputString = inputString.substring(0, startPos) + newString
                    + inputString.substring(startPos + propLength);
        }
        return inputString;
    }

    private boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    /**
     * Some perl5 characters may occur in the log file format.
     * Escape these characters to prevent parsing errors.
     * 
     * @param input
     * @return string
     */
    private String replaceMetaChars(String input) {
        // escape backslash first since that character is used to escape the
        // remaining meta chars
        input = input.replaceAll("\\\\", "\\\\\\");

        // don't escape star - it's used as the wildcard
        input = input.replaceAll(Pattern.quote("]"), "\\\\]");
        input = input.replaceAll(Pattern.quote("["), "\\\\[");
        input = input.replaceAll(Pattern.quote("^"), "\\\\^");
        input = input.replaceAll(Pattern.quote("$"), "\\\\$");
        input = input.replaceAll(Pattern.quote("."), "\\\\.");
        input = input.replaceAll(Pattern.quote("|"), "\\\\|");
        input = input.replaceAll(Pattern.quote("?"), "\\\\?");
        input = input.replaceAll(Pattern.quote("+"), "\\\\+");
        input = input.replaceAll(Pattern.quote("("), "\\\\(");
        input = input.replaceAll(Pattern.quote(")"), "\\\\)");
        input = input.replaceAll(Pattern.quote("-"), "\\\\-");
        input = input.replaceAll(Pattern.quote("{"), "\\\\{");
        input = input.replaceAll(Pattern.quote("}"), "\\\\}");
        input = input.replaceAll(Pattern.quote("#"), "\\\\#");
        return input;
    }

    private String quoteTimeStampChars(String input) {
        // put single quotes around text that isn't a supported dateformat char
        StringBuffer result = new StringBuffer();
        // ok to default to false because we also check for index zero below
        boolean lastCharIsDateFormat = false;
        for (int i = 0; i < input.length(); i++) {
            String thisVal = input.substring(i, i + 1);
            boolean thisCharIsDateFormat = VALID_DATEFORMAT_CHARS
                    .contains(thisVal);
            // we have encountered a non-dateformat char
            if (!thisCharIsDateFormat && (i == 0 || lastCharIsDateFormat)) {
                result.append("'");
            }
            // we have encountered a dateformat char after previously
            // encountering a non-dateformat char
            if (thisCharIsDateFormat && i > 0 && !lastCharIsDateFormat) {
                result.append("'");
            }
            lastCharIsDateFormat = thisCharIsDateFormat;
            result.append(thisVal);
        }
        // append an end single-quote if we ended with non-dateformat char
        if (!lastCharIsDateFormat) {
            result.append("'");
        }
        return result.toString();
    }

    /**
     * Helper method that will convert timestamp format to a pattern
     * 
     * 
     * @return string
     */
    private String convertTimestamp() {
        // some locales (for example, French) generate timestamp text with
        // characters not included in \w -
        // now using \S (all non-whitespace characters) instead of /w
        String result = timestampFormat.replaceAll(
                VALID_DATEFORMAT_CHAR_PATTERN + "+", "\\\\S+");
        // make sure dots in timestamp are escaped
        result = result.replaceAll(Pattern.quote("."), "\\\\.");
        return result;
    }

    /**
     * Walk the additionalLines list, looking for the EXCEPTION_PATTERN.
     * <p>
     * Return the index of the first matched line (the match may be the 1st line
     * of an exception)
     * <p>
     * Assumptions: <br>
     * - the additionalLines list may contain both message and exception lines<br>
     * - message lines are added to the additionalLines list and then exception
     * lines (all message lines occur in the list prior to all exception lines)
     * 
     * @return -1 if no exception line exists, line number otherwise
     */
    private int getExceptionLine() {
        for (int i = 0; i < additionalLines.size(); i++) {
            Matcher exceptionMatcher = exceptionPattern
                    .matcher((String) additionalLines.get(i));
            if (exceptionMatcher.matches()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Combine all exception lines occuring in the additionalLines list into a
     * String array
     * <p>
     * (all entries equal to or greater than the exceptionLine index)
     * 
     * @param exceptionLine
     *            index of first exception line
     * @return exception
     */
    private String[] buildException(int exceptionLine) {
        if (exceptionLine == -1) {
            return emptyException;
        }
        String[] exception = new String[additionalLines.size() - exceptionLine
                - 1];
        for (int i = 0; i < exception.length; i++) {
            exception[i] = (String) additionalLines.get(i + exceptionLine);
        }
        return exception;
    }

    /**
     * Combine all message lines occuring in the additionalLines list, adding
     * a newline character between each line
     * <p>
     * the event will already have a message - combine this message with the
     * message lines in the additionalLines list (all entries prior to the
     * exceptionLine index)
     * 
     * @param firstMessageLine
     *            primary message line
     * @param exceptionLine
     *            index of first exception line
     * @return message
     */
    private String buildMessage(String firstMessageLine, int exceptionLine) {
        if (additionalLines.size() == 0) {
            return firstMessageLine;
        }
        StringBuffer message = new StringBuffer();
        if (firstMessageLine != null) {
            message.append(firstMessageLine);
        }

        int linesToProcess = (exceptionLine == -1 ? additionalLines.size()
                : exceptionLine);

        for (int i = 0; i < linesToProcess; i++) {
            message.append(Const.NEW_LINE);
            message.append(additionalLines.get(i));
        }
        return message.toString();
    }

    private void updateCustomLevelDefinitionMap() {
        if (customLevelDefinitions != null) {
            StringTokenizer entryTokenizer = new StringTokenizer(
                    customLevelDefinitions, ",");

            customLevelDefinitionMap.clear();
            while (entryTokenizer.hasMoreTokens()) {
                StringTokenizer innerTokenizer = new StringTokenizer(
                        entryTokenizer.nextToken(), "=");
                customLevelDefinitionMap.put(innerTokenizer.nextToken(),
                        Level.toLevel(innerTokenizer.nextToken()));
            }
        }
    }

    /**
     * Convert the match into a map.
     * <p>
     * Relies on the fact that the matchingKeywords list is in the same order as
     * the groups in the regular expression
     * 
     * @param result
     * @return map
     */
    private Map<String, Object> processEntry(MatchResult result) {
        Map<String, Object> map = new HashMap<String, Object>();
        // group zero is the entire match - process all other groups
        for (int i = 1; i < result.groupCount() + 1; i++) {
            Object key = matchingKeywords.get(i - 1);
            Object value = result.group(i);
            map.put(key.toString(), value);

        }
        return map;
    }

    /** copy form LogFilePatternReceiver **/

    @Override
    public void setExcepression(String expression) {
        this.initialize(expression);

    }
}
