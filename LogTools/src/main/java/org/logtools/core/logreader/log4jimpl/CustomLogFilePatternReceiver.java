package org.logtools.core.logreader.log4jimpl;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.varia.LogFilePatternReceiver;

/**
 * 
 * Notes:
 * 1, after reading LogFilePatternReceiver, I found I need override process and
 * post method <br>
 * if I ONlY want to convert logEvent to logEntry, I don't need to override
 * process <br>
 * but if I need to get more information,like log is which line in file, I need
 * override the process <br>
 * 
 * IMPROVE:
 * support some special case,like two timestamp
 * 
 * @author Chandler.Song
 * 
 */
public class CustomLogFilePatternReceiver extends LogFilePatternReceiver {

    private static Logger logger = Logger
            .getLogger(CustomLogFilePatternReceiver.class);
    /* copy from super class */

    public static final String LOGGER = "LOGGER";
    public static final String MESSAGE = "MESSAGE";
    public static final String TIMESTAMP = "TIMESTAMP";
    public static final String NDC = "NDC"; // nested diagnostic context
    public static final String LEVEL = "LEVEL";
    public static final String THREAD = "THREAD";
    public static final String CLASS = "CLASS";
    public static final String FILE = "FILE";
    public static final String LINE = "LINE";
    public static final String METHOD = "METHOD";

    /* copy from super class */

    private String Log4jExpression;

    static interface Log4JConst {

        public static final char START = '%';
        public static final int UPPERCASE = 32;

        public static final String LOGGER = "%\\S*c";
        public static final char LOGGER_END = 'c';

        public static final String MESSAGE = "%\\S*m";
        public static final char MESSAGE_END = 'm';

        public static final String TIMESTAMP = "%\\S*d";
        public static final char TIMESTAMP_END = 'd';

        public static final char LEFT_SB = '{';
        public static final char RIGHT_SB = '}';

        public static final String LEVEL = "%\\S*p";
        public static final char LEVEL_END = 'p';

        public static final String THREAD = "%\\S*t";
        public static final char THREAD_END = 't';

        public static final String LINE = "%\\S*l";
        public static final char LINE_END = 'l';
    }

    @Override
    protected void initialize() {

        // check the format Expression, if it's log4j's, use custom flow
        if (StringUtils.isBlank(Log4jExpression)) {
            super.initialize();
        } else {
            this.initializeLog4j();
        }
    }

    private void initializeLog4j() {

    }

    /**
     * translate log4j expression like to %t %-d{yyyy-MM-dd HH:mm:ss.SSS}
     * %d{ABSOLUTE} %5p %c:%L - %m%n
     * to this can accept like MESSAGE,Thread
     * 
     * %m -->MESSAGE 输出代码中指定的消息
     * %p -->LEVEL 输出优先级，即 DEBUG ， INFO ， WARN ， ERROR ， FATAL
     * %r --> 输出自应用启动到输出该 log 信息耗费的毫秒数
     * %c --> LOGGER 输出所属的类目，通常就是所在类的全名
     * %t --> THREAD 输出产生该日志事件的线程名
     * %n --> 输出一个回车换行符， Windows 平台为 “rn” ， Unix 平台为 “n”
     * %d --> TIMESTAMP 输出日志时间点的日期或时间，默认格式为 ISO8601 ，也可以在其后指定格式，比如： %d{yyyy MMM
     * dd HH:mm:ss,SSS} ，输出类似： 2002 年 10 月 18 日 22 ： 10 ： 28 ， 921
     * %l --> LINE 输出日志事件的发生位置，包括类目名、发生的线程，以及在代码中的行数。
     * 
     * 
     * here, if %5m, should be translate to %m
     * 
     * if there are something which doesn't mention here, just ignore it
     * if there's timestamp after %d, it should can be added.
     * 
     * @param expression
     * @return
     */
    public String translateExpression(String expression) {

        StringBuilder result = new StringBuilder();
        StringBuilder previous = null;

        int cursor = 0;
        int length = expression.length();
        while (cursor < length) {

            char c = expression.charAt(cursor);

            if (Character.isUpperCase(c)) {
                c = Character.toLowerCase(c);
            }

            if (previous != null) {
                previous.append(c);
            }

            switch (c) {
            case Log4JConst.START: {
                previous = new StringBuilder();
                previous.append(c);
                break;
            }
            case Log4JConst.LOGGER_END: {
                this.vaildExpression(Log4JConst.LOGGER, previous, cursor,
                        expression, LOGGER, result);
                break;
            }
            case Log4JConst.LEVEL_END: {
                this.vaildExpression(Log4JConst.LEVEL, previous, cursor,
                        expression, LEVEL, result);
                break;
            }
            case Log4JConst.LINE_END: {
                this.vaildExpression(Log4JConst.LINE, previous, cursor,
                        expression, LINE, result);
                break;
            }
            case Log4JConst.MESSAGE_END: {
                this.vaildExpression(Log4JConst.MESSAGE, previous, cursor,
                        expression, MESSAGE, result);
                break;
            }
            case Log4JConst.THREAD_END: {
                this.vaildExpression(Log4JConst.THREAD, previous, cursor,
                        expression, THREAD, result);
                break;
            }
            case Log4JConst.TIMESTAMP_END: {
                if (Pattern.matches(Log4JConst.TIMESTAMP, previous.toString())) {
                    result.append(TIMESTAMP);
                    cursor = this.getAndSetTimeStampFormat(cursor + 1,
                            expression);
                    result.append(this.moveToNextStart(cursor + 1, expression));
                }

                break;
            }
            }

            cursor++;
        }

        return result.toString();
    }

    public int getAndSetTimeStampFormat(int cursor, String expression) {
        int length = expression.length();

        /*
         * move the cursor to previous position.
         * then it can work like there's no timestamp
         */
        if (expression.charAt(cursor) != Log4JConst.LEFT_SB) {
            return cursor - 1;
        }
        cursor++;
        StringBuffer timestampformat = new StringBuffer();
        for (; cursor < length; cursor++) {
            char c = expression.charAt(cursor);

            if (expression.charAt(cursor) == Log4JConst.RIGHT_SB) {
                String format = timestampformat.toString();
                logger.info("get timestamp format:" + format);
                this.setTimestampFormat(format);
                return cursor;
            } else {
                timestampformat.append(c);
            }

        }

        throw new IllegalStateException("not found { in regression");
    }

    /**
     * run the regular test
     * 
     * @param regularExpression
     * @param expression
     * @param expression2
     * @param cursor
     * @param logger2
     * @return
     */
    private void vaildExpression(String regularExpression,
            StringBuilder test, int cursor, String expression2,
            String appendString, StringBuilder result) {
        if (Pattern.matches(regularExpression, test.toString())) {
            result.append(appendString);
            result.append(this.moveToNextStart(cursor + 1, expression2));
        }
    }

    /**
     * move the cursor to the next start
     * 
     * @param cursor
     * @param expression
     * @return
     */
    private String moveToNextStart(int cursor, String expression) {
        int length = expression.length();

        StringBuffer result = new StringBuffer();
        for (; cursor < length; cursor++) {
            char c = expression.charAt(cursor);

            if (Log4JConst.START == c) {
                return result.toString();
            } else {
                result.append(c);
            }

        }
        return result.toString();
    }
}
