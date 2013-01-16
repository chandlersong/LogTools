package org.logtools.utils;

import org.apache.log4j.Logger;
import org.junit.Test;


public class LogGenerator {

    private static Logger logger = Logger.getLogger(LogGenerator.class);
    
    @Test
    public void generateSimplelog(){
        logger.info("info log");
        logger.trace("trace log");
        logger.error("error log");
    }
    
    @Test
    public void generateSimpleERRORlog(){
        logger.info("info log");
        logger.trace("trace log");
        logger.error("error log",new IllegalArgumentException());
    }
}
