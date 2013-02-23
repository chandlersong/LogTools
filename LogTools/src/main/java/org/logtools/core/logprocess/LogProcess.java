package org.logtools.core.logprocess;

import java.io.File;

public interface LogProcess {

    void process(File logFile);

    /**
     * this method needs output file;
     * 
     * @param logFile
     */
    void process(File[] logFile);

    void setExcepression(String expression);

    void addFilter(LogFilter... filter);

    void addPlugin(LogPlugin... plugin);
}
