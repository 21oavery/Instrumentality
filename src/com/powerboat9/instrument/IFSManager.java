package com.powerboat9.instrument;

import java.io.Reader;

public interface IFSManager {
    Reader getFile(HashObject hash);
    
    boolean publish(Object o);
    
    boolean unpublish(HashObject hash);
    
    boolean update();
}
