package uk.gov.homeoffice.toolkit.merkleStamp;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import uk.gov.homeoffice.toolkit.merkle.HashFunction;

import java.io.IOException;
import java.nio.ByteBuffer;

public class FileStampTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Before
    public void testInTempFolder() throws IOException {
        tempFolder.newFile("file1.txt");
    }

    @Test
    public void test() throws Exception {
        FileStamp fileStamp = new FileStamp(tempFolder.getRoot().getAbsolutePath(), hashFunction());
        System.out.println(fileStamp.stamp());
    }

    private HashFunction<ByteBuffer, String> hashFunction() {
        return new HashFunction<ByteBuffer, String>() {
            @Override
            public String hashOfItem(ByteBuffer item) {
                return Hex.toHexString(calculateHash(item.array(), new SHA1Digest()));
            }

            @Override
            public String hashOfItem(ByteBuffer left, ByteBuffer right) {
                return Hex.toHexString(calculateHash(Arrays.concatenate(left.array(), right.array()), new SHA1Digest()));
            }

            @Override
            public String hashOfHash(String left, String right) {
                return Hex.toHexString(calculateHash(Arrays.concatenate(handleNull(left), handleNull(right)), new SHA1Digest()));
            }

            private byte[] handleNull(String rec) {
                return rec == null ? new byte[0] : rec.getBytes();
            }

            private byte[] calculateHash(byte[] input, Digest digest) {
                byte[] retValue = new byte[digest.getDigestSize()];
                digest.update(input, 0, input.length);
                digest.doFinal(retValue, 0);
                return retValue;
            }
        };
    }
}
