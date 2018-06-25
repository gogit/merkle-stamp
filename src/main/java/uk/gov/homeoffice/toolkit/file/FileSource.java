package uk.gov.homeoffice.toolkit.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/**
 *
 */
public class FileSource {

    /**
     * Files listed in source path
     */
    private final Map<String, List<String>> listedFiles = new LinkedHashMap<>();

    /**
     * Get the files mentioned in the path
     *
     * @param sourcePath
     * @return
     * @throws IOException
     */
    public void walk(final String sourcePath) throws IOException {
        File dir = new File( sourcePath );
        String[] fileNames = dir.list();
        File[] files = dir.listFiles();
        if (files == null || files.length==0) return;
        listedFiles.put(sourcePath, Arrays.asList(fileNames));
        for ( File f : files ) {
            if ( !Files.isSymbolicLink(f.toPath()) && f.isDirectory() ) {
                walk( f.getAbsolutePath() );
            }
        }
    }

    public Map<String, List<String>> getListedFiles() {
        return listedFiles;
    }
}
