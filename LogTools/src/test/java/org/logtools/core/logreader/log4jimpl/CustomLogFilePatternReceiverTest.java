package org.logtools.core.logreader.log4jimpl;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Test;

public class CustomLogFilePatternReceiverTest {

    private static Logger logger = Logger
            .getLogger(CustomLogFilePatternReceiverTest.class);

    @Test
    public void testTranslateExpression() {

        CustomLogFilePatternReceiver target = new CustomLogFilePatternReceiver();
        CustomLogFilePatternReceiver targetwithoutformat = new CustomLogFilePatternReceiver();

        String testExpress = "%t %-d{yyyy-MM-dd HH:mm:ss.SSS} %5p %c:%L - %m%n";
        String testExpresswithoutFormate = "%t %-d %5p %c:%L - %m%n";
        String expectExpress = "THREAD TIMESTAMP LEVEL LOGGER:LINE - MESSAGE";
        String expectTimeStampFormat = "yyyy-MM-dd HH:mm:ss.SSS";

        testExpress = target.translateExpression(testExpress);
        logger.info("testExpress result:" + testExpress);
        Assert.assertEquals(expectExpress,
                testExpress);
        Assert.assertEquals(expectTimeStampFormat, target.getTimestampFormat());

        testExpresswithoutFormate = targetwithoutformat
                .translateExpression(testExpresswithoutFormate);
        logger.info("testExpresswithoutFormate result:"
                + testExpresswithoutFormate);
        Assert.assertEquals(expectExpress,
                testExpresswithoutFormate);
        Assert.assertEquals("yyyy-MM-d HH:mm:ss,SSS",
                targetwithoutformat.getTimestampFormat());
    }
}
