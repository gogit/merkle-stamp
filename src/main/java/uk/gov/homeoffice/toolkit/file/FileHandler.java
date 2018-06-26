package uk.gov.homeoffice.toolkit.file;

import uk.gov.homeoffice.toolkit.concurrent.Handler;
import uk.gov.homeoffice.toolkit.merkle.MerkleTree;

import java.nio.ByteBuffer;

public class FileHandler implements Handler<ByteBuffer>{

    final MerkleTree<ByteBuffer, String> merkleTree;

    public FileHandler(final MerkleTree<ByteBuffer, String> merkleTree){
        this.merkleTree = merkleTree;
    }

    @Override
    public void result(ByteBuffer result) {
        merkleTree.push(result);
    }
}
