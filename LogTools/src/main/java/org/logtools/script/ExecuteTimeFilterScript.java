
package org.logtools.script;

import java.io.File;
import java.util.List;

import org.logtools.core.logprocess.plugin.ExecuteTimeFilterPlugin;
import org.springframework.context.ApplicationContext;

/**
 * this script is to Log by the output format will be <br>
 * execute time,time <br>
 * Previous log,run time, previous message <br>
 * current log,run time, current message<br>
 * 
 * @author chandler.song
 */
public class ExecuteTimeFilterScript extends CommonScript {

    private static final String PROCESS_BEAN = "PROCESS_BEAN";

    private static final String TIME_FILTER_PLUGIN_BEAN = "TIME_FILTER_PLUGIN_BEAN";

    private ExecuteTimeFilterPlugin plugin;

    public ExecuteTimeFilterScript() {
        this.initialize();
    }

    private void initialize() {
        ApplicationContext context = this.getContext();
        plugin = context.getBean(TIME_FILTER_PLUGIN_BEAN, ExecuteTimeFilterPlugin.class);
    }

    @Override
    protected List<String> addConfigfile(List<String> filelist) {
        filelist.add("executeTimeFilterScript.xml");
        return filelist;
    }

    public File getExportFile() {
        return plugin.getExportFile();
    }

    public void setExportFile(File exportFile) {
        plugin.setExportFile(exportFile);
    }

    public int getBarrier() {
        return plugin.getTimeBarrier();
    }

    public void setBarrier(int barrier) {
        plugin.setTimeBarrier(barrier);
    }

    @Override
    protected String getProcessID() {
        return PROCESS_BEAN;
    }

}
