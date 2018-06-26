package uk.gov.homeoffice.toolkit.file;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.concurrent.Callable;

public class FileHash implements Callable<ByteBuffer> {

    private final File file;

    private final ByteBuffer ZERO_LENGTH = ByteBuffer.wrap(new byte[]{0});

    private final ByteBuffer TOO_BIG = ByteBuffer.wrap(new byte[]{1});

    private final ByteBuffer DIR = ByteBuffer.wrap(new byte[]{2});

    private final ByteBuffer NOT_EXISTS = ByteBuffer.wrap(new byte[]{3});

    public FileHash(final File file) {
        this.file = file;
    }

    @Override
    public ByteBuffer call() throws Exception {
        long len = file.length();
        //TODO alternative approach
        if (len <= 0) {
            return ZERO_LENGTH;
        }
        if (len > 1024000000) {
            return TOO_BIG;
        }
        if(file.isDirectory()){
            return DIR;
        }
        if(!file.canRead()){
            return NOT_EXISTS;
        }
        return ByteBuffer.wrap(calculateHash(Files.readAllBytes(file.toPath()), new SHA1Digest()));
    }

    private byte[] calculateHash(byte[] input, Digest digest) {
        byte[] retValue = new byte[digest.getDigestSize()];
        digest.update(input, 0, input.length);
        digest.doFinal(retValue, 0);
        return retValue;
    }
}
