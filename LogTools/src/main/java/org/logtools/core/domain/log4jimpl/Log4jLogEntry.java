package org.logtools.core.domain.log4jimpl;

import java.util.ArrayList;
import java.util.Date;

import org.logtools.core.domain.CommonLogEntry;

/**
 * TODO try to use LogFilePatternReceiver to parse
 * 
 * @author Chandler.Song
 * 
 */
public class Log4jLogEntry extends CommonLogEntry {

    public Log4jLogEntry() {
        super();
    }

    public Log4jLogEntry(String threadInfo, Date time, String message,
            String level, String catalog, String line, String fileName,
            ArrayList<String> lineInFile, ArrayList<String> traceLog) {
        super(threadInfo, time, message, level, catalog, line, fileName,
                lineInFile,
                traceLog);
    }

}
