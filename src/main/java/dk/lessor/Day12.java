package dk.lessor;

import java.util.LinkedList;
import java.util.List;

public class Day12 {
    public static void main(String[] args) {
        new Day12().runner();
    }

    public void runner() {

        List<String> instructions = FileUtilsKt.readFile("day_12.txt");
        puzzleOne(instructions);
        puzzleTwo(instructions);
    }

    private void puzzleOne(List<String> instructions) {
        PuzzleOneNavigation navigation = new PuzzleOneNavigation();
        instructions.stream().map(Move::new).forEach(navigation::move);
        System.out.println(navigation.ship.unitX + navigation.ship.unitY);
    }

    private void puzzleTwo(List<String> instructions) {
        PuzzleTwoNavigation position = new PuzzleTwoNavigation();
        instructions.stream().map(Move::new).forEach(position::move);
        System.out.println(position.ship.unitX + position.ship.unitY);
    }

    static class Move {

        char action;
        long units;

        public Move(String instruction) {
            action = instruction.charAt(0);
            units = Long.parseLong(instruction.substring(1));
        }

        public Move(char action, long units) {
            this.action = action;
            this.units = units;
        }

        @Override
        public String toString() {
            return "Move{" +
                    "action=" + action +
                    ", units=" + units +
                    '}';
        }
    }

    static class PuzzleOneNavigation extends Navigation {

        Position facing = new Position(3, 0L, 0, 0L);
        Position ship = new Position(null, 0L, null, 0L);

        public void move(Move move) {
            if (move.action == 'F') {
                move.action = turn.get(facing.x);
            }
            movePosition(move, ship);

            if (move.action == 'L' || move.action == 'R') {
                turn(move, facing);
            }
        }

        private void turn(Move move, Position position) {
            int turn = (int) (move.units / 90);
            if (move.action == 'R') {
                turn = 4 - turn;
            } else if (move.action == 'L') {
                turn = 4 + turn;
            }
            position.x = (position.x + turn) % 4;
        }
    }

    static class PuzzleTwoNavigation extends Navigation {

        Position flag = new Position(3, 10L, 0, 1L);
        Position ship = new Position(null, 0L, null, 0L);

        public void move(Move move) {
            if (move.action == 'F') {
                movePosition(new Move(turn.get(flag.x), move.units * flag.unitX), ship);
                movePosition(new Move(turn.get(flag.y), move.units * flag.unitY), ship);
                return;
            }

            movePosition(move, flag);

            if (move.action == 'L' || move.action == 'R') {
                turn(move, flag);
            }
        }

        private void turn(Move move, Position position) {
            int turn = (int) (move.units / 90);
            if (move.action == 'R') {
                turn = 4 - turn;
            } else if (move.action == 'L') {
                turn = 4 + turn;
            }
            int tmpY = (position.y + turn) % 4;
            int tmpX = (position.x + turn) % 4;
            if (turn % 2 != 0) {
                position.x = tmpY;
                position.y = tmpX;
                Long tmp = position.unitX;
                position.unitX = position.unitY;
                position.unitY = tmp;
            } else {
                position.y = tmpY;
                position.x = tmpX;
            }
        }
    }

    static class Position {

        Integer x;
        Long unitX;

        Integer y;
        Long unitY;

        public Position(Integer x, Long unitX, Integer y, Long unitY) {
            this.x = x;
            this.unitX = unitX;
            this.y = y;
            this.unitY = unitY;
        }

        @Override
        public String toString() {
            return "Position{" +
                    "x=" + x +
                    ", unitX=" + unitX +
                    ", y=" + y +
                    ", unitY=" + unitY +
                    '}';
        }
    }

    private static abstract class Navigation {

        LinkedList<Character> turn = new LinkedList<>(List.of('N', 'W', 'S', 'E'));

        protected void movePosition(Move move, Position position) {
            if (move.action == 'E' || move.action == 'W') {
                if (position.x == null) {
                    position.x = move.action == 'E' ? 3 : 1;
                    position.unitX += move.units;
                } else if (turn.get(position.x) == move.action) {
                    position.unitX += move.units;
                } else {
                    position.unitX -= move.units;
                }

                if (position.unitX < 0) {
                    position.x = position.x == 3 ? 1 : 3;
                    position.unitX *= -1;
                }
            }

            if (move.action == 'N' || move.action == 'S') {
                if (position.y == null) {
                    position.y = move.action == 'N' ? 0 : 2;
                    position.unitY += move.units;
                } else if (turn.get(position.y) == move.action) {
                    position.unitY += move.units;
                } else {
                    position.unitY -= move.units;
                }

                if (position.unitY < 0) {
                    position.y = position.y == 0 ? 2 : 0;
                    position.unitY *= -1;
                }
            }
        }
    }
}
