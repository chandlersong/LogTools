package org.logtools.core.logprocess.log4jimpl;

import java.io.File;
import java.text.ParseException;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.logtools.TestConst;
import org.logtools.core.domain.LogDocument;
import org.logtools.core.domain.LogEntry;
import org.logtools.core.logprocess.plugin.GenerateLogDocumentPlugin;

/**
 * LogParser is a abstact class. use it's sub class LogProcesslog4jImpl to test
 * it's function
 * 
 * @author Chandler.Song
 * 
 */
public class Log4JParserProcessTest {

    @Test
    public void testInitial() {

        Log4JParserProcess praser = new Log4JParserProcess(
                TestConst.Log4jFormat);

        Assert.assertEquals(
                "(.*?)[ ]+(\\S+-\\S+-\\S+ \\S+:\\S+:\\S+\\.\\S+)[ ]+(\\S*\\s*?)[ ]+(\\S*\\s*?):(.*?)[ ]+\\-[ ]+(.*)"
                ,
                praser.getRegexp());
        Assert.assertNotNull(praser.getExceptionPattern());
        Assert.assertNotNull(praser.getRegexpPattern());

    }

    @Test
    public void testProcessSimpleLogFile() throws ParseException {
        Log4JParserProcess praser = new Log4JParserProcess(
                TestConst.Log4jFormat);
        GenerateLogDocumentPlugin plugin = new GenerateLogDocumentPlugin();
        praser.addPlugin(plugin);

        final File simpleLogFile = new File(
                "src/test/resources/logfile/simple.log");

        praser.process(simpleLogFile);

        LogDocument logdoc = plugin.getDoc();

        Assert.assertEquals(2, logdoc.size().intValue());

        List<LogEntry> entrylist = logdoc.listAllEntry();

        LogEntry firstEntry = entrylist.get(0);
        Assert.assertEquals("org.logtools.utils.LogGenerator",
                firstEntry.getCatalog());
        Assert.assertEquals("info log", firstEntry.getMessage());
        Assert.assertEquals("INFO", firstEntry.getLevel());
        Assert.assertEquals("main", firstEntry.getThreadInfo());
        Assert.assertEquals("13", firstEntry.getLine());
        Assert.assertEquals(DateUtils.parseDate("2013-02-18 23:29:26.125",
                "yyyy-MM-dd HH:mm:ss.SSS"), firstEntry.getTime());
        Assert.assertEquals("simple.log", firstEntry.getFileName());
        Assert.assertEquals("1", firstEntry.getLineInFile().get(0));

        LogEntry secondEntry = entrylist.get(1);
        Assert.assertEquals("org.logtools.utils.LogGenerator",
                secondEntry.getCatalog());
        Assert.assertEquals("error log", secondEntry.getMessage());
        Assert.assertEquals("ERROR", secondEntry.getLevel());
        Assert.assertEquals("main", secondEntry.getThreadInfo());
        Assert.assertEquals("main", secondEntry.getThreadInfo());
        Assert.assertEquals("15", secondEntry.getLine());
        Assert.assertEquals(DateUtils.parseDate("2013-02-18 23:29:26.130",
                "yyyy-MM-dd HH:mm:ss.SSS"), secondEntry.getTime());
        Assert.assertEquals("simple.log", secondEntry.getFileName());
        Assert.assertEquals("2", secondEntry.getLineInFile().get(0));
    }

    @Test
    public void testProcessSimpleExceptionLogFile() throws ParseException {
        Log4JParserProcess praser = new Log4JParserProcess(
                TestConst.Log4jFormat);
        GenerateLogDocumentPlugin plugin = new GenerateLogDocumentPlugin();
        praser.addPlugin(plugin);

        final File simpleErrorLogFile = new File(
                "src/test/resources/logfile/simpleError.log");

        praser.process(simpleErrorLogFile);

        LogDocument logdoc = plugin.getDoc();
        List<LogEntry> entrylist = logdoc.listAllEntry();
        LogEntry errorEntry = entrylist.get(1);
        Assert.assertEquals(23, errorEntry.getTraceLog().size());
        Assert.assertEquals(25, errorEntry.getLineInFile().size());
        Assert.assertTrue(errorEntry.isException());

        LogEntry normalEntry = entrylist.get(0);
        Assert.assertFalse(normalEntry.isException());
        Assert.assertNull(normalEntry.getTraceLog());
    }
}
