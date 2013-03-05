
package org.logtools.core.logprocess.plugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;
import org.logtools.core.domain.LogEntry;
import org.logtools.core.writer.impl.LogFileWriter;
import org.logtools.script.object.LogRange;

public class AverageExectueTimePlugin extends AbsLogPlugin {

    private static Logger logger = Logger.getLogger(AverageExectueTimePlugin.class);

    private LogRange range;

    private Pattern startPattern;

    private Pattern endPattern;

    private Map<String, Boolean> inRangeMap;

    private File exportFile;

    private LogFileWriter writer;

    private Map<String, LogEntry> startLogMap;

    private static class ExportContent {
        private final static String DETAIL_LINE_1 = "execute time,%1$s";

        private final static String DETAIL_LINE_2 = "start log,%1$s,%2$s,%3$s";

        private final static String DETAIL_LINE_3 = "end log,%1$s,%2$s,%3$s";

        private final static String DETAIL_MISSING_START = "missing start";

        private final static String DETAIL_MISSING_END = "missing end";
    }

    @Override
    public void executeAfterPostLogEntry(LogEntry entry) {

        String thread = entry.getThreadInfo();

        Boolean inRange = ObjectUtils.defaultIfNull(inRangeMap.get(thread), Boolean.FALSE);
        LogEntry startLog = startLogMap.get(thread);

        String message = entry.getMessage();
        if (inRange) {
            /**
             * this means missing end message,<br>
             * it will start a new range<br>
             * and export current
             */
            if (startPattern.matcher(message).matches()) {
                this.exportLog(startLog, null);
                startLogMap.put(thread, entry);
            }

            /**
             * one range is over
             */
            if (endPattern.matcher(message).matches()) {
                this.exportLog(startLog, entry);
                inRangeMap.put(thread, Boolean.FALSE);
                startLogMap.put(thread, null);
            }
        } else {

            /**
             * start one range
             */
            if (startPattern.matcher(message).matches()) {
                inRangeMap.put(thread, Boolean.TRUE);
                startLogMap.put(thread, entry);
            }

            /**
             * this means missing start message,<br>
             * it will start a new range<br>
             */
            if (endPattern.matcher(message).matches()) {
                this.exportLog(null, entry);
            }
        }
    }

    private void exportLog(LogEntry startlog, LogEntry endlog) {
        // header
        if (startlog != null && endlog != null) {
            Long delta = endlog.getTime().getTime() - startlog.getTime().getTime();
            writer.writeOneLine(String.format(ExportContent.DETAIL_LINE_1, delta));
        } else if (startlog != null && endlog == null) {
            writer.writeOneLine(ExportContent.DETAIL_MISSING_END);
        } else if (startlog == null && endlog != null) {
            writer.writeOneLine(ExportContent.DETAIL_MISSING_START);
        } else {
            return;
        }

        FastDateFormat format = FastDateFormat.getInstance(this.getTimestampFormat());
        // second line
        if (startlog != null) {
            writer.writeOneLine(String.format(ExportContent.DETAIL_LINE_2, startlog.getThreadInfo(),
                    format.format(startlog.getTime()), startlog.getMessage()));
        }

        // third line
        if (endlog != null) {
            writer.writeOneLine(String.format(ExportContent.DETAIL_LINE_3, endlog.getThreadInfo(),
                    format.format(endlog.getTime()), endlog.getMessage()));
        }
    }

    public void initialize() {

        if (range == null || StringUtils.isBlank(range.getStartLogMessage())
                || StringUtils.isBlank(range.getEndLogMessage())) {
            logger.warn("AverageExectueTimePlugin hasn't been inintilized");
            return;
        } else {
            logger.info("AverageExectueTimePlugin has been inintilized");
        }
        startPattern = Pattern.compile(range.getStartLogMessage());
        endPattern = Pattern.compile(range.getEndLogMessage());

        inRangeMap = new HashMap<String, Boolean>();
        startLogMap = new HashMap<String, LogEntry>();
    }

    public void setRange(LogRange range) {
        if (StringUtils.isBlank(range.getStartLogMessage()) || StringUtils.isBlank(range.getEndLogMessage())) {
            throw new IllegalArgumentException("start message or end message is blank");
        }

        if (range.getStartLogMessage().equals(StringUtils.isBlank(range.getEndLogMessage()))) {
            throw new IllegalArgumentException("start message are  end message equal");
        }
        this.range = range;
    }

    @Override
    public void executeAfterProcess(File[] files) {
        writer.close();
    }

    @Override
    public void executeBeforeProcess(File[] files) {
        writer = new LogFileWriter(exportFile);
        writer.start();

        this.initialize();
    }

    public File getExportFile() {
        return exportFile;
    }

    public void setExportFile(File exportFile) {
        this.exportFile = exportFile;
    }

    public LogFileWriter getWriter() {
        return writer;
    }

    public void setWriter(LogFileWriter writer) {
        this.writer = writer;
    }

    public LogRange getRange() {
        return range;
    }

    public Map<String, Boolean> getInRangeMap() {
        return inRangeMap;
    }

    public void setInRangeMap(Map<String, Boolean> inRangeMap) {
        this.inRangeMap = inRangeMap;
    }

    public Map<String, LogEntry> getStartLogMap() {
        return startLogMap;
    }

    public void setStartLogMap(Map<String, LogEntry> startLogMap) {
        this.startLogMap = startLogMap;
    }

}
