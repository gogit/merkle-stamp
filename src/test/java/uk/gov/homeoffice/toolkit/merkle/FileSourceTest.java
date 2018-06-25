package uk.gov.homeoffice.toolkit.merkle;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import uk.gov.homeoffice.toolkit.file.FileSource;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FileSourceTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Before
    public void testInTempFolder() throws IOException {
        tempFolder.newFile("file1.txt");
    }

    @Test
    public void test()throws Exception {
        FileSource fileSource = new FileSource();
        fileSource.walk(tempFolder.getRoot().getPath());
        Map<String, List<String>> listed = fileSource.getListedFiles();
        Assert.assertEquals(1, listed.size());
        Assert.assertEquals(1, listed.get(tempFolder.getRoot().getAbsolutePath()).size());
        Assert.assertEquals("file1.txt", listed.get(tempFolder.getRoot().getAbsolutePath()).get(0));
    }
}
