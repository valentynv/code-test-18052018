package com.wixpress.test.tree;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BinTreeSerialization {
    public static final String NULL_CONSTANT = "null";
    public static final String OPEN_SYMBOL = "[";
    public static final String CLOSE_SYMBOL = "]";
    public static final String SPLITTER = ",";
    private static final Pattern VALIDATOR = Pattern.compile("(((" + NULL_CONSTANT + ")" + SPLITTER + ")|(\\" + OPEN_SYMBOL + "(.*)\\" + CLOSE_SYMBOL + SPLITTER + "))+");

    public static String serialize(BinTree bintree) throws BinTreeSerializationException {
        if (bintree == null) return null;           // TODO: mb no need

        // init phase
        StringBuilder builder = new StringBuilder();
        List<BinTree> serializedNodes = new LinkedList<>();
        Queue<BinTree> queue = new LinkedList<>();
        queue.offer(bintree);

        // payload
        while (!queue.isEmpty()) {
            BinTree parent = queue.poll();

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
                queue.offer(parent.getLeft());
            }

            // save as parent right child
            if (serializedNodes.contains(parent.getRight())) {
                throw new BinTreeSerializationException("Right node was already serialized");
            } else {
                queue.offer(parent.getRight());
            }
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

        // split string
        String[] split = serialized.split(SPLITTER);

        // extract parent node
        BinTree result = convertCellToNode(split[0]);
        if (result == null) return null;                  // quick exit if parent node is null

        // prepare round variables
        List<BinTree> currentParents = new LinkedList<>();
        currentParents.add(result);
        List<BinTree> nextParents = new LinkedList<>();
        int globalNodeNumber = 1;       // parent node is first node

        // payload
        while (!currentParents.isEmpty()) {
            int parentNumber = currentParents.size();

            for (int i = 0; i < parentNumber; i++) {
                // start index for left and right nodes
                int index = globalNodeNumber + i * 2;

                // extract children nodes
                String leftNodeCell = split[index];
                BinTree leftNode = convertCellToNode(leftNodeCell);
                if (leftNode != null) {
                    nextParents.add(leftNode);
                }
                String rightNodeCell = split[index + 1];
                BinTree rightNode = convertCellToNode(rightNodeCell);
                if (rightNode != null) {
                    nextParents.add(rightNode);
                }

                // link to parent
                BinTree currentParent = currentParents.get(i);
                currentParent.setLeft(leftNode);
                currentParent.setRight(rightNode);
            }

            // prepare next round
            globalNodeNumber += parentNumber * 2;
            currentParents = nextParents;
            nextParents = new LinkedList<>();
        }

        // return
        return result;
    }

    private static BinTree convertCellToNode(String cellValue) {
        if (cellValue.equals(NULL_CONSTANT)) {
            return null;
        } else {
            String value = cellValue.substring(OPEN_SYMBOL.length(), cellValue.length() - CLOSE_SYMBOL.length());
            return new BinTree(unescape(value));
        }
    }

    private static void appendValue(BinTree node, StringBuilder builder) {
        if (node == null) {
            builder.append(NULL_CONSTANT);
        } else {
            builder.append(OPEN_SYMBOL)
                    .append(escape(node.getValue()))
                    .append(CLOSE_SYMBOL);
        }

        builder.append(SPLITTER);
    }

    // TODO: future implementation
    private static String escape(String value) {
        return value;
    }

    private static String unescape(String value) {
        return value;
    }
}

