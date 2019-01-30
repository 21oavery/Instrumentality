package com.powerboat9.instrument;

import java.io.Reader;

public interface IFSManager {
    Reader getFile(byte[] in);
    
    boolean publish(Object o);
    
    boolean unpublish(byte[] hash);
    
    void update();
}
