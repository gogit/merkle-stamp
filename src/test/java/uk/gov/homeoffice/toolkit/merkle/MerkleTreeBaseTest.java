package uk.gov.homeoffice.toolkit.merkle;

import org.junit.Assert;
import org.junit.Test;
import uk.gov.homeoffice.toolkit.hash.EchoHashFunction;
import uk.gov.homeoffice.toolkit.hash.HashFunction;
import uk.gov.homeoffice.toolkit.util.JsonParser;

import java.util.Arrays;

public class MerkleTreeBaseTest {

    /**
     * Use a function that echoes input for test
     */
    MerkleTree<String, String> mt = new MerkleTree(new EchoHashFunction());

    @Test
    public void test1() {
        test("a");
    }

    @Test
    public void test2() {
        test("ab");
    }

    @Test
    public void test3() {
        test("abc");
    }

    @Test
    public void test4() {
        test("abcd");
    }

    @Test
    public void test5() {
        test("abcde");
    }

    @Test
    public void testAlphabets() {
        test("abcdefghijklmnopqrstuvwxyz");
    }

    @Test
    public void testAlphaNumeric() {
        test("abcdefghijklmnopqrstuvwxyz0123456789");
    }

    private void test(String data) {
        String[] al = data.split("");
        Arrays.stream(al).forEach(s->mt.push(s));
        Node<String, String> n = mt.buildTree();
        String hash = n.getHash();
        for (String s : al) {
            Assert.assertTrue(hash.contains(s));
        }
        Assert.assertEquals(al.length, hash.length());

        System.out.println(new JsonParser<Node<String, String>>().asPrettyString(n));
    }

}
