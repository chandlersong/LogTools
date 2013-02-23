package org.logtools.core.logprocess.plugin;

import org.logtools.core.domain.CommonDocument;
import org.logtools.core.domain.LogDocument;
import org.logtools.core.domain.LogEntry;

/**
 * the plugin will create one LogDocument and add all the log in to it
 * 
 * @author Chandler.Song
 * 
 */
public class GenerateLogDocumentPlugin extends AbsLogPlugin {

    private LogDocument doc;

    public GenerateLogDocumentPlugin() {
        doc = new CommonDocument();
    }

    public GenerateLogDocumentPlugin(LogDocument doc) {
        this.doc = doc;
    }

    public LogDocument getDoc() {
        return doc;
    }

    public void setDoc(LogDocument doc) {
        this.doc = doc;
    }

    @Override
    public void executeAfterPostLogEntry(LogEntry entry) {
        this.doc.addLine(entry);
    }

}
