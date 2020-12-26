package dk.lessor;

import dk.lessor.LinkedList.Node;

public class Day23 {

    public static void main(String[] args) {
        new Day23().run();
    }

    private void run() {
        puzzleOne();
        puzzleTwo();
    }

    private void puzzleOne() {
        LinkedList linkedList = fetchData();
        int moves = 100;
        run(linkedList, moves, 9);
        Node currentNode = linkedList.search(1).next;
        while (true) {
            System.out.print(currentNode.number);
            if (currentNode.next.number == 1) {
                break;
            }
            currentNode = currentNode.next;
        }
        System.out.println();
    }

    private void run(LinkedList list, int moves, int size) {
        list.reset();
        for (int i = 0; i < moves; i++) {
            runCrabCup(list, size);
            list.next();
        }
    }

    private void runCrabCup(LinkedList list, int size) {
        LinkedList splitCup = new LinkedList();
        for (int i = 0; i < 3; i++) {
            splitCup.appendLast(list.removeNext());
        }
        long nextCup = list.current.number - 1;

        while (splitCup.contains(nextCup) || nextCup < 1) {
            nextCup = nextCup - 1;
            if (nextCup < 1) {
                nextCup = size;
            }
        }

        list.append(splitCup, nextCup);
    }

    private void puzzleTwo() {
        LinkedList list = fetchData();
        for (int i = 10; i <= 1000000; i++) {
            list.appendLast(new Node(i));
        }
        int moves = 10000000;
        run(list, moves, 1000000);

        Node numberOne = list.search(1);
        Node first = numberOne.next;
        Node second = numberOne.next.next;
        System.out.println(first.number * second.number);

        System.out.println("End");
    }

    private LinkedList fetchData() {
        LinkedList list = new LinkedList();
        for (char number : FileUtilsKt.readFile("day_23.txt").get(0).toCharArray()) {
            list.appendLast(new Node(number - 48));
        }
        return list;
    }
}
