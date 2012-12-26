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
     *   it should return the logEntry, if i want to find log which lineNumber is 15<br>
     *   
     * TODO it should be remove form here
     * 
     * @param lineNumber
     * @return
     */
    public LogEntry findLog(int lineNumber);
}
