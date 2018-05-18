package com.wixpress.test.tree;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BinTreeSerialization {
    private static final String NULL_CONSTANT = "null";
    private static final String OPEN_SYMBOL = "[";
    private static final String CLOSE_SYMBOL = "]";
    private static final String SPLITTER = ",";
    private static final Pattern VALIDATOR = Pattern.compile("((null,)|(\\[.*\\],))+");

    public static String serialize(BinTree bintree) throws BinTreeSerializationException {
        if (bintree == null) return null;           // TODO: mb no need

        // init phase
        StringBuilder builder = new StringBuilder();
        List<BinTree> serializedNodes = new LinkedList<>();
        Queue<BinTree> queue = new LinkedList<>();
        queue.offer(bintree);
        Queue<BinTree> nextParents = new LinkedList<>();

        // payload
        while (!queue.isEmpty()) {

            // round
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

            // prepare next round
            queue = nextParents;
            nextParents = new LinkedList<>();
        }

        // return
        return builder.toString();
    }

    public static BinTree deserialize(String serialized) throws BinTreeSerializationException {
        if (serialized == null) throw new BinTreeSerializationException("Cannot deserialize null value");
        if (serialized.isEmpty()) throw new BinTreeSerializationException("Cannot deserialize empty value");

        // validate
        Matcher matcher = VALIDATOR.matcher(serialized);
        if (!matcher.matches()) throw new BinTreeSerializationException("Wrong format");

        String[] split = serialized.split(",");

        return null;
    }

    private static void appendValue(BinTree node, StringBuilder builder) {
        if (node == null) {
            builder.append(NULL_CONSTANT);
        } else {
            builder.append(OPEN_SYMBOL)
                    .append(node.getValue())
                    .append(CLOSE_SYMBOL);
        }

        builder.append(SPLITTER);
    }
}

