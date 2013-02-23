package org.logtools.core.logprocess;

import org.logtools.core.domain.LogEntry;

/**
 * 
 * @author Chandler.Song
 * 
 */
public interface LogPlugin {

    void executeBeforePostLogEntry(LogEntry entry);

    void executeAfterPostLogEntry(LogEntry entry);

    void executeAfterProcess();

    void executeBeforeProcess();

    void executeAfterFinishOneFile();

    void executeBeforeFinishOneFile();
}
