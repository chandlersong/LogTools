
package org.logtools.core.logprocess.plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.log4j.Logger;
import org.logtools.Const;
import org.logtools.core.domain.LogEntry;
import org.logtools.core.logprocess.plugin.exxecutetimefilterobjects.SortbyMeanTimeComparator;
import org.logtools.core.logprocess.plugin.exxecutetimefilterobjects.SummaryResult;
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

    private Map<String, LogEntry> previousLogEntryMap; // thread, logEntry

    private int timeBarrier = 500;

    private File exportFile;

    private LogFileWriter writer;

    private final static String LINE_1 = "execute time,%1$s";

    private final static String LINE_2 = "Previous log,%1$s,%2$s,%3$s";

    private final static String LINE_3 = "current log,%1$s,%2$s,%3$s";

    private final static String TITLE = "frequency,mean,mid,90% value,sd,max,min,message" + Const.NEW_LINE;

    private final static String SUMMARY_FORMAT = "%1$s,%2$s,%3$s,%4$s,%5$s,%6$s,%7$s,%8$s" + Const.NEW_LINE;

    // message, SummaryResult
    private Map<String, SummaryResult> summaryMap = new HashMap<String, SummaryResult>();

    private static Logger logger = Logger.getLogger(ExecuteTimeFilterPlugin.class);

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

        if (delta > timeBarrier) {
            this.exportLog(entry, previousLogEntry, delta);

            String message = entry.getMessage();
            SummaryResult sr = summaryMap.get(entry.getMessage());

            if (sr == null) {
                sr = new SummaryResult();
                sr.setMessage(message);
            }

            sr.addValue(delta);
            summaryMap.put(message, sr);

        }
    }

    @Override
    public void executeAfterProcess(File[] files) {
        writer.close();

        try {
            this.exportSummary();
        } catch (IOException e) {
            logger.error("generate summary file error", e);
        }
    }

    private void exportSummary() throws IOException {

        File SummaryFile = this.generateSummaryFile(exportFile);

        try {
            SummaryFile.createNewFile();
        } catch (IOException e) {
            // never happened;
        }

        FileUtils.writeStringToFile(SummaryFile, TITLE, true);
        ArrayList<SummaryResult> srList = new ArrayList<SummaryResult>(summaryMap.values());
        Collections.sort(srList, new SortbyMeanTimeComparator());

        Iterator<SummaryResult> iter = srList.iterator();

        SummaryResult sr;
        DescriptiveStatistics statistics;
        String message;
        String line;
        while (iter.hasNext()) {
            sr = iter.next();
            statistics = sr.getStatistics();
            message = sr.getMessage();
            // Frequency,avg,mid,90% value,sd,max,min,message
            line = String.format(SUMMARY_FORMAT, statistics.getN(), statistics.getMean(), statistics.getPercentile(50),
                    statistics.getPercentile(90), statistics.getStandardDeviation(), statistics.getMax(),
                    statistics.getMin(), message);
            FileUtils.writeStringToFile(SummaryFile, line, true);
        }
    }

    @Override
    public void executeBeforeProcess(File[] files) {
        writer = new LogFileWriter(exportFile);
        writer.start();

        previousLogEntryMap = new HashMap<String, LogEntry>();
    }

    private void exportLog(LogEntry current, LogEntry pervious, long delta) {
        FastDateFormat format = FastDateFormat.getInstance(this.getTimestampFormat());
        writer.writeOneLine(String.format(LINE_1, String.valueOf(delta)));
        writer.writeOneLine(String.format(LINE_2, pervious.getLineInFile(), format.format(pervious.getTime()),
                pervious.getMessage()));
        writer.writeOneLine(String.format(LINE_3, current.getLineInFile(), format.format(current.getTime()),
                current.getMessage()));
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

}
