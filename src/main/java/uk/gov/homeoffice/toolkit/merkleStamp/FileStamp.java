package uk.gov.homeoffice.toolkit.merkleStamp;

import uk.gov.homeoffice.toolkit.concurrent.TaskExecutor;
import uk.gov.homeoffice.toolkit.file.FileHandler;
import uk.gov.homeoffice.toolkit.file.FileHash;
import uk.gov.homeoffice.toolkit.file.FileSource;
import uk.gov.homeoffice.toolkit.hash.BouncyCastleHashFunction;
import uk.gov.homeoffice.toolkit.merkle.HashDigest;
import uk.gov.homeoffice.toolkit.hash.HashFunction;
import uk.gov.homeoffice.toolkit.merkle.MerkleTree;
import uk.gov.homeoffice.toolkit.util.BenchMark;

import java.io.File;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.stream.Collectors;

public class FileStamp implements Stamp<String> {

    String rootDirectory;

    final HashFunction<ByteBuffer, String> hashFunction;

    public FileStamp(final String rootDirectory, final HashFunction<ByteBuffer, String> hashFunction) {
        this.rootDirectory = rootDirectory;
        this.hashFunction = hashFunction;
        File dir = new File(rootDirectory);
        if (!dir.exists()) {
            throw new IllegalArgumentException("Directory does not exist " + rootDirectory);
        }
        if (!dir.canRead()) {
            throw new IllegalArgumentException("Directory cannot be read " + rootDirectory);
        }
    }

    @Override
    public String stamp() throws Exception {
        FileSource fileSource = new FileSource();
        fileSource.walk(rootDirectory);
        MerkleTree<ByteBuffer, String> merkleTree = new MerkleTree(hashFunction);
        new TaskExecutor<ByteBuffer>().execute(fileSource.getListOfFiles().stream().map(
                f -> new FileHash(new File(f))).collect(Collectors.toList()), new FileHandler(merkleTree));
        return merkleTree.buildTree().getHash();
    }

    public static void main(String args[]) throws Exception {
        BenchMark ben = new BenchMark();
        Duration duration = ben.execute((Void) -> {
            try {
                System.out.println(new FileStamp("/home/pt", new BouncyCastleHashFunction(HashDigest.SHA1)).stamp());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        System.out.println(duration.getSeconds());

    }
}
