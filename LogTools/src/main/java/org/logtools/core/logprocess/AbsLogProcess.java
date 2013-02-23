package org.logtools.core.logprocess;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.logtools.core.domain.LogEntry;

public abstract class AbsLogProcess implements LogProcess {

    List<LogPlugin> plugins;

    List<LogFilter> filters;

    public AbsLogProcess() {
        plugins = new ArrayList<LogPlugin>();
        filters = new ArrayList<LogFilter>();
    }

    public void process(File[] logFile) {
        // TODO Auto-generated method stub

    }

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
