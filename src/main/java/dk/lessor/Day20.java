package dk.lessor;

import kotlin.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day20 {

    public static void main(String[] args) {
        new Day20().run();
    }

    private void run() {
        Map<Integer, Tile> tiles = parseInputToTiles(FileUtilsKt.readFile("day_20.txt"));
        matchTiles(tiles);
        puzzleOne(tiles);
        puzzleTwo(tiles);
    }

    private void puzzleOne(Map<Integer, Tile> tiles) {
        long result = 1;
        for (Tile tile : tiles.values()) {
            if (tile.sideTiles.size() == 2) {
                System.out.println(tile.id);
                result *= tile.id;
            }
        }
        System.out.println(result);
    }

    private void puzzleTwo(Map<Integer, Tile> tiles) {

        Tile startingTile = null;

        for (Tile tile : tiles.values()) {
            if (tile.sideTiles.size() == 2) {
                startingTile = tile;
                break;
            }
        }
        if (startingTile != null) {
            char[][] lines = startingTile.generateMap();
            char[][] tmp = lines.clone();
            int count = 0;

            boolean seaMonsterFound = false;
            LOOP:
            for (int k = 0; k < 2; k++) {
                for (int j = 0; j < 4; j++) {
                    for (int i = 1; i < lines.length - 1; i++) {
                        String line = new String(lines[i]);
                        List<String> test = test(line);
                        for (String testString : test) {
                            int index = line.indexOf(testString);
                            char[] prev = lines[i - 1];
                            char[] next = lines[i + 1];

                            if (prev[index + 18] == '#' && next[index + 16] == '#' && next[index + 13] == '#' && next[index + 10] == '#' && next[index + 7] == '#' && next[index + 4] == '#' && next[index + 1] == '#') {
                                markSeaMonster(tmp, i, index);
                                seaMonsterFound = true;
                            }
                        }
                    }
                    if (seaMonsterFound) {
                        break LOOP;
                    }
                    tmp = rotatePicture(tmp);
                    lines = rotatePicture(lines);
                }
                tmp = flipPicture(tmp);
                lines = flipPicture(lines);
            }

            for (char[] line : lines) {
                System.out.println(new String(line));
            }
            System.out.println();
            for (char[] chars : tmp) {
                for (char aChar : chars) {
                    if (aChar == '#') {
                        count++;
                    }
                }
                System.out.println(new String(chars));
            }
            System.out.println(count);
        }
    }

    private void markSeaMonster(char[][] tmp, int i, int index) {
        char[] current = tmp[i];
        char[] prev = tmp[i - 1];
        char[] next = tmp[i + 1];

        char mark = 'O';
        prev[index + 18] = mark;

        current[index] = mark;
        current[index + 5] = mark;
        current[index + 6] = mark;
        current[index + 11] = mark;
        current[index + 12] = mark;
        current[index + 17] = mark;
        current[index + 18] = mark;
        current[index + 19] = mark;

        next[index + 16] = mark;
        next[index + 13] = mark;
        next[index + 10] = mark;
        next[index + 7] = mark;
        next[index + 4] = mark;
        next[index + 1] = mark;
    }

    public char[][] rotatePicture(char[][] picture) {
        char[][] tmp = new char[picture.length][picture.length];

        for (int x = 0; x < picture.length; x++) {
            for (int y = 0; y < picture.length; y++) {
                tmp[y][picture.length - 1 - x] = picture[x][y];
            }
        }
        return tmp;
    }

    private char[][] flipPicture(char[][] lines) {
        char[][] tmp = new char[lines.length][lines.length];
        for (int x = 0; x < lines.length; x++) {
            tmp[x] = lines[lines.length - 1 - x];
        }
        return tmp;
    }

    private List<String> test(String line) {
        List<String> match = new ArrayList<>();
        final Pattern pattern = Pattern.compile("#([.#]{4})##([.#]{4})##([.#]{4})###", Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(line);

        while (matcher.find()) {
            match.add(matcher.group(0));
        }
        return match;
    }

    private void matchTiles(Map<Integer, Tile> tiles) {
        for (Tile current : tiles.values()) {
            for (Tile next : tiles.values()) {
                if (next.id == current.id) {
                    continue;
                }

                matchTile(current, next, Direction.TOP);
                matchTile(current, next, Direction.LEFT);
                matchTile(current, next, Direction.BOTTOM);
                matchTile(current, next, Direction.RIGHT);

                if (current.sideTiles.size() == 4) {
                    break;
                }
            }
        }
    }

    private void matchTile(Tile current, Tile next, Direction side) {
        if (current.sideTiles.get(side) == null) {
            Direction direction = compare(next, current.sides.get(side));
            if (direction != null) {
                current.sideTiles.put(side, next);
            }
        }
    }

    private Direction compare(Tile nextTile, Pair<Long, Long> currentSide) {
        for (Map.Entry<Direction, Pair<Long, Long>> entry : nextTile.sides.entrySet()) {
            if (compareSides(currentSide, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    private boolean compareSides(Pair<Long, Long> current, Pair<Long, Long> test) {
        return current.getFirst().equals(test.getFirst()) || current.getFirst().equals(test.getSecond()) || current.getSecond().equals(test.getSecond());
    }

    private Map<Integer, Tile> parseInputToTiles(List<String> lines) {
        Map<Integer, Tile> tiles = new HashMap<>();
        Tile tmp = null;
        int index = 0;
        char[][] data = new char[10][10];
        for (String line : lines) {
            if (line.startsWith("Tile")) {
                tmp = new Tile(line.replaceAll("^Tile |:", ""));
                tiles.put(tmp.id, tmp);
                data = new char[10][10];
                index = 0;
            } else if (!line.isEmpty()) {
                char[] chars = line.toCharArray();
                data[index++] = chars;
            } else {
                Objects.requireNonNull(tmp).addData(data);
                Objects.requireNonNull(tmp).calcSides();
            }
        }
        Objects.requireNonNull(tmp).addData(data);
        Objects.requireNonNull(tmp).calcSides();
        return tiles;
    }

    private static class Tile {

        private final int id;
        private char[][] data;

        private final Map<Direction, Pair<Long, Long>> sides = new HashMap<>();
        private Map<Direction, Tile> sideTiles = new HashMap<>();

        public Tile(String tileId) {
            this.id = Integer.parseInt(tileId);
        }

        public void addData(char[][] data) {
            this.data = data;
        }

        public void calcSides() {
            StringBuilder leftSide = new StringBuilder();
            StringBuilder rightSide = new StringBuilder();

            for (char[] datum : data) {
                leftSide.append(datum[0]);
                rightSide.append(datum[data.length - 1]);
            }
            sides.clear();
            sides.put(Direction.TOP, stringToPair(new String(data[0])));
            sides.put(Direction.LEFT, stringToPair(leftSide.toString()));
            sides.put(Direction.BOTTOM, stringToPair(new String(data[data.length - 1])));
            sides.put(Direction.RIGHT, stringToPair(rightSide.toString()));
        }

        private Pair<Long, Long> stringToPair(String sideInfo) {
            StringBuilder stringBuilder = new StringBuilder(sideInfo.replaceAll("#", "1").replaceAll("\\.", "0"));

            long side = Long.parseLong(stringBuilder.toString(), 2);
            long reversedSide = Long.parseLong(stringBuilder.reverse().toString(), 2);

            return new Pair<>(side, reversedSide);
        }

        public void rotate() {
            char[][] tmp = new char[10][10];

            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    tmp[y][9 - x] = data[x][y];
                }
            }
            data = tmp;
            rotateTiles();

            calcSides();
        }

        private void rotateTiles() {
            Map<Direction, Tile> tmp = new HashMap<>();
            sideTiles.forEach((direction, tile) -> {
                if (direction == Direction.TOP) {
                    tmp.put(Direction.RIGHT, tile);
                } else if (direction == Direction.RIGHT) {
                    tmp.put(Direction.BOTTOM, tile);
                } else if (direction == Direction.BOTTOM) {
                    tmp.put(Direction.LEFT, tile);
                } else {
                    tmp.put(Direction.TOP, tile);
                }
            });
            sideTiles = tmp;
        }

        public void flip(Direction direction) {
            char[][] tmp = new char[10][10];

            if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                for (int x = 0; x < 10; x++) {
                    tmp[x] = data[9 - x];
                }
                Tile top = sideTiles.get(Direction.TOP);
                Tile bottom = sideTiles.get(Direction.BOTTOM);

                sideTiles.put(Direction.TOP, bottom);
                sideTiles.put(Direction.BOTTOM, top);
            } else {
                for (int x = 0; x < 10; x++) {
                    for (int y = 0; y < 10; y++) {
                        tmp[x][y] = data[x][9 - y];
                    }
                }
                Tile left = sideTiles.get(Direction.LEFT);
                Tile right = sideTiles.get(Direction.RIGHT);

                sideTiles.put(Direction.LEFT, right);
                sideTiles.put(Direction.RIGHT, left);
            }

            data = tmp;
            calcSides();
        }

        public char[][] generateMap() {
            do {
                this.rotate();
            } while (sideTiles.get(Direction.TOP) != null || sideTiles.get(Direction.LEFT) != null);
            Tile startOfLine = this;
            List<String> lines = new ArrayList<>();
            do {
                for (int i = 1; i < 9; i++) {
                    Tile currentTile = startOfLine;
                    StringBuilder stringBuilder = new StringBuilder();
                    while (currentTile != null) {
                        char[] datum = currentTile.data[i];

                        for (int j = 1; j < datum.length - 1; j++) {
                            stringBuilder.append(datum[j]);
                        }
                        Tile next = currentTile.sideTiles.get(Direction.RIGHT);
                        while (next != null && currentTile.id != next.sideTiles.getOrDefault(Direction.LEFT, new Tile("-1")).id) {
                            next.rotate();
                        }
                        if (next != null && currentTile.sides.get(Direction.RIGHT).getFirst().equals(next.sides.get(Direction.LEFT).getSecond())) {
                            next.flip(Direction.RIGHT);
                        }
                        currentTile = next;
                    }
                    lines.add(stringBuilder.toString());
                }
                Tile nextLineStart = startOfLine.sideTiles.get(Direction.BOTTOM);

                while (nextLineStart != null && startOfLine.id != nextLineStart.sideTiles.getOrDefault(Direction.TOP, new Tile("-1")).id) {
                    nextLineStart.rotate();
                }
                if (nextLineStart != null && startOfLine.sides.get(Direction.BOTTOM).getFirst().equals(nextLineStart.sides.get(Direction.TOP).getSecond())) {
                    nextLineStart.flip(Direction.BOTTOM);
                }
                startOfLine = nextLineStart;
            } while (startOfLine != null);

            char[][] result = new char[lines.size()][lines.size()];
            for (int i = 0; i < lines.size(); i++) {
                result[i] = lines.get(i).toCharArray();
            }
            return result;
        }
    }

    private enum Direction {
        TOP, LEFT, BOTTOM, RIGHT
    }
}
