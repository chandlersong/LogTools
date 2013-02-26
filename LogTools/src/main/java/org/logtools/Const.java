
package org.logtools;

import java.io.File;

public class Const {

    public static final String NEW_LINE = System.getProperty("line.separator");

    public static final File DEFALT_REPORT_FOLDER = new File("report");

    static {
        if (!DEFALT_REPORT_FOLDER.exists()) {
            DEFALT_REPORT_FOLDER.mkdirs();
        }
    }
}
