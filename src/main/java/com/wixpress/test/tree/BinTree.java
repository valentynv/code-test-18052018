package com.wixpress.test.tree;

public class BinTree {
    private String value;
    private BinTree left;
    private BinTree right;

    public BinTree(String value) {
        this(value, null, null);
    }

    public BinTree(String value, BinTree left, BinTree right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public BinTree getLeft() {
        return left;
    }

    public void setLeft(BinTree left) {
        this.left = left;
    }

    public BinTree getRight() {
        return right;
    }

    public void setRight(BinTree right) {
        this.right = right;
    }

    public String toString() {
        try {
            return serialize();
        } catch (BinTreeSerializationException e) {
            return "failed to represent the tree as a string cause of " + e.getMessage();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BinTree binTree = (BinTree) o;

        if (left != null ? !left.equals(binTree.left) : binTree.left != null) return false;
        if (right != null ? !right.equals(binTree.right) : binTree.right != null) return false;
        if (value != null ? !value.equals(binTree.value) : binTree.value != null) return false;

        return true;
    }

    ///////////////////////////
    //                       //
    //      Implement:       //
    //                       //
    ///////////////////////////

    public String serialize() throws BinTreeSerializationException {
        return "";
    }

    public static BinTree deserialize(String serialized) throws BinTreeSerializationException {
        return null;
    }
}
