package dk.lessor;

import java.util.ArrayList;
import java.util.List;

public class Day3 {

    private boolean printMap = false;
    private List<String> list = FileUtilsKt.readFile("day_3.txt");

    public static void main(String[] args) {
        new Day3().runner();
    }

    public void runner() {
        puzzleOne();
        puzzleTwo();
    }
    private void puzzleOne() {
        List<int[]> list = new ArrayList<>();
        list.add(new int[]{3, 1});

        runMap(list);
    }

    private void puzzleTwo() {
        List<int[]> list = new ArrayList<>();
        list.add(new int[]{1, 1});
        list.add(new int[]{3, 1});
        list.add(new int[]{5, 1});
        list.add(new int[]{7, 1});
        list.add(new int[]{1, 2});

        runMap(list);
    }

    private void runMap(List<int[]> list) {
        Long result = 1L;
        for (int[] ints : list) {
            int i = calcResult(ints[0], ints[1]);
            result *= i;
        }
        System.out.println(result);
    }

    private int calcResult(int right, int down) {
        Integer position = 0;
        int treeCount = 0;
        if(printMap) {
            System.out.println(list.get(0));
        }
        for (int i = down, listSize = list.size(); i < listSize; i += down) {
            String submap = list.get(i);
            String map = submap;

            position += right;

            while (map.length() - 1 < position) {
                map += submap;
            }

            char isTree = 'O';
            if (map.charAt(position) == '#') {
                treeCount++;
                isTree = 'X';
            }
            if(printMap){
                System.out.println(map.substring(0, position) + isTree + map.substring(position + 1));
            }
        }
        return treeCount;
    }
}
