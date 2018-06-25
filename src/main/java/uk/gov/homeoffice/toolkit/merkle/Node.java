package uk.gov.homeoffice.toolkit.merkle;

/**
 * Merkle Tree Node
 *
 * @param <T>
 * @param <H>
 */
public class Node<T, H> {

    private Node<T, H> left;
    private Node<T, H> right;
    private int level;
    private H hash;


    public Node(final Node<T, H> left, final Node<T, H> right, final int level, final H hash) {
        this.left = left;
        this.right = right;
        this.level = level;
        this.hash = hash;
    }

    public Node(final int level, final H hash) {
        this.left = null;
        this.right = null;
        this.level = level;
        this.hash = hash;
    }

    public Node<T, H> getLeft() {
        return left;
    }

    public Node<T, H> getRight() {
        return right;
    }

    public int getLevel() {
        return level;
    }

    public H getHash() {
        return hash;
    }
}
