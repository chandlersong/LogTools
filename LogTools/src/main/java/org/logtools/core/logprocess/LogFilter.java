package org.logtools.core.logprocess;

import org.logtools.core.domain.LogEntry;

public interface LogFilter {

    Boolean acceptLog(LogEntry entry);
}
