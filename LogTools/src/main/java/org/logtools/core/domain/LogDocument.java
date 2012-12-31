package org.logtools.core.domain;

/**
 * a log file
 * @author Chandler.Song
 *
 */
public interface LogDocument {

    
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
     * @return
     */
    public LogEntry findLog(int lineNumber);
}
