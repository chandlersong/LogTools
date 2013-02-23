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
        Assert.assertEquals(DateUtils.parseDate("2013-02-18 23:29:26.125",
                "yyyy-MM-dd HH:mm:ss.SSS"), firstEntry.getTime());

        LogEntry secondEntry = entrylist.get(1);
        Assert.assertEquals("org.logtools.utils.LogGenerator",
                secondEntry.getCatalog());
        Assert.assertEquals("error log", secondEntry.getMessage());
        Assert.assertEquals("ERROR", secondEntry.getLevel());
        Assert.assertEquals("main", secondEntry.getThreadInfo());
        Assert.assertEquals(DateUtils.parseDate("2013-02-18 23:29:26.130",
                "yyyy-MM-dd HH:mm:ss.SSS"), secondEntry.getTime());

    }
}
