package org.logtools.utils;

import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.junit.Test;

public class LogGenerator {

    private static Logger logger = Logger.getLogger(LogGenerator.class);

    @Test
    public void generateSimplelog() {
        logger.info("info log");
        logger.trace("trace log");
        logger.error("error log");
    }

    @Test
    public void generateSimpleERRORlog() {
        logger.error("error log", new IllegalArgumentException());
    }

    @Test
    public void generateForExecuteTimeFilter() throws InterruptedException {
        logger.info("1");
        Thread.sleep(300);
        logger.info("2");
        logger.info("3");
        Thread.sleep(600);
        logger.info("4");
        logger.info("5");
        logger.info("6");
        logger.info("7");
        Thread.sleep(700);
        logger.info("8");
        logger.info("9");
        logger.info("10");
        logger.info("11");
        logger.info("12");
        Thread.sleep(1000);
        logger.info("13");
        Thread.sleep(900);
        logger.info("14");
        logger.info("15");
    }

    @Test
    public void generateForExecuteTimeFilterMultThread() throws InterruptedException {

        final CountDownLatch latch = new CountDownLatch(2);
        Thread t1 = new Thread(
                new Runnable() {

                    public void run() {
                        try {
                            logger.info("1," + Thread.currentThread().getId());
                            Thread.sleep(300);
                            logger.info("2," + Thread.currentThread().getId());
                            logger.info("3," + Thread.currentThread().getId());
                            Thread.sleep(600);
                            logger.info("4," + Thread.currentThread().getId());
                            logger.info("5," + Thread.currentThread().getId());
                            logger.info("6," + Thread.currentThread().getId());
                            logger.info("7," + Thread.currentThread().getId());
                            Thread.sleep(700);
                            logger.info("8," + Thread.currentThread().getId());
                            logger.info("9," + Thread.currentThread().getId());
                            logger.info("10," + Thread.currentThread().getId());
                        } catch (InterruptedException e) {
                        }
                        latch.countDown();
                    }

                }
                );

        Thread t2 = new Thread(
                new Runnable() {

                    public void run() {
                        try {
                            logger.info("1," + Thread.currentThread().getId());
                            Thread.sleep(300);
                            logger.info("2," + Thread.currentThread().getId());
                            logger.info("3," + Thread.currentThread().getId());
                            Thread.sleep(600);
                            logger.info("4," + Thread.currentThread().getId());
                            logger.info("5," + Thread.currentThread().getId());
                            logger.info("6," + Thread.currentThread().getId());
                            logger.info("7," + Thread.currentThread().getId());
                            Thread.sleep(700);
                            logger.info("8," + Thread.currentThread().getId());
                            logger.info("9," + Thread.currentThread().getId());
                            logger.info("10," + Thread.currentThread().getId());
                        } catch (InterruptedException e) {
                        }
                        latch.countDown();

                    }

                }
                );

        t1.start();
        t2.start();
        latch.await();

    }

}
