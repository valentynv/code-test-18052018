package com.wixpress.test.tree;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class BinTreeTest {

    @Test
    public void testSimpleCase() throws BinTreeSerializationException {
        BinTree tree = new BinTree("A");

        String serialized = tree.serialize();
        System.out.println(serialized);
        BinTree tree2 = BinTree.deserialize(serialized);

        assertEquals("tree equals", tree, tree2);
    }

    @Test
    public void testSerialization() throws BinTreeSerializationException {
        BinTree tree = new BinTree("A",
                new BinTree("B",
                        new BinTree("C", null,
                                new BinTree("E")
                        ),
                        new BinTree("P")
                ),
                new BinTree("G"));

        String serialized = tree.serialize();
        System.out.println(serialized);


        BinTree tree2 = BinTree.deserialize(serialized);

        assertEquals("tree equals", tree, tree2);
    }

    @Test(expected = BinTreeSerializationException.class)
    public void testInvalidSerialization() throws BinTreeSerializationException {
        BinTree.deserialize("I'm not a tree!!!");
        fail("we should not reach here");
    }

    @Test(expected = BinTreeSerializationException.class)
    public void testGraph() throws BinTreeSerializationException {
        BinTree node = new BinTree("T", new BinTree("W"), null);
        BinTree tree = new BinTree("A",
                new BinTree("B",
                        new BinTree("C", null,
                                new BinTree("E")
                        ),
                        new BinTree("P", node, null)
                ),
                new BinTree("G"));
        node.setRight(tree);

        String serialized = tree.serialize();
        fail("we should not reach here");
    }
}
