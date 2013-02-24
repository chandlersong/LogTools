package org.logtools.core.domain;

import java.util.Date;
import java.util.Vector;

public class CommonLogEntry implements LogEntry {

    private String threadInfo;
    private Date time;
    private String message;
    private String level;
    private String catalog;
    private String line;
    private String fileName;
    private Vector<String> lineInFile;
    private Vector<String> traceLog;

    public CommonLogEntry() {

    }

    public CommonLogEntry(String threadInfo, Date time, String message,
            String level, String catalog, String line, String fileName,
            Vector<String> lineInFile, Vector<String> traceLog) {
        super();
        this.threadInfo = threadInfo;
        this.time = time;
        this.message = message;
        this.level = level;
        this.catalog = catalog;
        this.line = line;
        this.fileName = fileName;
        this.lineInFile = lineInFile;
        this.traceLog = traceLog;
    }

    public String getThreadInfo() {
        return threadInfo;
    }

    public void setThreadInfo(String threadInfo) {
        this.threadInfo = threadInfo;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Vector<String> getLineInFile() {
        return lineInFile;
    }

    public void setLineInFile(Vector<String> lineInFile) {
        this.lineInFile = lineInFile;
    }

    public Vector<String> getTraceLog() {
        return traceLog;
    }

    public void setTraceLog(Vector<String> traceLog) {
        this.traceLog = traceLog;
    }

    public String get(String pattern) {
        throw new UnsupportedOperationException();
    }

    public Boolean isException() {
        return !this.traceLog.isEmpty();
    }

    @Override
    public String toString() {
        return "CommonLogEntry [threadInfo=" + threadInfo + ", time=" + time
                + ", message=" + message + ", level=" + level + ", catalog="
                + catalog + ", line=" + line + ", fileName=" + fileName
                + ", lineInFile=" + lineInFile + ", traceLog=" + traceLog + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((catalog == null) ? 0 : catalog.hashCode());
        result = prime * result
                + ((fileName == null) ? 0 : fileName.hashCode());
        result = prime * result + ((level == null) ? 0 : level.hashCode());
        result = prime * result + ((line == null) ? 0 : line.hashCode());
        result = prime * result
                + ((lineInFile == null) ? 0 : lineInFile.hashCode());
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        result = prime * result
                + ((threadInfo == null) ? 0 : threadInfo.hashCode());
        result = prime * result + ((time == null) ? 0 : time.hashCode());
        result = prime * result
                + ((traceLog == null) ? 0 : traceLog.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CommonLogEntry other = (CommonLogEntry) obj;
        if (catalog == null) {
            if (other.catalog != null)
                return false;
        } else if (!catalog.equals(other.catalog))
            return false;
        if (fileName == null) {
            if (other.fileName != null)
                return false;
        } else if (!fileName.equals(other.fileName))
            return false;
        if (level == null) {
            if (other.level != null)
                return false;
        } else if (!level.equals(other.level))
            return false;
        if (line == null) {
            if (other.line != null)
                return false;
        } else if (!line.equals(other.line))
            return false;
        if (lineInFile == null) {
            if (other.lineInFile != null)
                return false;
        } else if (!lineInFile.equals(other.lineInFile))
            return false;
        if (message == null) {
            if (other.message != null)
                return false;
        } else if (!message.equals(other.message))
            return false;
        if (threadInfo == null) {
            if (other.threadInfo != null)
                return false;
        } else if (!threadInfo.equals(other.threadInfo))
            return false;
        if (time == null) {
            if (other.time != null)
                return false;
        } else if (!time.equals(other.time))
            return false;
        if (traceLog == null) {
            if (other.traceLog != null)
                return false;
        } else if (!traceLog.equals(other.traceLog))
            return false;
        return true;
    }

}
