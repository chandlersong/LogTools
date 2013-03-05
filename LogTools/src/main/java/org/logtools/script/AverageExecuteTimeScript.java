
package org.logtools.script;

import java.io.File;
import java.util.List;

import org.logtools.core.logprocess.plugin.AverageExectueTimePlugin;
import org.logtools.script.object.LogRange;
import org.springframework.context.ApplicationContext;

public class AverageExecuteTimeScript extends CommonScript {

    private static final String PROCESS_BEAN = "PROCESS_BEAN";

    private static final String AVERAGE_EXECUTE_TIME_PLUGIN_BEAN = "AVERAGE_EXECUTE_TIME_PLUGIN_BEAN";

    private AverageExectueTimePlugin plugin;

    public AverageExecuteTimeScript() {
        this.initialize();
    }

    private void initialize() {
        ApplicationContext context = this.getContext();
        plugin = context.getBean(AVERAGE_EXECUTE_TIME_PLUGIN_BEAN, AverageExectueTimePlugin.class);
    }

    @Override
    protected List<String> addConfigfile(List<String> filelist) {
        filelist.add("AverageExecuteTimeScript.xml");
        return filelist;
    }

    public File getExportFile() {
        return plugin.getExportFile();
    }

    public void setExportFile(File exportFile) {
        plugin.setExportFile(exportFile);
    }

    public void setLogRange(LogRange range) {
        plugin.setRange(range);
    }

    @Override
    protected String getProcessID() {
        return PROCESS_BEAN;
    }
}
