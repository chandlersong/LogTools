
package org.logtools.example;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.logtools.TestConst;
import org.logtools.script.ExecuteTimeFilterScript;

public class ExecuteTimeFilterScriptExample {

    private static Logger logger = Logger.getLogger(ExecuteTimeFilterScriptExample.class);

    private String exportFileName = FileUtils.getTempDirectory() + File.separator + "LogTools" + File.separator
            + RandomStringUtils.randomAlphabetic(10) + ".txt";

    private String logFileName = "src/test/resources/logfile/ExecuteTimeFilter.log";

    private String expression = TestConst.Log4jFormat;

    private int timeBarrier = 500;

    /**
     * default barrier is 500ms
     * 
     * @throws IOException
     */
    @Test
    public void testProcessSimple() throws IOException {
        File exportFile = new File(exportFileName);
        File logFile = new File(logFileName);
        exportFile.createNewFile();
        ExecuteTimeFilterScript script = new ExecuteTimeFilterScript();
        script.setExportFile(exportFile);
        script.setExcepression(expression);
        script.setBarrier(timeBarrier);
        script.process(logFile);
        logger.info("result file:" + exportFile.getAbsolutePath());

    }
}
