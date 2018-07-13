package uk.gov.homeoffice.toolkit.hash;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.GeneralDigest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA224Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;
import uk.gov.homeoffice.toolkit.merkle.HashDigest;

import java.nio.ByteBuffer;

public class BouncyCastleHashFunction implements HashFunction<ByteBuffer, String> {

    GeneralDigest generalDigest;

    String hashName;

    public BouncyCastleHashFunction(final HashDigest digest) {
        switch (digest) {
            case SHA1:
                generalDigest = new SHA1Digest();
                hashName = "SHA1";
                break;
            case SHA224:
                generalDigest = new SHA224Digest();
                hashName = "SHA224";
                break;
            case SHA256:
                generalDigest = new SHA256Digest();
                hashName = "SHA256";
                break;
            default:
                hashName = "SHA1";
                generalDigest = new SHA1Digest();
        }
    }

    @Override
    public String hashOfItem(ByteBuffer item) {
        return Hex.toHexString(calculateHash(item.array(), generalDigest));
    }

    @Override
    public String hashOfItem(ByteBuffer left, ByteBuffer right) {
        return Hex.toHexString(calculateHash(Arrays.concatenate(left.array(), right.array()), generalDigest));
    }

    @Override
    public String hashOfHash(String left, String right) {
        return Hex.toHexString(calculateHash(Arrays.concatenate(handleNull(left), handleNull(right)), generalDigest));
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

    @Override
    public String nameOfHash() {
        return hashName;
    }
}
