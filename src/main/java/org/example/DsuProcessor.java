package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class DsuProcessor {
    private final Map<Integer, Map<String, Node>> allColumnWords = new HashMap<>();
    private final List<Node> nodes = new ArrayList<>();

    public void add(String[] line) {
        Node node = new Node(line, 1);
        nodes.add(node);
        processNode(node);
    }

    private void processNode(Node node) {                   //O(m), m - words in line
        String[] columnWords = node.columnWords;
        for (int i = 0; i < columnWords.length; i++) {      //O(m), m - words in line
            String word = columnWords[i];
            if (word.equals("\"\"")) {
                continue;
            }
            Map<String, Node> wordsInColumn = allColumnWords.computeIfAbsent(i, k -> new HashMap<>());    //O(1)
            Node peerNode = wordsInColumn.put(word, node);              //O(1)
            if (peerNode != null) {
                union(node, peerNode);                                  //O(1)
            }
        }
    }

    private void union(Node node1, Node node2) {    //almost O(1) amortized
        node1 = find(node1);    //find set1 representation
        node2 = find(node2);    //find set2 representation
        if (node1 == node2) {
            return;
        }
        if (node1.weight > node2.weight) {
            Node temp = node1;
            node1 = node2;
            node2 = temp;
        }
        //node1 weight here is less or equal and should be connected to node2
        node2.weight += node1.weight;
        node1.weight = 0;
        node1.parent = node2;
    }

    private Node find(Node node) {      //almost O(1) amortized
        Node initialNode = node;
        while (node.parent != node) {    //until we get to representation
            node = node.parent;
        }
        Node representationNode = node;
        while (initialNode.parent != representationNode) {   //until we get to representation
            node = initialNode;
            initialNode = initialNode.parent;
            node.parent = representationNode;
        }
        return representationNode;
    }

    public List<List<String>> getSortedLineGroupsWithLeastSize(int leastSize) {
        //O(min(n, mLogM)), n - number of nodes, m - number of groups
        Map<Node, List<String>> nodeGroups = new HashMap<>();
        for (Node node : nodes) {                                   //O(n), n - number of nodes
            Node representation = find(node);                       //O(1)
            List<String> group = nodeGroups.computeIfAbsent(representation, k -> new ArrayList<>());    //O(1)
            group.add(getInitialLine(node.columnWords));            //O(1) amortized
        }
        return nodeGroups.values()
                .stream()
                .filter(list -> list.size() >= leastSize)
                .sorted((o1, o2) -> o2.size() - o1.size())        //O(mLogM), m - number of groups
                .collect(Collectors.toList());
    }

    private String getInitialLine(String[] words) {
        return String.join(";", words);
    }

    private static class Node {
        private Node parent;
        private final String[] columnWords;
        private int weight;

        private Node(String[] columnWords, int weight) {
            this.parent = this;
            this.columnWords = columnWords;
            this.weight = weight;
        }
    }
}
