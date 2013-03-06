
package org.logtools.core.logprocess.plugin;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.logtools.core.domain.LogEntry;
import org.logtools.core.logprocess.LogPlugin;

public class AbsLogPlugin implements LogPlugin {

    private static final String SUMMARY_TXT = "Summary.csv";

    private String timestampFormat = "yyyy-MM-dd HH:mm:ss.SSS";

    public File generateSummaryFile(File exportFile) {
        String fileName = FilenameUtils.getBaseName(exportFile.getName()) + SUMMARY_TXT;
        File SummaryFile = new File(exportFile.getParent(), fileName);

        try {
            SummaryFile.createNewFile();
        } catch (IOException e) {
            // never happened;
        }

        return SummaryFile;
    }

    public String getTimestampFormat() {
        return timestampFormat;
    }

    public void setTimestampFormat(String timestampFormat) {
        this.timestampFormat = timestampFormat;
    }

    public void executeBeforePostLogEntry(LogEntry entry) {

    }

    public void executeAfterPostLogEntry(LogEntry entry) {

    }

    public void executeAfterProcess(File[] files) {

    }

    public void executeBeforeProcess(File[] files) {

    }

    public void executeAfterFinishOneFile(File file) {

    }

    public void executeBeforeFinishOneFile(File file) {

    }

}
