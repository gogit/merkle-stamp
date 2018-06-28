package uk.gov.homeoffice.toolkit.hash;

/**
 * A test function that echoes input
 */
public class EchoHashFunction implements HashFunction<String, String> {


    @Override
    public String hashOfItem(String left, String right) {
        return emptyIfNull(left) + emptyIfNull(right);
    }

    @Override
    public String hashOfHash(String left, String right) {
        return emptyIfNull(left) + emptyIfNull(right);
    }

    private String emptyIfNull(String data) {
        return data == null ? "" : data;
    }

    @Override
    public String hashOfItem(String item) {
        return item;
    }
}
