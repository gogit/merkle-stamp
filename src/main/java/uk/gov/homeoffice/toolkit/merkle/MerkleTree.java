package uk.gov.homeoffice.toolkit.merkle;

import uk.gov.homeoffice.toolkit.hash.HashFunction;

import java.util.ArrayList;
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
    private final HashFunction<T, H> hashFunction;

    /**
     *
     */
    private final List<T> leaves;

    /**
     * @param hashFunction
     */
    public MerkleTree(HashFunction<T, H> hashFunction) {
        this.hashFunction = hashFunction;
        this.leaves = new ArrayList<>();
    }

    /**
     * @param leaf
     */
    public void push(T leaf) {
        leaves.add(leaf);
    }

    /**
     * @return
     */
    public Node<T, H> buildTree() {
        int size = leaves.size();
        Iterator<T> recs = leaves.iterator();
        if (size <= 1) {
            return new Node<>(1, hashFunction.hashOfItem(recs.next()));
        }

        Stack<Node<T, H>> stackCurrent = new Stack<>();
        Stack<Node<T, H>> stackUp = new Stack<>();

        buildLeafLevel(size, recs, stackCurrent);

        stackPush(stackCurrent, stackUp);
        while (!stackUp.isEmpty()) {
            if (stackUp.size() == 1) {
                return stackUp.pop().setLeafCount(size);
            }
            stackCurrent = stackUp;
            stackUp = new Stack<>();
            stackPush(stackCurrent, stackUp);
        }
        throw new IllegalStateException("Shouldn't be here");
    }

    private void buildLeafLevel(final int size, Iterator<T> recs, final Stack<Node<T, H>> stackCurrent) {
        recs.forEachRemaining(it->stackCurrent.push(getLeafNode(it)));
    }

    private void stackPush(final Stack<Node<T, H>> stackCurrent, final Stack<Node<T, H>> stackUp) {
        while (!stackCurrent.isEmpty()) {
            if (stackCurrent.size() == 1) {
                stackUp.push(getNode(stackCurrent.pop(), null));
                return;
            }
            stackUp.push(getNode(stackCurrent.pop(), stackCurrent.pop()));
        }
    }

    private H hashLeaf(final T left, final T right) {
        return hashFunction.hashOfItem(left, right);
    }

    private H hashNode(final Node<T, H> left, final Node<T, H> right) {
        return hashFunction.hashOfHash(hashOrNull(left), hashOrNull(right));
    }

    /*
    private Node<T, H> getLeafNode(final T left, final T right) {
        return new Node<>(0, hashLeaf(left, right));
    }*/

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
