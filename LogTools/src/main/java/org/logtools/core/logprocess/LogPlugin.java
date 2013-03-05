
package org.logtools.core.logprocess;

import java.io.File;

import org.logtools.core.domain.LogEntry;

/**
 * @author Chandler.Song
 */
public interface LogPlugin {

    void executeBeforePostLogEntry(LogEntry entry);

    void executeAfterPostLogEntry(LogEntry entry);

    void executeAfterProcess(File[] files);

    void executeBeforeProcess(File[] files);

    void executeAfterFinishOneFile(File file);

    void executeBeforeFinishOneFile(File file);

    void setTimestampFormat(String timestampFormat);

    String getTimestampFormat();
}
