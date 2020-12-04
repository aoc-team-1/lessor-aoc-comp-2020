package dk.lessor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day2 {

    public static void main(String[] args) {
        new Day2().runner();
    }

    public void runner() {
        List<PasswordInfo> puzzleOne =  FileUtilsKt.readFile("day_2.txt").stream().flatMap(s -> Stream.of(new PasswordInfo(s))).filter(PasswordInfo::puzzleOne).collect(Collectors.toList());
        List<PasswordInfo> puzzleTwo =  FileUtilsKt.readFile("day_2.txt").stream().flatMap(s -> Stream.of(new PasswordInfo(s))).filter(PasswordInfo::puzzleTwo).collect(Collectors.toList());

        System.out.println("Day 2: Password Philosophy : Puzzle 1 :" + puzzleOne.size());
        System.out.println("Day 2: Password Philosophy : Puzzle 2 :" + puzzleTwo.size());
    }

    class PasswordInfo {

        int min;
        int max;
        char required;
        String password;

        public PasswordInfo(String info) {
            processInfo(info);
        }

        private void processInfo(String info) {
            String[] parts = info.split("-|: | ");
            this.min = Integer.parseInt(parts[0]);
            this.max = Integer.parseInt(parts[1]);
            this.required = parts[2].charAt(0);
            this.password = parts[3];
        }

        public boolean puzzleOne() {
            int charCount = 0;
            for (char c : password.toCharArray()) {
                if (c == required) {
                    if (++charCount > max) {
                        return false;
                    }
                }
            }
            return charCount >= min;
        }

        public boolean puzzleTwo() {
            return password.charAt(min - 1) == required ^ password.charAt(max - 1) == required;
        }
    }
}
