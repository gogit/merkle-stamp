package uk.gov.homeoffice.toolkit.hash;

/**
 * Hash function to be applied
 *
 * @param <T> Type to hash
 * @param <H> Type of hash
 */
public interface HashFunction<T, H> {

    /**
     *
     * @param item
     * @return
     */
    H hashOfItem(T item);

    /**
     * @param left  item
     * @param right item
     * @return
     */
    H hashOfItem(T left, T right);

    /**
     * @param left
     * @param right
     * @return
     */
    H hashOfHash(H left, H right);

}
