package uk.gov.homeoffice.toolkit.merkle;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class MerkleTreeBaseTest {

    /**
     * Use a function that echoes input for test
     */
    MerkleTree<String, String> mt = new MerkleTree(new HashFunction<String, String>() {

        @Override
        public String hashOfItem(String left, String right) {
            return emptyIfNull(left)+emptyIfNull(right);
        }

        @Override
        public String hashOfHash(String left, String right) {
            return emptyIfNull(left)+emptyIfNull(right);
        }

        private String emptyIfNull(String data){
            if(data==null){
                return "";
            }
            return data;
        }

        @Override
        public String hashOfItem(String item) {
            return item;
        }
    });

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
        List<String> a = Arrays.asList(al);
        Node<String, String> n = mt.buildTree(a.size(), a.iterator());
        String hash = n.getHash();
        for (String s : al) {
            Assert.assertTrue(hash.contains(s));
        }
        Assert.assertEquals(al.length, hash.length());
    }


}
