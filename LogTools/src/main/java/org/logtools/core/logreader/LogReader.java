package org.logtools.core.logreader;

import java.io.File;

import org.logtools.core.domain.LogConfiguration;
import org.logtools.core.domain.LogDocument;

public interface LogReader {

    /**
     * it should be the basic function of readLog. Because I don't have time to design Logconfiguration,  <br>
     * so readLog(File logFile, String pattern) will be the main function
     * @param logFile
     * @param configuration
     * @return
     */
    LogDocument readLog(LogConfiguration configuration);
    
    LogDocument readLog(File logFile, String pattern);
    
    /**
     * read the log all be default
     * @param logFile
     * @return
     */
    LogDocument readLog(File logFile);
}
