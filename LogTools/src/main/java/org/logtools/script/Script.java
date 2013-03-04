package org.logtools.script;

import java.io.File;

public interface Script {

    void process(File log);

    void process(File[] logs);
}
