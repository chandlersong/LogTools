
package org.logtools.core.logprocess.plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.logtools.Const;
import org.logtools.Exception.ExportResultException;
import org.logtools.core.domain.LogEntry;
import org.logtools.core.writer.impl.LogFileWriter;

/**
 * Filter the Log according to the execute time than the previous and output the log<br>
 * Format:<br>
 * execute time,time <br>
 * Previous log,run time, previous message <br>
 * current log,run time, current message<br>
 * 
 * @author chandler.song
 */
public class ExecuteTimeFilterPlugin extends AbsLogPlugin {

    private static final String DEFAULT_FILE_NAME = "ExecuteTimeFilter.txt";

    private Map<String, LogEntry> previousLogEntryMap;

    private int timeBarrier = 500;

    private File exportFile;

    private LogFileWriter writer;

    private final static String LINE_1 = "execute time,%1$s";

    private final static String LINE_2 = "Previous log,%1$s,%2$s";

    private final static String LINE_3 = "current log,%1$s,%2$s";

    private String timestampFormat = "yyyy-MM-dd HH:mm:ss.SSS";

    @Override
    public void executeAfterPostLogEntry(LogEntry entry) {
        // for concurrent thread
        String threadInfo = entry.getThreadInfo();
        LogEntry previousLogEntry = previousLogEntryMap.get(threadInfo);
        previousLogEntryMap.put(threadInfo, entry);

        // no previous log
        if (previousLogEntry == null) {
            return;
        }

        Long delta = entry.getTime().getTime() - previousLogEntry.getTime().getTime();
        previousLogEntry = entry;

        if (delta > timeBarrier) {
            this.exportLog(entry, previousLogEntry, delta);
        }
    }

    @Override
    public void executeAfterProcess(File[] files) {
        writer.close();
    }

    @Override
    public void executeBeforeProcess(File[] files) {
        this.perpareExportFile();
        writer = new LogFileWriter(exportFile);
        writer.start();

        previousLogEntryMap = new HashMap<String, LogEntry>();
    }

    private void exportLog(LogEntry current, LogEntry pervious, long delta) {
        FastDateFormat format = FastDateFormat.getInstance(timestampFormat);
        writer.writeOneLine(String.format(LINE_1, String.valueOf(delta)));
        writer.writeOneLine(String.format(LINE_2, format.format(pervious.getTime()), pervious.getMessage()));
        writer.writeOneLine(String.format(LINE_3, format.format(current.getTime()), current.getMessage()));
    }

    private void perpareExportFile() {

        if (exportFile == null) {
            exportFile = new File(Const.DEFALT_REPORT_FOLDER, RandomStringUtils.randomAlphabetic(5) + DEFAULT_FILE_NAME);
            try {
                exportFile.createNewFile();
            } catch (IOException e) {
                ExportResultException ex = new ExportResultException();
                ex.initCause(e);
                throw ex;
            }
        }
    }

    public int getTimeBarrier() {
        return timeBarrier;
    }

    /**
     * Milliseconds
     * 
     * @param timeBarrier
     */
    public void setTimeBarrier(int timeBarrier) {
        this.timeBarrier = timeBarrier;
    }

    public File getExportFile() {
        return exportFile;
    }

    public void setExportFile(File exportFile) {
        this.exportFile = exportFile;
    }

    public String getTimestampFormat() {
        return timestampFormat;
    }

    public void setTimestampFormat(String timestampFormat) {
        this.timestampFormat = timestampFormat;
    }

}
