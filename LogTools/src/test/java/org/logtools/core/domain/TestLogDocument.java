package org.logtools.core.domain;

import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import junit.framework.Assert;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.logtools.core.domain.log4jimpl.Log4jLogEntry;

/**
 * Test Log4jLogDocument
 * 
 * 
 * @author Chandler.Song
 * 
 */
public class TestLogDocument {

    private Random r;

    private CommonDocument logDocument;

    private LogEntry[] logs;

    @Before
    public void initial() {

        logDocument = new CommonDocument();
        logs = new LogEntry[100];
        r = new Random();

        /* Perpare Test Data */
        LogEntry log;
        for (int i = 0; i < 100; i++) {
            log = new Log4jLogEntry(r.nextInt(),
                    RandomStringUtils.randomAlphabetic(10),
                    new Date(r.nextLong()),
                    RandomStringUtils.randomAlphabetic(10),
                    RandomStringUtils.randomAlphabetic(10),
                    RandomStringUtils.randomAlphabetic(10));
            logs[i] = log;
            logDocument.addLine(log);
        }
    }

    @Test
    public void TestCommon() {

        Assert.assertEquals(logDocument.size().intValue(), 100);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void TestIterator() {

        Iterator<LogEntry> iter = logDocument.iterator();

        int i = 0;

        while (iter.hasNext()) {
            LogEntry le = iter.next();
            Assert.assertTrue(le.equals(logs[i]));
            i++;
        }

        iter.remove();
    }

    /**
     * this test case doesn't cover duplicate log which will be added in the
     * future
     */
    @Test
    public void TestFindLog() {

        int index = r.nextInt(100);
        Assert.assertEquals(logs[index], logDocument.findLog(index));

    }
}
