package dk.lessor;

import kotlin.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Day20 {

    public static void main(String[] args) {
        new Day20().run();
    }

    private void run() {
        List<Tile> tiles = parseInputToTiles(FileUtilsKt.readFile("day_20.txt"));
        puzzleOne(tiles);
    }

    private void puzzleOne(List<Tile> tiles) {
        long result = 1;
        tiles.forEach(Tile::calcSides);
        for (Tile tile : tiles) {

            int count = 0;
            for (Tile nextTile : tiles) {
                if (nextTile.id != tile.id) {
                    count += compare(nextTile, tile.topSide);
                    count += compare(nextTile, tile.leftSide);
                    count += compare(nextTile, tile.bottomSide);
                    count += compare(nextTile, tile.rightSide);
                }
            }
            if (count == 2) {
                result *= tile.id;
            }
        }
        System.out.println(result);
    }

    private int compare(Tile nextTile, Pair<Long, Long> currentSide) {
        return (compareSides(currentSide, nextTile.topSide) || compareSides(currentSide, nextTile.leftSide) || compareSides(currentSide, nextTile.bottomSide) || compareSides(currentSide, nextTile.rightSide)) ? 1 : 0;
    }

    private boolean compareSides(Pair<Long, Long> current, Pair<Long, Long> test) {
        return current.getFirst().equals(test.getFirst()) || current.getFirst().equals(test.getSecond()) || current.getSecond().equals(test.getSecond());
    }

    private List<Tile> parseInputToTiles(List<String> lines) {
        List<Tile> tiles = new ArrayList<>();
        Tile tmp = null;
        for (String line : lines) {
            if (line.startsWith("Tile")) {
                tmp = new Tile(line.replaceAll("^Tile |:", ""));
                tiles.add(tmp);
            } else if (!line.isEmpty()) {
                Objects.requireNonNull(tmp).addData(line);
            } else {
                Objects.requireNonNull(tmp).calcSides();
            }
        }
        return tiles;
    }

    private static class Tile {

        private final int id;
        private final List<String> data = new ArrayList<>();

        private Pair<Long, Long> topSide;
        private Pair<Long, Long> leftSide;
        private Pair<Long, Long> bottomSide;
        private Pair<Long, Long> rightSide;

        public Tile(String tileId) {
            this.id = Integer.parseInt(tileId);
        }

        public void addData(String line) {
            data.add(line);
        }

        public void calcSides() {
            String ref = data.get(0);

            String topSide = "";
            StringBuilder leftSide = new StringBuilder();
            String bottomSide = "";
            StringBuilder rightSide = new StringBuilder();

            for (int i = 0; i < data.size(); i++) {
                String d = data.get(i);
                if (i == 0) {
                    topSide = d;
                } else if (i == data.size() - 1) {
                    bottomSide = d;
                }
                leftSide.append(d.charAt(0));
                rightSide.append(d.charAt(ref.length() - 1));
            }

            this.topSide = stringToPair(topSide);
            this.leftSide = stringToPair(leftSide.toString());
            this.bottomSide = stringToPair(bottomSide);
            this.rightSide = stringToPair(rightSide.toString());
        }

        private Pair<Long, Long> stringToPair(String sideInfo) {
            StringBuilder stringBuilder = new StringBuilder(sideInfo.replaceAll("#", "1").replaceAll("\\.", "0"));

            long side = Long.parseLong(stringBuilder.toString(), 2);
            long reversedSide = Long.parseLong(stringBuilder.reverse().toString(), 2);

            return new Pair<>(side, reversedSide);
        }
    }
}
