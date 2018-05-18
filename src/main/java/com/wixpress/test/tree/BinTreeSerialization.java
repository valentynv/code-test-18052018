package com.wixpress.test.tree;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BinTreeSerialization {
    private static final String NULL_CONSTANT = "null";
    private static final String OPEN_SYMBOL = "[";
    private static final String CLOSE_SYMBOL = "]";
    private static final String SPLITTER = ",";
    private static final Pattern VALIDATOR = Pattern.compile("(((null),)|(\\[(.*)\\],))+");

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

        // split string
        String[] split = serialized.split(SPLITTER);

        // extract parent node
        BinTree result = convertCellToNode(split[0]);
        if (result == null) return result;                  // quick exit if parent node is null

        // prepare round variables
        List<BinTree> currentParents = new LinkedList<>();
        currentParents.add(result);
        List<BinTree> nextParents = new LinkedList<>();
        int globalNodeNumber = 1;       // parent node is first node

        // payload
        while (!currentParents.isEmpty()) {
            int parentNumber = currentParents.size();

            for (int i = 0; i < parentNumber; i++) {
                // find current parent
                BinTree currentParent = currentParents.get(i);

                // init
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
                currentParent.setLeft(leftNode);
                currentParent.setRight(rightNode);
            }

            globalNodeNumber += nextParents.size();
            currentParents = nextParents;
            nextParents = new LinkedList<>();
        }

        // return
        return result;
    }

    private static BinTree convertCellToNode(String cellValue) {
        if (cellValue.equals(NULL_CONSTANT)) {
            return null;
        } else{
            String value = cellValue.substring(OPEN_SYMBOL.length(), cellValue.length()  - CLOSE_SYMBOL.length());
            return new BinTree(value);
        }
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

