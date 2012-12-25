package org.logtools.script;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * add some common support to script, like spring support
 * @author Chandler.Song
 *
 */
public abstract class CommonScript {

    private ApplicationContext context;
    
    public CommonScript(){
       this.inital();
    }
    
    public void inital(){
        context =
            new ClassPathXmlApplicationContext(this.getConfigureList());
    }
    
    protected final String[] getConfigureList(){     

        List<String> filelist = this.addConfigfile(new ArrayList<String>());
        
        return filelist.toArray(new String[0]);
    }

    public ApplicationContext getContext() {
        return context;
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }
    
    /**
     * the sub class should add the spring file it need to for the script
     * @param filelist
     * @return
     */
    protected abstract List<String> addConfigfile(List<String> filelist);
}

