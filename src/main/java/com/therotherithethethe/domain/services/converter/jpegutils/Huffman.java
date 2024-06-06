package com.therotherithethethe.domain.services.converter.jpegutils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * The {@code Huffman} class is responsible for building a Huffman Tree and encoding data
 * using Huffman coding.
 */
public class Huffman {
    private Map<Integer, String> huffmanCode = new HashMap<>();
    /**
     * Builds a Huffman Tree for the given data.
     *
     * @param data an array of integers representing the data to be encoded
     */
    public void buildHuffmanTree(int[] data) {
        Map<Integer, Integer> frequency = new HashMap<>();

        for (int num : data) {
            frequency.put(num, frequency.getOrDefault(num, 0) + 1);
        }

        PriorityQueue<HuffmanNode> heap = new PriorityQueue<>(
            Comparator.comparingInt(a -> a.frequency));
        frequency.forEach((key, freq) -> heap.add(new HuffmanNode(key, freq)));

        while (heap.size() > 1) {
            HuffmanNode left = heap.poll();
            HuffmanNode right = heap.poll();

            HuffmanNode sum = new HuffmanNode(-1, left.frequency + right.frequency);
            sum.left = left;
            sum.right = right;

            heap.add(sum);
        }

        if (!heap.isEmpty()) {
            generateCodes(heap.poll(), "");
        }
    }

    private void generateCodes(HuffmanNode node, String code) {
        if (node != null) {
            if (node.value != -1) {  // This is a leaf node
                huffmanCode.put(node.value, code);
            }
            generateCodes(node.left, code + "0");
            generateCodes(node.right, code + "1");
        }
    }
    /**
     * Encodes the given data using the previously built Huffman Tree.
     *
     * @param data an array of integers representing the data to be encoded
     * @return a string representing the encoded data
     */
    public String encode(int[] data) {
        StringBuilder encodedData = new StringBuilder();
        for (int num : data) {
            encodedData.append(huffmanCode.get(num));
        }
        return encodedData.toString();
    }
    /**
     * Returns the Huffman codes for the data.
     *
     * @return a map containing the Huffman codes
     */
    public Map<Integer, String> getHuffmanCodes() {
        return huffmanCode;
    }
}