
package org.logtools.core.logprocess.filter;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.logtools.Const;
import org.logtools.Exception.ExportResultException;
import org.logtools.core.domain.LogEntry;
import org.logtools.core.logprocess.LogFilter;

/**
 * Filter the Log according to the execute time than the previous and output the log
 * 
 * @author chandler.song
 */
public class ExecuteTimeFilter implements LogFilter {

    private static final String DEFAULT_FILE_NAME = "ExecuteTimeFilter.txt";

    private LogEntry previousLogEntry;

    private int timeBarrier = 500;

    private String exportFileName;

    public Boolean acceptLog(LogEntry entry) {

        Long delta = entry.getTime().getTime() - previousLogEntry.getTime().getTime();
        previousLogEntry = entry;

        if (delta > timeBarrier) {
            this.exportLog(entry, previousLogEntry);
            return true;
        }
        return false;
    }

    private void exportLog(LogEntry current, LogEntry pervious) {

        File exportFile;
        if (StringUtils.isBlank(exportFileName)) {
            exportFile = new File(Const.DEFALT_REPORT_FOLDER, DEFAULT_FILE_NAME);
        } else {
            exportFile = new File(exportFileName);
            exportFile.getParentFile().mkdirs();
        }
        try {
            exportFile.createNewFile();
        } catch (IOException e) {
            ExportResultException ex = new ExportResultException();
            ex.initCause(e);
            throw ex;
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

    public String getExportFileName() {
        return exportFileName;
    }

    public void setExportFileName(String exportFileName) {
        this.exportFileName = exportFileName;
    }

}
