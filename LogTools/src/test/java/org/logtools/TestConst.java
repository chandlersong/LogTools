
package org.logtools;

import java.io.File;

import org.apache.commons.io.FileUtils;

public class TestConst {

    public static final String Log4jFormat = "%t %-d{yyyy-MM-dd HH:mm:ss.SSS} %5p %c:%L - %m%n";

    public static final File OutputPath = new File(FileUtils.getTempDirectory() + "\\LogTools");

    static {
        if (!OutputPath.exists()) {
            OutputPath.mkdirs();
        }
    }
}
