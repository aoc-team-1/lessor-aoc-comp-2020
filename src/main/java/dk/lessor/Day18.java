package dk.lessor;

import dk.lessor.FileUtilsKt;

import java.util.List;

public class Day18 {

    public static void main(String[] args) {
        new Day18().runner();
    }

    public void runner() {

        List<String> instructions = FileUtilsKt.readFile("day_18.txt");
        puzzleOne(instructions);
//        puzzleTwo(instructions);
    }

    private void puzzleOne(List<String> expressions) {
        int sum = 0;
        for (String s : expressions) {
            sum += calculator(s);
        }
        System.out.println(sum);
    }

    private void puzzleTwo(List<String> expressions) {

    }

    private int calculator(String exp) {
        if (exp.contains("(")) {
            int indexBegin = exp.indexOf("(");
            int indexEnd = findEnd(exp);
            String subExp = exp.substring(indexBegin + 1, indexEnd);
            exp = exp.replace("(" +subExp+ ")", "" + calculator(subExp));
            return calculator(exp);
        } else {
            return simpleExpHandler(exp);
        }
    }

    private int findEnd(String exp) {
        char[] characters = exp.toCharArray();
        int counter = 0;
        for (int i = exp.indexOf("("); i < characters.length; i++) {
            if ('(' == characters[i]) {
                counter++;
            }
            if (')' == characters[i]) {
                counter--;
            }
            if (counter == 0) {
                return i;
            }
        }
        return characters.length - 1;
    }

    private int simpleExpHandler(String exp) {
        String[] array = exp.split(" ");
        int res = 0;
        String operant = null;
        for (String s : array) {
            int tmp = 0;
            if (s.matches("[+*]")) {
                operant = s;
            } else {
                tmp = Integer.parseInt(s);
            }
            if (tmp != 0) {
                if ("+".equals(operant)) {
                    res += tmp;
                } else if ("*".equals(operant)) {
                    res *= tmp;
                } else {
                    res = tmp;
                }
            }
        }
        return res;
    }
}
