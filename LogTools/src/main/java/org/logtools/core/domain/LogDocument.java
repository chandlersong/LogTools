package org.logtools.core.domain;

import java.util.List;

/**
 * a log file
 * @author Chandler.Song
 *
 */
public interface LogDocument extends Iterable<LogEntry>{

    
    public void addLine(LogEntry le);
    
    
    /**
     * if there's one multi-line log. <br>
     *  One scenario <br>
     *   one exception log, the first line is 10 and prints 10 lines<br>
     *   The next log which lineNumber is 11<br>
     *   
     * TODO it should be remove form here
     * TODO need to find a way to store the actual line number for log,for example 
     *       one exception log, the first line is 10 and prints 10 lines<br>
     *       from line 10 to 20. it should return this one
     * @param lineNumber
     * @return the logEntry you want to find
     */
    public LogEntry findLog(int lineNumber);
    
    /**
     * how many logEntry in this document
     * @return how many logEntry in this document
     */
    public Integer size();
    
    /**
     * return all the entry
     * @return
     */
    public List<LogEntry> listAllEntry();

}
