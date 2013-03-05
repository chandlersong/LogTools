
package org.logtools.core.logprocess.plugin;

import java.io.File;

import org.logtools.core.domain.LogEntry;
import org.logtools.core.logprocess.LogPlugin;

public class AbsLogPlugin implements LogPlugin {

    private String timestampFormat = "yyyy-MM-dd HH:mm:ss.SSS";

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
