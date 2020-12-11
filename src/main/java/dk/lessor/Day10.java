package dk.lessor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day10 {

    public static void main(String[] args) {
        new Day10().run();
    }

    private void run() {
        puzzleOne();
        puzzleTwo();
    }

    private void puzzleOne() {
        List<Integer> list = FileUtilsKt.readFile("day_10.txt").stream().map(Integer::parseInt).sorted().collect(Collectors.toList());
        int ones = 0;
        int twos = 0;
        int threes = 1;

        int prev = 0;
        for (int i = 0; i < list.size(); i++) {
            int current = list.get(i);
            int diff = current - prev;
            if (1 == diff) {
                ones++;
            } else if (2 == diff) {
                twos++;
            } else if (3 == diff) {
                threes++;
            } else {
                throw new RuntimeException();
            }
            prev = current;
        }

        System.out.println(threes * ones);
    }

    private void puzzleTwo() {
        List<Node> list = new ArrayList<>();
        list.add(new Node(0));
        list.addAll(FileUtilsKt.readFile("day_10.txt").stream().map(Integer::parseInt).sorted().map(Node::new).collect(Collectors.toList()));

        int start = 0;
        Node current = list.get(start);
        long count = processList(list, current, start, list.get(list.size() - 1));

        System.out.println(count);
    }

    private long processList(List<Node> list, Node current, int start, Node lastValue) {
        if (current == lastValue) {
            return 1;
        }

        List<Node> list1 = current.getList();
        if (list1.isEmpty()) {
            for (int j = 1; j < 4; j++) {
                try {
                    if (start + j > list.size()) {
                        break;
                    }
                    Node nextNode = list.get(start + j);
                    if (nextNode.getValue() - current.getValue() < 4) {
                        current.getList().add(nextNode);
                        current.count += processList(list, nextNode, start + j, lastValue);
                    }
                } catch (Exception e) {
                }
            }
        }
        return current.count;
    }
    class Node {

        long count = 0;
        int value;

        List<Node> list = new ArrayList<>();

        public Node(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public List<Node> getList() {
            return list;
        }
        public void setList(List<Node> list) {
            this.list = list;
        }

    }
}
