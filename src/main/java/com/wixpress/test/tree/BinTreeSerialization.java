package com.wixpress.test.tree;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class BinTreeSerialization {
    private static final String NULL_CONSTANT = "null";
    public static final String OPEN_SYMBOL = "[";
    public static final String CLOSE_SYMBOL = "]";

    public static String serialize(BinTree bintree) throws BinTreeSerializationException {
        if (bintree == null) return null;

        // init phase
        StringBuilder builder = new StringBuilder();
        Set<BinTree> serializedNodes = new HashSet<>();
        Queue<BinTree> queue = new LinkedList<>();
        queue.offer(bintree);

        Queue<BinTree> nextParents = new LinkedList<>();
        for (BinTree parent : queue) {
            // serialize
            appendValue(parent, builder);

            // if current parent == null so he doesn't have children
            if (parent == null) {
                continue;
            }

            // add to already serialized list
            serializedNodes.add(parent);

            // save as parent left child
            if (serializedNodes.contains(parent.getLeft())) {
                throw new BinTreeSerializationException("Left node was already serialized");
            } else {
                nextParents.offer(parent.getLeft());
            }

            // save as parent right child
            if (serializedNodes.contains(parent.getRight())) {
                throw new BinTreeSerializationException("Right node was already serialized");
            } else {
                nextParents.offer(parent.getRight());
            }
        }


        throw new BinTreeSerializationException();
    }

    private static StringBuilder appendValue(BinTree node, StringBuilder builder) {
        if (node == null) {
            return builder.append(NULL_CONSTANT);
        }

        return builder.append(OPEN_SYMBOL).append(node.getValue()).append(CLOSE_SYMBOL);
    }
}

