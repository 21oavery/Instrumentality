package com.powerboat9.instrument;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.*;

public class FolderFSManager implements IFSManager {
    private Path dir;
    private WatchService watcher;
    private WatchKey watchKey;
    
    private Object modLock = new Object();

    private HashMap<HashObject, Path> files = new HashMap<>();
    
    public FolderFSManager(Path fIn) throws IOException {
        if (!fIn.isAbsolute()) fIn = fIn.toAbsolutePath();
        System.out.println(fIn);
        if (!Files.isDirectory(fIn)) throw new IOException("Not a directory");
        if (!Files.isReadable(fIn)) throw new IOException("Cannot read directory");
        dir = fIn;
        watcher = dir.getFileSystem().newWatchService();
        watchKey = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        rebuild();
    }

    private void rebuildUnsafe() throws IOException {
        rebuildUnsafe(dir);
    }
    
    private void rebuild() throws IOException {
        rebuild(dir);
    }

    private void rebuildUnsafe(Path f) throws IOException {
        if (f.isAbsolute()) {
            if (!f.startsWith(dir)) return;
        } else {
            f = f.resolve(dir);
        }
        for (HashObject entry : files.keySet()) {
            if (files.get(entry).equals(f)) {
                files.remove(entry);
                break;
            }
        }
        if (Files.isDirectory(f)) {
            for (Path p : (Iterable<? extends Path>) Files.walk(f)::iterator) {
                if (p != f) rebuildUnsafe(p);
            }
            return;
        }
        HashObject newHash = HashObject.hash(f);
        files.put(newHash, f);
    }

    private void rebuild(Path f) throws IOException {
        synchronized (modLock) {
            rebuildUnsafe(f);
        }
    }

    @Override
    public Reader getFile(HashObject hash) {
        try {
            return Files.newBufferedReader(files.get(hash));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean publish(Object o) {
        return false;
    }

    @Override
    public boolean unpublish(HashObject hash) {
        if (files.containsKey(hash)) {
            try {
                Files.deleteIfExists(files.get(hash));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean update() {
        synchronized (modLock) {
            List<WatchEvent<?>> es = watchKey.pollEvents();
            watchKey.reset();
            for (WatchEvent<?> e : es) {
                if (e.kind() == OVERFLOW) {
                    try {
                        rebuildUnsafe();
                        return true;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        return false;
                    }
                }
            }
            HashSet<Path> done = new HashSet<>();
            for (WatchEvent<?> e : es) {
                if ((e.kind() == ENTRY_CREATE) || (e.kind() == ENTRY_DELETE) || (e.kind() == ENTRY_MODIFY)) {
                    if (!done.contains(e.context())) done.add((Path) e.context());
                }
            }
            for (Path p : done) {
                try {
                    rebuildUnsafe(p);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return true;
    }
}