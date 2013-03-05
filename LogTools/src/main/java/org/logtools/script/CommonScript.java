
package org.logtools.script;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.logtools.core.logprocess.LogProcess;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * add some common support to script, like spring support
 * 
 * @author Chandler.Song
 */
public abstract class CommonScript implements Script {

    private LogProcess process;

    private ApplicationContext context;

    public CommonScript() {
        this.inital();
    }

    public void inital() {
        context = new ClassPathXmlApplicationContext(this.getConfigureList());
        process = context.getBean(this.getProcessID(), LogProcess.class);
    }

    protected final String[] getConfigureList() {

        List<String> filelist = this.addConfigfile(new ArrayList<String>());

        return filelist.toArray(new String[0]);
    }

    public ApplicationContext getContext() {
        return context;
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    public void setExcepression(String expression) {
        process.setExcepression(expression);
    }

    public void process(File log) {
        process.process(log);
    }

    public void process(File[] logs) {
        process.process(logs);
    }

    public LogProcess getProcess() {
        return process;
    }

    public void setProcess(LogProcess process) {
        this.process = process;
    }

    /**
     * the sub class should add the spring file it need to for the script
     * 
     * @param filelist
     * @return
     */
    protected abstract List<String> addConfigfile(List<String> filelist);

    protected abstract String getProcessID();
}
