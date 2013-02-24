package org.logtools.core.logprocess;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.logtools.core.domain.LogEntry;

public abstract class AbsLogProcess implements LogProcess {

    private List<LogPlugin> plugins;

    private List<LogFilter> filters;

    private Boolean processOnefile = true;

    public AbsLogProcess() {
        plugins = new ArrayList<LogPlugin>();
        filters = new ArrayList<LogFilter>();
    }

    public void process(File[] logFiles) {
        processOnefile = false;
        for (LogPlugin plugin : plugins) {
            plugin.executeBeforeProcess(logFiles);
        }

        // sort file by modify date
        Arrays.sort(logFiles, new Comparator<File>() {

            public int compare(File o1, File o2) {
                return (int) (o1.lastModified() - o2.lastModified());
            }

        });

        int filesLength = logFiles.length;
        for (int i = 0; i < filesLength; i++) {
            this.process(logFiles[i]);
        }
        for (LogPlugin plugin : plugins) {
            plugin.executeAfterProcess(logFiles);
        }
    }

    public void process(File logFile) {

        if (processOnefile) {
            for (LogPlugin plugin : plugins) {
                plugin.executeBeforeProcess(new File[] { logFile });
            }
        }

        for (LogPlugin plugin : plugins) {
            plugin.executeBeforeFinishOneFile(logFile);
        }
        this.doProcess(logFile);
        for (LogPlugin plugin : plugins) {
            plugin.executeAfterFinishOneFile(logFile);
        }

        if (processOnefile) {
            for (LogPlugin plugin : plugins) {
                plugin.executeAfterProcess(new File[] { logFile });
            }
        }

    }

    protected abstract void doProcess(File file);

    public abstract void setExcepression(String expression);

    public void addFilter(LogFilter... filter) {
        this.filters.addAll(Arrays.asList(filter));

    }

    public void addPlugin(LogPlugin... plugin) {
        this.plugins.addAll(Arrays.asList(plugin));

    }

    protected void post(LogEntry entry) {
        // filter log entry
        if (!this.loopFilters(entry)) {
            return;
        }

        for (LogPlugin plugin : plugins) {
            plugin.executeBeforePostLogEntry(entry);
        }
        this.doPost(entry);
        for (LogPlugin plugin : plugins) {
            plugin.executeAfterPostLogEntry(entry);
        }
    }

    /**
     * do something if you want to do something
     * 
     * @param event
     */
    protected void doPost(LogEntry event) {

    }

    private Boolean loopFilters(LogEntry entry) {

        Boolean result = Boolean.TRUE;

        for (LogFilter filter : filters) {
            if (!filter.acceptLog(entry)) {
                return Boolean.FALSE;
            }
        }
        return result;
    }

}
