package org.logtools.core.domain.log4jimpl;

import java.util.Date;

import org.logtools.core.domain.LogEntry;

/**
 * TODO try to use LogFilePatternReceiver to parse
 * 
 * @author Chandler.Song
 * 
 */
public class Log4jLogEntry implements LogEntry {

    private Integer lineNumber;
    private String threadInfo;
    private Date time;
    private String message;
    private String level;
    private String catalog;
    private String Line;

    public Log4jLogEntry() {

    }

    public Log4jLogEntry(Integer lineNumber, String threadInfo, Date time,
            String message, String level, String catalog) {
        super();
        this.lineNumber = lineNumber;
        this.threadInfo = threadInfo;
        this.time = time;
        this.message = message;
        this.level = level;
        this.catalog = catalog;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public void setThreadInfo(String threadInfo) {
        this.threadInfo = threadInfo;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public Integer getLineNumber() {
        // TODO Auto-generated method stub
        return lineNumber;
    }

    public Boolean isException() {
        // TODO Auto-generated method stub
        return null;
    }

    public Boolean isMultiLines() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getThreadInfo() {
        return threadInfo;
    }

    public Date getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    public String getLevel() {
        return level;
    }

    public String getCatalog() {
        return catalog;
    }

    public String get(String pattern) {
        // TODO Auto-generated method stub
        return null;
    }

    public String getLine() {
        return Line;
    }

    public void setLine(String line) {
        Line = line;
    }

}
