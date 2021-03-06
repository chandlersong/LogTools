
package org.logtools.script;

import java.io.File;
import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.logtools.TestConst;

public class ExecuteTimeFilterScriptTest {

    private static Logger logger = Logger.getLogger(ExecuteTimeFilterScriptTest.class);

    /**
     * default barrier is 500ms
     * 
     * @throws IOException
     */
    @Test
    public void testProcessSimple() throws IOException {
        File exportFile = new File(TestConst.OutputPath, RandomStringUtils.randomAlphabetic(10) + ".csv");
        File logFile = new File("src/test/resources/logfile/ExecuteTimeFilter.log");
        exportFile.createNewFile();
        ExecuteTimeFilterScript script = new ExecuteTimeFilterScript();
        script.setExportFile(exportFile);
        script.setExcepression(TestConst.Log4jFormat);
        script.process(logFile);
        logger.info("result file:" + exportFile.getAbsolutePath());
        List<String> lines = FileUtils.readLines(exportFile);
        Assert.assertEquals(12, lines.size());

        Assert.assertEquals("execute time,600", lines.get(0));
        Assert.assertEquals("Previous log,2013-02-26 16:33:27.691,3", lines.get(1));
        Assert.assertEquals("current log,2013-02-26 16:33:28.291,4", lines.get(2));

        Assert.assertEquals("execute time,700", lines.get(3));
        Assert.assertEquals("Previous log,2013-02-26 16:33:28.292,7", lines.get(4));
        Assert.assertEquals("current log,2013-02-26 16:33:28.992,8", lines.get(5));
    }

    /**
     * default barrier is 500ms
     * 
     * @throws IOException
     */
    @Test
    public void testProcessSummary() throws IOException {
        String randomFileName = RandomStringUtils.randomAlphabetic(10);
        File exportFile = new File(TestConst.OutputPath, randomFileName + ".txt");
        File logFile = new File("src/test/resources/logfile/ExecuteTimeFilterSummary.log");
        exportFile.createNewFile();

        ExecuteTimeFilterScript script = new ExecuteTimeFilterScript();
        script.setExportFile(exportFile);
        script.setExcepression(TestConst.Log4jFormat);
        script.process(logFile);

        File exportSummaryFile = new File(TestConst.OutputPath, randomFileName + "Summary.txt");
        Assert.assertTrue(exportSummaryFile.exists());

        logger.info("result file:" + exportFile.getAbsolutePath());
        List<String> lines = FileUtils.readLines(exportSummaryFile);

        Assert.assertEquals(3, lines.size());
        Assert.assertEquals("frequency,mean,mid,90% value,sd,max,min,message", lines.get(0));
        Assert.assertEquals("6,684.0,700.0,703.0,41.16794869798591,703.0,600.0,xxxxx", lines.get(1));
        Assert.assertEquals("7,657.8571428571429,700.0,701.0,52.881718868397144,701.0,600.0,Frequency", lines.get(2));

    }

    @Test
    public void testProcessSimpleMultiThread() throws IOException {
        File exportFile = new File(TestConst.OutputPath, RandomStringUtils.randomAlphabetic(10) + ".txt");
        File logFile = new File("src/test/resources/logfile/ExecuteTimeFilterMulti.log");
        exportFile.createNewFile();
        ExecuteTimeFilterScript script = new ExecuteTimeFilterScript();
        script.setExportFile(exportFile);
        script.setExcepression(TestConst.Log4jFormat);
        script.process(logFile);
        logger.info("result file:" + exportFile.getAbsolutePath());
        List<String> lines = FileUtils.readLines(exportFile);
        Assert.assertEquals(12, lines.size());

        Assert.assertEquals("execute time,601", lines.get(0));
        Assert.assertEquals("Previous log,2013-02-27 20:55:35.693,3,11", lines.get(1));
        Assert.assertEquals("current log,2013-02-27 20:55:36.294,4,11", lines.get(2));

        Assert.assertEquals("execute time,604", lines.get(3));
        Assert.assertEquals("Previous log,2013-02-27 20:55:35.693,3,10", lines.get(4));
        Assert.assertEquals("current log,2013-02-27 20:55:36.297,4,10", lines.get(5));

        Assert.assertEquals("execute time,700", lines.get(6));
        Assert.assertEquals("Previous log,2013-02-27 20:55:36.295,7,11", lines.get(7));
        Assert.assertEquals("current log,2013-02-27 20:55:36.995,8,11", lines.get(8));

        Assert.assertEquals("execute time,701", lines.get(9));
        Assert.assertEquals("Previous log,2013-02-27 20:55:36.297,7,10", lines.get(10));
        Assert.assertEquals("current log,2013-02-27 20:55:36.998,8,10", lines.get(11));
    }

