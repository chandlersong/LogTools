
package org.logtools.core.writer;

import org.logtools.core.domain.LogEntry;

public interface LogWriter {

    void writeOneLine(String line);

    void writeOneLogEntry(LogEntry logEntry);

    void start();

    void flush();

    void close();
}
