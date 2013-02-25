
package org.logtools.core.writer;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.logtools.TestConst;
import org.logtools.core.writer.impl.LogFileWriter;

public class LogFileWriterTest {

    private static Logger logger = Logger.getLogger(LogFileWriterTest.class);

    private Random r = new Random();

    @Test
    public void testWriteOneline() throws IOException {
        File testFile = new File(TestConst.OutputPath, RandomStringUtils.randomAlphabetic(10));
        testFile.createNewFile();
        logger.info("create file:" + testFile.getAbsolutePath());
        LogFileWriter writer = new LogFileWriter(testFile);
        writer.start();
        int linesNumber = r.nextInt(10000) + 1;
        String[] lines = new String[linesNumber];
        String line;

        for (int i = 0; i < linesNumber; i++) {
            line = RandomStringUtils.randomAlphabetic(100);
            writer.writeOneLine(line);
            lines[i] = line;
        }
        writer.close();

        this.verfiyResult(testFile, lines);
    }

    @Test
    public void testFlush() throws IOException {
        File testFile = new File(TestConst.OutputPath, RandomStringUtils.randomAlphabetic(10));
        testFile.createNewFile();
        logger.info("create file:" + testFile.getAbsolutePath());
        LogFileWriter writer = new LogFileWriter(testFile);
        writer.start();
        int linesNumber = r.nextInt(10000) + 1;
        String[] lines = new String[linesNumber];
        String line;

        for (int i = 0; i < linesNumber; i++) {
            line = RandomStringUtils.randomAlphabetic(100);
            writer.writeOneLine(line);
            lines[i] = line;
        }
        writer.flush();
        this.verfiyResult(testFile, lines);
        writer.close();
    }

    private void verfiyResult(File testFile, String[] lines) throws IOException {

        List<String> linesInFIle = FileUtils.readLines(testFile);

        Assert.assertEquals(lines.length, linesInFIle.size());
        int lineId = 0;

        Iterator<String> iter = linesInFIle.iterator();

        while (iter.hasNext()) {
            Assert.assertEquals(iter.next(), lines[lineId]);
            lineId++;
        }

    }
}
