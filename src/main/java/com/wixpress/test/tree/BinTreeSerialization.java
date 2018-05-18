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
        validateSerializedValue(serialized);

        // extract parent node
        String[] split = serialized.split(SPLITTER);
        BinTree result = convertCellToNode(split[0]);
        if (result == null) return null;                  // quick exit if parent node is null

        // prepare round variables
        Queue<BinTree> currentParents = new LinkedList<>();
        currentParents.add(result);

        // payload
        for (int globalNodeNumber = 1 /*parent node is first node*/; !currentParents.isEmpty(); globalNodeNumber += 2) {
            // extract left node
            String leftNodeCell = split[globalNodeNumber];
            BinTree leftNode = convertCellToNode(leftNodeCell);
            if (leftNode != null) {
                currentParents.offer(leftNode);
            }

            // extract right node
            String rightNodeCell = split[globalNodeNumber + 1];
            BinTree rightNode = convertCellToNode(rightNodeCell);
            if (rightNode != null) {
                currentParents.offer(rightNode);
            }

            // link to parent
            BinTree currentParent = currentParents.poll();
            currentParent.setLeft(leftNode);
            currentParent.setRight(rightNode);
        }

        // return
        return result;
    }

    private static void validateSerializedValue(String serialized) throws BinTreeSerializationException {
        if (serialized == null) throw new BinTreeSerializationException("Cannot deserialize null value");
        if (serialized.isEmpty()) throw new BinTreeSerializationException("Cannot deserialize empty value");

        // validate
        Matcher matcher = VALIDATOR.matcher(serialized);
        if (!matcher.matches()) throw new BinTreeSerializationException("Wrong format!");
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

