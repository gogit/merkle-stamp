package uk.gov.homeoffice.toolkit.merkleStamp;

import uk.gov.homeoffice.toolkit.concurrent.TaskExecutor;
import uk.gov.homeoffice.toolkit.file.FileHandler;
import uk.gov.homeoffice.toolkit.file.FileHash;
import uk.gov.homeoffice.toolkit.file.FileSource;
import uk.gov.homeoffice.toolkit.hash.BouncyCastleHashFunction;
import uk.gov.homeoffice.toolkit.merkle.HashDigest;
import uk.gov.homeoffice.toolkit.hash.HashFunction;
import uk.gov.homeoffice.toolkit.merkle.MerkleTree;
import uk.gov.homeoffice.toolkit.merkle.Node;
import uk.gov.homeoffice.toolkit.util.BenchMark;

import java.io.File;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.stream.Collectors;

public class FileStamp implements Stamp<String> {

    String rootDirectory;

    HashFunction<ByteBuffer, String> hashFunction;

    int noLeaves;

    public FileStamp(final String rootDirectory) {
        this.rootDirectory = rootDirectory;
        File dir = new File(rootDirectory);
        if (!dir.exists()) {
            throw new IllegalArgumentException("Directory does not exist " + rootDirectory);
        }
        if (!dir.canRead()) {
            throw new IllegalArgumentException("Directory cannot be read " + rootDirectory);
        }
    }

    public HashFunction<ByteBuffer, String> getHashFunction() {
        return hashFunction;
    }

    public void setHashFunction(HashFunction<ByteBuffer, String> hashFunction) {
        this.hashFunction = hashFunction;
    }

    @Override
    public String stamp() throws Exception {
        FileSource fileSource = new FileSource();
        fileSource.walk(rootDirectory);
        MerkleTree<ByteBuffer, String> merkleTree = new MerkleTree(hashFunction);
        new TaskExecutor<ByteBuffer>().execute(fileSource.getListOfFiles().stream().map(
                f -> new FileHash(new File(f))).collect(Collectors.toList()), new FileHandler(merkleTree));
        Node<ByteBuffer, String> node = merkleTree.buildTree();
        noLeaves = node.getLeafCount();
        return node.getHash();
    }

    @Override
    public int getLeafCount() {
        return noLeaves;
    }

    public static void main(String args[]) throws Exception {
        if (args == null || args.length != 2) {
            System.out.println("Pass in the directory to hash and the hash type (SHA1|SHA224|SHA256)");
            System.exit(0);
        }
        BenchMark ben = new BenchMark();
        final FileStamp fileStamp = new FileStamp(args[0]);
        Duration duration = ben.execute((Void) -> {
            try {
                switch (args[1]) {
                    case "SHA1":
                        fileStamp.setHashFunction(new BouncyCastleHashFunction(HashDigest.SHA1));
                        break;
                    case "SHA224":
                        fileStamp.setHashFunction(new BouncyCastleHashFunction(HashDigest.SHA224));
                        break;
                    case "SHA256":
                        fileStamp.setHashFunction(new BouncyCastleHashFunction(HashDigest.SHA256));
                        break;
                }
                System.out.println("File Stamp      -> " + fileStamp.stamp());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        System.out.println("Hash used       -> " + fileStamp.getHashFunction().nameOfHash());
        System.out.println("Files processed -> " + fileStamp.getLeafCount());
        System.out.println("Time taken secs -> " + duration.getSeconds());
    }
}
