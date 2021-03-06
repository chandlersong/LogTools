
package org.logtools.core.domain;

import java.util.ArrayList;
import java.util.Date;

/**
 * represent one line log with timestamp, <br>
 * other multi-line log, like exception with track log, should still be one logEntry
 * 
 * @author Chandler.Song
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

    public ArrayList<String> getLineInFile();

    public String getFileName();

    public Boolean isException();

    public ArrayList<String> getTraceLog();

    public String getLine();

    public String getThreadInfo();

    public Date getTime();

    public String getMessage();

    public String getLevel();

    public String getCatalog();

    public String getContent();

    public String getFirstLineNumber();
}
