package dk.lessor;

public class Day1 {
    public static void main(String[] args) {
        new Day1().runner();
    }

    public void runner() {

        Integer[] input = FileUtilsKt.readFile("day_1.txt").stream().map(Integer::parseInt).toArray(Integer[]::new);

        boolean puzzleOneNFound = false;
        boolean puzzleTwoFound = false;

        for (int i = 0; i < input.length; i++) {
            for (int j = i + 1; j < input.length; j++) {
                if (input[i] + input[j] == 2020) {
                    System.out.println("Day 1: Report Repair : Puzzle 1 :" + (input[i] * input[j]));
                    puzzleOneNFound = true;
                }
                if (!puzzleTwoFound) {
                    for (int k = j + 1; k < input.length; k++) {
                        if (input[i] + input[j] + input[k] == 2020) {
                            System.out.println("Day 1: Report Repair : Puzzle 2 :" + (input[i] * input[j] * input[k]));
                            puzzleTwoFound = true;
                        }
                    }
                }
                if (puzzleOneNFound && puzzleTwoFound) {
                    return;
                }
            }
        }
    }
}
