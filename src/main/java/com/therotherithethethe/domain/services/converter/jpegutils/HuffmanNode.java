package com.therotherithethethe.domain.services.converter.jpegutils;
/**
 * The {@code HuffmanNode} class represents a node in the Huffman Tree.
 */
class HuffmanNode {
    int value;
    int frequency;
    HuffmanNode left;
    HuffmanNode right;
    /**
     * Constructs a {@code HuffmanNode} with the given value and frequency.
     *
     * @param value the value of the node
     * @param frequency the frequency of the node
     */
    HuffmanNode(int value, int frequency) {
        this.value = value;
        this.frequency = frequency;
        this.left = null;
        this.right = null;
    }
}