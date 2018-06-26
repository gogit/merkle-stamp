package uk.gov.homeoffice.toolkit.merkleStamp;

import uk.gov.homeoffice.toolkit.concurrent.Handler;
import uk.gov.homeoffice.toolkit.concurrent.TaskExecutor;
import uk.gov.homeoffice.toolkit.file.FileHandler;
import uk.gov.homeoffice.toolkit.file.FileHash;
import uk.gov.homeoffice.toolkit.file.FileSource;
import uk.gov.homeoffice.toolkit.merkle.HashFunction;
import uk.gov.homeoffice.toolkit.merkle.MerkleTree;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class FileStamp implements Stamp<String>{

    String rootDirectory;

    final HashFunction<ByteBuffer, String> hashFunction;

    public FileStamp(final String rootDirectory, final HashFunction<ByteBuffer, String> hashFunction){
        this.rootDirectory = rootDirectory;
        this.hashFunction = hashFunction;
        File dir = new File(rootDirectory);
        if(!dir.exists()){
            throw new IllegalArgumentException("Directory does not exist "+ rootDirectory);
        }
        if(!dir.canRead()){
            throw new IllegalArgumentException("Directory cannot be read "+ rootDirectory);
        }
    }

    @Override
    public String stamp()throws Exception {
        FileSource fileSource = new FileSource();
        fileSource.walk(rootDirectory);
        List<String> listOfFiles = fileSource.getListOfFiles();
        MerkleTree<ByteBuffer, String> merkleTree = new MerkleTree(hashFunction);
        FileHandler handler = new FileHandler(merkleTree);
        List<Callable<ByteBuffer>> l = listOfFiles.stream().map(f->new FileHash(new File(f))).collect(Collectors.toList());
        new TaskExecutor<ByteBuffer>().execute(l, handler);
        return merkleTree.buildTree().getHash();
    }
}
