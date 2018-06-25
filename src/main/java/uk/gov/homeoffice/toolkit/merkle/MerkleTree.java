package uk.gov.homeoffice.toolkit.merkle;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * @param <T>
 * @param <H>
 */
public class MerkleTree<T, H> {

    /**
     *
     */
    private HashFunction<T, H> hashFunction;

    /**
     * @param hashFunction
     */
    public MerkleTree(HashFunction<T, H> hashFunction) {
        this.hashFunction = hashFunction;
    }

    /**
     *
     * @param recs
     * @return
     */
    public Node<T, H> buildTree(final List<T> recs) {
        return buildTree(recs.size(), recs.iterator());
    }

    /**
     * @param size
     * @param recs
     * @return
     */
    public Node<T, H> buildTree(final int size, final Iterator<T> recs) {
        if (size <= 1) {
            return new Node<>(1, hashFunction.hashOfItem(recs.next()));
        }

        Stack<Node<T, H>> stackCurrent = new Stack<>();
        Stack<Node<T, H>> stackUp = new Stack<>();

        buildLeafLevel(size, recs, stackCurrent);

        stackPush(stackCurrent, stackUp);
        while (!stackUp.isEmpty()) {
            if (stackUp.size() == 1) {
                return stackUp.pop();
            }
            stackCurrent = stackUp;
            stackUp = new Stack<>();
            stackPush(stackCurrent, stackUp);
        }
        throw new IllegalStateException("Shouldn't be here");
    }

    private void buildLeafLevel(final int size, Iterator<T> recs, final Stack<Node<T, H>> stackCurrent) {
        boolean even = size % 2 == 0;
        for (int i = 0, j = size / 2; i < size / 2; i++, j++) {
            T left = recs.next();
            T right = recs.next();
            stackCurrent.push(getLeafNode(left, right));
        }
        if (!even) {
            stackCurrent.push(getLeafNode(recs.next()));
        }
    }

    private void stackPush(final Stack<Node<T, H>> stackCurrent, final Stack<Node<T, H>> stackUp) {
        while (!stackCurrent.isEmpty()) {
            if (stackCurrent.size() == 1) {
                Node<T, H> single = stackCurrent.pop();
                stackUp.push(getNode(single, null));
                return;
            }
            Node<T, H> left = stackCurrent.pop();
            Node<T, H> right = stackCurrent.pop();
            stackUp.push(getNode(left, right));
        }
    }

    private H hashLeaf(final T left, final T right) {
        return hashFunction.hashOfItem(left, right);
    }

    private H hashNode(final Node<T, H> left, final Node<T, H> right) {
        return hashFunction.hashOfHash(hashOrNull(left), hashOrNull(right));
    }

    private Node<T, H> getLeafNode(final T left, final T right) {
        return new Node<>(0, hashLeaf(left, right));
    }

    private Node<T, H> getLeafNode(final T left) {
        return new Node<>(0, hashFunction.hashOfItem((left)));
    }

    private Node<T, H> getNode(final Node<T, H> left, final Node<T, H> right) {
        return new Node<>(left, right, left.getLevel() + 1, hashNode(left, right));
    }

    private H hashOrNull(final Node<T, H> node) {
        return node == null ? null : node.getHash();
    }
}
