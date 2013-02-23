package org.logtools.core.domain;

import java.util.Date;

/**
 * represent one line log with timestamp, <br>
 * other multi-line log, like exception with track log, should still be one
 * logEntry
 * 
 * @author Chandler.Song
 * 
 */
public interface LogEntry {

    /**
     * for some case, some value have to get according to the pattern. <br>
     * the implementation is optional
     * 
     * @param pattern
     * @return
     */
    public String get(String pattern);

    /**
     * get the line number of this log<br>
     * 
     * if one log entry is multi-line, for example. it's a exception log. it's
     * line number is 10 and prints 10 lines.<br>
     * the next LogEntry's lineNumber is 11
     * 
     * @return
     */
    public Integer getLineNumber();

    public String getLine();

    public Boolean isException();

    public Boolean isMultiLines();

    public String getThreadInfo();

    public Date getTime();

    public String getMessage();

    public String getLevel();

    public String getCatalog();
}
