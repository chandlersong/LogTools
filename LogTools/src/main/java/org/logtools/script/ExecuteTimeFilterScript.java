
package org.logtools.script;

import java.io.File;
import java.util.List;

/**
 * this script is to Log by the output format will be <br>
 * execute time,time <br>
 * Previous log,run time, previous message <br>
 * current log,run time, current message<br>
 * 
 * @author chandler.song
 */
public class ExecuteTimeFilterScript extends CommonScript {

    private File exportFile;

    private int barrier;

    @Override
    protected List<String> addConfigfile(List<String> filelist) {
        filelist.add("executeTimeFilterScript.xml");
        return filelist;
    }

    public File getExportFile() {
        return exportFile;
    }

    public void setExportFile(File exportFile) {
        this.exportFile = exportFile;
    }

    public void process(File log) {

    }

    public void process(File[] logs) {

    }

    public int getBarrier() {
        return barrier;
    }

    public void setBarrier(int barrier) {
        this.barrier = barrier;
    }

}
