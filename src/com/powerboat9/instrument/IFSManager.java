package com.powerboat9.instrument;

public interface IFSManager {
    Reader getFile(byte[] in);
    
    boolean publish(Object o);
    
    boolean unpublish(byte[] hash);
}
