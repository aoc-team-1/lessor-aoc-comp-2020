package dk.lessor;

import java.util.HashMap;
import java.util.Map;

public class LinkedList {

    long size = 0;
    Node first;
    Node last;
    Node current;

    Map<Long, Node> map = new HashMap<>();

    public void append(LinkedList list, long nextCup) {
        if (first != null) {
            Node current = search(nextCup);
            Node next = current.next;
            list.first.previous = current;
            current.next = list.first;
            next.previous = list.last;
            list.last.next = next;
        } else {
            first = list.first;
            last = list.last;
            this.current = list.current;
        }
        this.map.putAll(list.map);
        size += list.size;
    }

    public void appendLast(Node node) {
        if (first != null) {
            last.next = node;
            node.next = first;
            node.previous = last;
            first.previous = node;
            last = node;
        } else {
            addFirst(node);
        }
        this.map.put(node.number, node);
        size++;
    }

    private void addFirst(Node node) {
        first = node;
        last = node;
        current = node;
        node.next = node;
        node.previous = node;
    }

    public void reset() {
        current = first;
    }

    public void next() {
        current = current.next;
    }

    public Node removeNext() {
        Node nodeToRemove = current.next;
        if (nodeToRemove == first) {
            first = nodeToRemove.next.next;
        }
        Node previous = nodeToRemove.previous;
        Node next = nodeToRemove.next;
        previous.next = next;
        next.previous = previous;

        nodeToRemove.next = nodeToRemove;
        nodeToRemove.previous = nodeToRemove;
        map.remove(nodeToRemove.number);
        return nodeToRemove;
    }

    public boolean contains(long number) {
        return search(number) != null;
    }

    public Node search(long number) {
        return map.get(number);
    }

    public static class Node {
        long number;
        Node next;
        Node previous;

        public Node(long number) {
            this.number = number;
            next = this;
            previous = this;
        }
    }
}
