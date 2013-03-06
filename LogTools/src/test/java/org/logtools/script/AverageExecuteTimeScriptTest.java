
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
import org.logtools.script.object.LogRange;

public class AverageExecuteTimeScriptTest {

    private static Logger logger = Logger.getLogger(AverageExecuteTimeScriptTest.class);

    @Test
    public void testMainFlow() throws IOException {
        File exportFile = new File(TestConst.OutputPath, RandomStringUtils.randomAlphabetic(10) + ".txt");
        File logFile = new File("src/test/resources/logfile/AverageExecuteTime.log");
        exportFile.createNewFile();
        AverageExecuteTimeScript script = new AverageExecuteTimeScript();
        script.setExportFile(exportFile);
        script.setExcepression(TestConst.Log4jFormat);
        LogRange range = new LogRange("start", "end");
        script.setLogRange(range);

        script.process(logFile);

        logger.info("result file:" + exportFile.getAbsolutePath());

        List<String> lines = FileUtils.readLines(exportFile);
        Assert.assertEquals(19, lines.size());

        Assert.assertEquals("execute time,328", lines.get(0));
        Assert.assertEquals("start log,Thread-0,2013-03-04 22:39:55.029,start", lines.get(1));
        Assert.assertEquals("end log,Thread-0,2013-03-04 22:39:55.357,end", lines.get(2));

        Assert.assertEquals("execute time,328", lines.get(3));
        Assert.assertEquals("start log,Thread-1,2013-03-04 22:39:55.029,start", lines.get(4));
        Assert.assertEquals("end log,Thread-1,2013-03-04 22:39:55.357,end", lines.get(5));

        Assert.assertEquals("execute time,608", lines.get(6));
        Assert.assertEquals("start log,Thread-1,2013-03-04 22:39:55.357,start", lines.get(7));
        Assert.assertEquals("end log,Thread-1,2013-03-04 22:39:55.965,end", lines.get(8));

        Assert.assertEquals("missing end", lines.get(9));
        Assert.assertEquals("start log,Thread-0,2013-03-04 22:39:55.357,start", lines.get(10));

        Assert.assertEquals("execute time,703", lines.get(11));
        Assert.assertEquals("start log,Thread-0,2013-03-04 22:39:55.965,start", lines.get(12));
        Assert.assertEquals("end log,Thread-0,2013-03-04 22:39:56.668,end", lines.get(13));

        Assert.assertEquals("execute time,703", lines.get(14));
        Assert.assertEquals("start log,Thread-1,2013-03-04 22:39:55.965,start", lines.get(15));
        Assert.assertEquals("end log,Thread-1,2013-03-04 22:39:56.668,end", lines.get(16));

        Assert.assertEquals("missing start", lines.get(17));
        Assert.assertEquals("end log,Thread-1,2013-03-04 22:39:56.668,end", lines.get(18));
    }

    @Test
    public void testProcessSummary() throws IOException {
        String randomFileName = RandomStringUtils.randomAlphabetic(10);
        File exportFile = new File(TestConst.OutputPath, randomFileName + ".txt");
        File logFile = new File("src/test/resources/logfile/AverageExecuteTime.log");
        exportFile.createNewFile();

        AverageExecuteTimeScript script = new AverageExecuteTimeScript();
        script.setExportFile(exportFile);
        script.setExcepression(TestConst.Log4jFormat);

        LogRange range = new LogRange("start", "end");
        script.setLogRange(range);

        script.process(logFile);

        File exportSummaryFile = new File(TestConst.OutputPath, randomFileName + "Summary.csv");
        Assert.assertTrue(exportSummaryFile.exists());

        logger.info("result file:" + exportFile.getAbsolutePath());
        List<String> lines = FileUtils.readLines(exportSummaryFile);

        Assert.assertEquals(2, lines.size());
        Assert.assertEquals("frequency,mean,mid,90% value,sd,max,min,missing start,missing end,message", lines.get(0));
        Assert.assertEquals("5,534.0,608.0,703.0,192.00911436700082,703.0,328.0,1,1,start-end", lines.get(1));

    }

    @Test
    public void testRegularExpression() throws IOException {
        File exportFile = new File(TestConst.OutputPath, RandomStringUtils.randomAlphabetic(10) + ".txt");
        File logFile = new File("src/test/resources/logfile/AverageExecuteTimeRegularExpression.log");
        exportFile.createNewFile();
        AverageExecuteTimeScript script = new AverageExecuteTimeScript();
        script.setExportFile(exportFile);
        script.setExcepression(TestConst.Log4jFormat);
        LogRange range = new LogRange("start,\\d{1,2}", "end,\\d{1,2}");
        script.setLogRange(range);
        script.process(logFile);

        logger.info("result file:" + exportFile.getAbsolutePath());

        List<String> lines = FileUtils.readLines(exportFile);
        Assert.assertEquals(19, lines.size());

        Assert.assertEquals("execute time,304", lines.get(0));
        Assert.assertEquals("start log,Thread-0,2013-03-04 22:26:31.700,start,10", lines.get(1));
        Assert.assertEquals("end log,Thread-0,2013-03-04 22:26:32.004,end,10", lines.get(2));

        Assert.assertEquals("execute time,308", lines.get(3));
        Assert.assertEquals("start log,Thread-1,2013-03-04 22:26:31.700,start,11", lines.get(4));
        Assert.assertEquals("end log,Thread-1,2013-03-04 22:26:32.008,end,11", lines.get(5));

        Assert.assertEquals("execute time,603", lines.get(6));
        Assert.assertEquals("start log,Thread-1,2013-03-04 22:26:32.010,start,11", lines.get(7));
        Assert.assertEquals("end log,Thread-1,2013-03-04 22:26:32.613,end,11", lines.get(8));

        Assert.assertEquals("missing end", lines.get(9));
        Assert.assertEquals("start log,Thread-0,2013-03-04 22:26:32.008,start,10", lines.get(10));

        Assert.assertEquals("execute time,704", lines.get(11));
        Assert.assertEquals("start log,Thread-0,2013-03-04 22:26:32.610,start,10", lines.get(12));
        Assert.assertEquals("end log,Thread-0,2013-03-04 22:26:33.314,end,10", lines.get(13));

        Assert.assertEquals("execute time,702", lines.get(14));
        Assert.assertEquals("start log,Thread-1,2013-03-04 22:26:32.613,start,11", lines.get(15));
        Assert.assertEquals("end log,Thread-1,2013-03-04 22:26:33.315,end,11", lines.get(16));

        Assert.assertEquals("missing start", lines.get(17));
        Assert.assertEquals("end log,Thread-1,2013-03-04 22:26:33.316,end,11", lines.get(18));
    }
}
