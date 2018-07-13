package uk.gov.homeoffice.toolkit.merkleStamp;

public interface Stamp<H> {

    /**
     * Stamp assets
     *
     * @return
     * @throws Exception
     */
    H stamp()throws Exception;

    /**
     *
     * @return
     */
    int getLeafCount();
}
