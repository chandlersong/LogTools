package org.logtools.core.logreader.log4jimpl;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.logtools.core.domain.LogDocument;
import org.logtools.core.domain.LogEntry;
import org.logtools.core.logreader.LogReader;

public class TestLogReader {

    private String PATTERN = "%t %-d{yyyy-MM-dd HH:mm:ss.SSS}  %d{ABSOLUTE} %5p %c:%L - %m%n";
    
    private LogReader reader;
    
    @Before
    public void initial(){
        reader = new LogReaderlog4jImpl();
    }
    
    /**
     * Test the simple log reader
     * @throws IOException 
     * @throws ParseException 
     */
    @Test
    public void testSimpleLog() throws IOException, ParseException{
        
        final File simpleLogFile = new File("src/test/resources/logfile/simple.log");
        
        LogDocument logdoc = reader.readLog(simpleLogFile, PATTERN);
        
        Assert.assertEquals(2, logdoc.size().intValue());
        
        List<LogEntry> entrylist = logdoc.listAllEntry();
        
        LogEntry firstEntry = entrylist.get(0);     
        Assert.assertEquals("com.hilatest.log4j.LogGenerator",  firstEntry.getCatalog());
        Assert.assertEquals("info log",  firstEntry.getMessage());
        Assert.assertEquals("INFO",  firstEntry.getLevel());
        Assert.assertEquals("main",  firstEntry.getThreadInfo());
        Assert.assertEquals(DateUtils.parseDate("2013-01-02 15:15:41.810", "yyyy-MM-dd HH:mm:ss.SSS"),  firstEntry.getTime());
        
        LogEntry secondEntry = entrylist.get(0);  
        Assert.assertEquals("com.hilatest.log4j.LogGenerator",  secondEntry.getCatalog());
        Assert.assertEquals("error log",  secondEntry.getMessage());
        Assert.assertEquals("ERROR",  secondEntry.getLevel());
        Assert.assertEquals("main",  secondEntry.getThreadInfo());
        Assert.assertEquals(DateUtils.parseDate("2013-01-02 15:15:41.816", "yyyy-MM-dd HH:mm:ss.SSS"),  secondEntry.getTime());
        
    }
}
