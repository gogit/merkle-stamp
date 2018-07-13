package uk.gov.homeoffice.toolkit.merkleStamp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import uk.gov.homeoffice.toolkit.hash.BouncyCastleHashFunction;
import uk.gov.homeoffice.toolkit.merkle.HashDigest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class FileStampTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private static final String FILE1 = "file1";

    @Before
    public void testInTempFolder() throws IOException {
        tempFolder.newFile(FILE1);
    }

    @Test
    public void test() throws Exception {
        FileStamp fileStamp = new FileStamp(tempFolder.getRoot().getAbsolutePath());
        fileStamp.setHashFunction(new BouncyCastleHashFunction(HashDigest.SHA1));
        Files.write(new File(tempFolder.getRoot().getAbsolutePath(), FILE1).toPath(),
                new byte[]{0}, StandardOpenOption.APPEND);
        String stamp1 = fileStamp.stamp();

        Files.write(new File(tempFolder.getRoot().getAbsolutePath(), FILE1).toPath(),
                new byte[]{0}, StandardOpenOption.CREATE);
        String stamp2 = fileStamp.stamp();

        Assert.assertEquals(stamp1, stamp2);

        Files.write(new File(tempFolder.getRoot().getAbsolutePath(), FILE1).toPath(),
                new byte[]{1}, StandardOpenOption.CREATE);
        String stamp3 = fileStamp.stamp();
        Assert.assertNotEquals(stamp2, stamp3);

        Files.write(new File(tempFolder.getRoot().getAbsolutePath(), FILE1).toPath(),
                new byte[]{0}, StandardOpenOption.CREATE);
        String stamp4 = fileStamp.stamp();
        Assert.assertEquals(stamp2, stamp4);

        Files.write(new File(tempFolder.getRoot().getAbsolutePath(), FILE1).toPath(),
                new byte[]{0}, StandardOpenOption.APPEND);
        String stamp5 = fileStamp.stamp();
        Assert.assertNotEquals(stamp4, stamp5);

    }
}
