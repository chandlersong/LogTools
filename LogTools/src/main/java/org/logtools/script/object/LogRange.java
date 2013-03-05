package org.logtools.script.object;

/**
 * For averageExecuteTimeScript
 * to define a loop where a log start and where a log end
 * 
 * @author Chandler.Song
 * 
 */
public class LogRange {

    private String startLogMessage;

    private String endLogMessage;

    public LogRange() {

    }

    public LogRange(String startLogMessage, String endLogMessage) {
        super();
        this.startLogMessage = startLogMessage;
        this.endLogMessage = endLogMessage;
    }

    public String getStartLogMessage() {
        return startLogMessage;
    }

    public void setStartLogMessage(String startLogMessage) {
        this.startLogMessage = startLogMessage;
    }

    public String getEndLogMessage() {
        return endLogMessage;
    }

    public void setEndLogMessage(String endLogMessage) {
        this.endLogMessage = endLogMessage;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((endLogMessage == null) ? 0 : endLogMessage.hashCode());
        result = prime * result + ((startLogMessage == null) ? 0 : startLogMessage.hashCode());
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
        LogRange other = (LogRange) obj;
        if (endLogMessage == null) {
            if (other.endLogMessage != null)
                return false;
        } else if (!endLogMessage.equals(other.endLogMessage))
            return false;
        if (startLogMessage == null) {
            if (other.startLogMessage != null)
                return false;
        } else if (!startLogMessage.equals(other.startLogMessage))
            return false;
        return true;
    }

}