    /**
     * default barrier is 500ms
     * 
     * @throws IOException
     */
    @Test
    public void testchangeBarrier() throws IOException {
        File exportFile = new File(TestConst.OutputPath, RandomStringUtils.randomAlphabetic(10) + ".txt");
        File logFile = new File("src/test/resources/logfile/ExecuteTimeFilter.log");
        exportFile.createNewFile();
        ExecuteTimeFilterScript script = new ExecuteTimeFilterScript();
        script.setExcepression(TestConst.Log4jFormat);
        script.setExportFile(exportFile);
        script.setBarrier(200); // set time to 200
        script.process(logFile);
        logger.info("result file,barrier 200:" + exportFile.getAbsolutePath());
        List<String> lines = FileUtils.readLines(exportFile);
        Assert.assertEquals(15, lines.size());

        Assert.assertEquals("execute time,302", lines.get(0));
        Assert.assertEquals("Previous log,2013-02-26 16:33:27.389,1", lines.get(1));
        Assert.assertEquals("current log,2013-02-26 16:33:27.691,2", lines.get(2));

        Assert.assertEquals("execute time,600", lines.get(3));
        Assert.assertEquals("Previous log,2013-02-26 16:33:27.691,3", lines.get(4));
        Assert.assertEquals("current log,2013-02-26 16:33:28.291,4", lines.get(5));

        Assert.assertEquals("execute time,700", lines.get(6));
        Assert.assertEquals("Previous log,2013-02-26 16:33:28.292,7", lines.get(7));
        Assert.assertEquals("current log,2013-02-26 16:33:28.992,8", lines.get(8));

        Assert.assertEquals("execute time,1000", lines.get(9));
        Assert.assertEquals("Previous log,2013-02-26 16:33:28.993,12", lines.get(10));
        Assert.assertEquals("current log,2013-02-26 16:33:29.993,13", lines.get(11));

        Assert.assertEquals("execute time,900", lines.get(12));
        Assert.assertEquals("Previous log,2013-02-26 16:33:29.993,13", lines.get(13));
        Assert.assertEquals("current log,2013-02-26 16:33:30.893,14", lines.get(14));

        exportFile = new File(TestConst.OutputPath, RandomStringUtils.randomAlphabetic(10) + ".txt");
        exportFile.createNewFile();
        logger.info("result file,barrier 699:" + exportFile.getAbsolutePath());
        script.setBarrier(699); // set time to 200
        script.setExportFile(exportFile);
        script.process(logFile);
        lines = FileUtils.readLines(exportFile);
        Assert.assertEquals(9, lines.size());

        Assert.assertEquals("execute time,700", lines.get(0));
        Assert.assertEquals("Previous log,2013-02-26 16:33:28.292,7", lines.get(1));
        Assert.assertEquals("current log,2013-02-26 16:33:28.992,8", lines.get(2));

        Assert.assertEquals("execute time,1000", lines.get(3));
        Assert.assertEquals("Previous log,2013-02-26 16:33:28.993,12", lines.get(4));
        Assert.assertEquals("current log,2013-02-26 16:33:29.993,13", lines.get(5));

        Assert.assertEquals("execute time,900", lines.get(6));
        Assert.assertEquals("Previous log,2013-02-26 16:33:29.993,13", lines.get(7));
        Assert.assertEquals("current log,2013-02-26 16:33:30.893,14", lines.get(8));
    }
}
