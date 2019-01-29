public class FolderFSManager implements IFSManager {
    

    public FolderFSManager(File fIn) throws IOException {
        if (!fIn.isDirrectory())

    @Override
    public Reader getFile(byte[] in) {
        if (in.length)
