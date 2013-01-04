package org.logtools.core.logreader.log4jimpl;

import org.apache.log4j.varia.LogFilePatternReceiver;

/**
 * 
 * Notes:
 * 1, after reading LogFilePatternReceiver, I found I need override process and post method <br>
 *    if I ONlY want to convert logEvent to logEntry, I don't need to override process <br>
 *    but if I need to get more information,like log is which line in file, I need override the process <br>
 * @author Chandler.Song
 *
 */
public class CustomLogFilePatternReceiver extends LogFilePatternReceiver {

}
