package com.powerboat9.instrument;

import java.nio.*;
import java.io.*;

public class FolderFSManager implements IFSManager {
    private Path dir;
    private WatchService watcher;
    private WatchKey watchKey;
    
    private Object modLock;

    private HashMap<HashObject, Path> files = new HashMap<>();
    
    public FolderFSManager(Path fIn) throws IOException {
        fIn = fIn.getAbsoluteFile();
        if (!fIn.isDirectory()) throw new IOException("Not a directory");
        if (!fIn.canRead()) throw new IOException("Cannot read directory");
        dir = fIn;
        watcher = dir.getFileSystem().newWatcherServive();
        watchKey = watcher.register(dir, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
    }
    
    public static void rebuild() {
        synchronized (modLock) {
            

    @Override
    public Reader getFile(byte[] in) {
        synchronized (modLock) {
        }
    }
    
    @Override
    public void update() {
        synchronized (modLock) {
            List<WatchEvent<?>> es = watchKey.pollEvents();
            watchKey.reset();
            for (WatchEvent<?> e : es) {
                
}
