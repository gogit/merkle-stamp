package uk.gov.homeoffice.toolkit.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

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
        listedFiles.put(sourcePath, Arrays.stream(files).map(f->f.getAbsolutePath()).collect(Collectors.toList()));
        for ( File f : files ) {
            if ( !Files.isSymbolicLink(f.toPath()) && f.isDirectory() ) {
                walk( f.getAbsolutePath() );
            }
        }
    }

    public Map<String, List<String>> getMapOfFiles() {
        return listedFiles;
    }

    public List<String> getListOfFiles() {
        return listedFiles.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }
}
