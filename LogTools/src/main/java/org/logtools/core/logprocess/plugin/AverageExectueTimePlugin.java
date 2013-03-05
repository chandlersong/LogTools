
package org.logtools.core.logprocess.plugin;

import java.io.File;

import org.logtools.core.writer.impl.LogFileWriter;
import org.logtools.script.object.LogRange;

public class AverageExectueTimePlugin extends AbsLogPlugin {

    private LogRange range;

    private File exportFile;

    private LogFileWriter writer;

    public LogRange getRange() {
        return range;
    }

    public void setRange(LogRange range) {
        this.range = range;
    }

    public File getExportFile() {
        return exportFile;
    }

    public void setExportFile(File exportFile) {
        this.exportFile = exportFile;
    }

    public LogFileWriter getWriter() {
        return writer;
    }

    public void setWriter(LogFileWriter writer) {
        this.writer = writer;
    }

}
