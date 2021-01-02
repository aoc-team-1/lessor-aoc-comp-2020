package dk.lessor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Day24 {

    public static void main(String[] args) throws CloneNotSupportedException {
        new Day24().runner();
    }

    public void runner() {
        List<String> ins = FileUtilsKt.readFile("day_24.txt");
        List<Tile> visited = new ArrayList<>();
        puzzleOne(visited, ins);
        puzzleTwo(visited);
    }

    private void puzzleOne(List<Tile> visited, List<String> ins) {
        for (String s : ins) {
            Tile tile = new Tile();
            tile.navigate(s);
            if (visited.contains(tile)) {
                visited.get(visited.indexOf(tile)).toggleColour();
            } else {
                tile.toggleColour();
                visited.add(tile);
            }
        }
        System.out.println(visited.stream().filter(Tile::isBlack).count());
    }

    private void puzzleTwo(List<Tile> visited) {
        for (int i = 0; i < 100; i++) {
            List<Tile> flippedQueue = new ArrayList<>();

            List<Tile> tmp = new ArrayList<>(visited);
            tmp.forEach(tile -> tile.getNeighbors(visited).stream().filter(neighbor -> !visited.contains(neighbor) && flip(visited, flippedQueue, neighbor)).forEach(visited::add));

            for (int j = 0; j < visited.size(); j++) {
                Tile tile = visited.get(j);
                if (!flippedQueue.contains(tile)) {
                    flip(visited, flippedQueue, tile);
                }
            }
            flippedQueue.forEach(Tile::toggleColour);
        }
        System.out.println(visited.stream().filter(Tile::isBlack).count());
    }

    private boolean flip(List<Tile> visited, List<Tile> flippedQueue, Tile tile) {
        List<Tile> neighbors = tile.getNeighbors(visited);
        long count = neighbors.stream().filter(Tile::isBlack).count();
        if (tile.isBlack() && (count == 0 || count > 2)) {
            flippedQueue.add(tile);
            return true;
        } else if (tile.isWhite() && count == 2) {
            flippedQueue.add(tile);
            return true;
        }
        return false;
    }

    private static class Tile implements Cloneable {
        private int x = 0;
        private int y = 0;
        private Boolean colour = false;

        public Tile() {
        }

        public Tile goE() {
            x += 2;
            return this;
        }

        public Tile goW() {
            x -= 2;
            return this;
        }

        public Tile goNE() {
            y++;
            x++;
            return this;
        }

        public Tile goNW() {
            y++;
            x--;
            return this;
        }

        public Tile goSE() {
            y--;
            x++;
            return this;
        }

        public Tile goSW() {
            y--;
            x--;
            return this;
        }

        public void toggleColour() {
            colour ^= true;
        }

        public boolean isBlack() {
            return colour;
        }

        public boolean isWhite() {
            return !colour;
        }

        public void navigate(String ins) {
            String direction = "";
            while (ins.length() > 0) {
                if (ins.startsWith("e")) {
                    this.goE();
                    direction = "e";
                }
                if (ins.startsWith("w")) {
                    this.goW();
                    direction = "w";
                }
                if (ins.startsWith("ne")) {
                    this.goNE();
                    direction = "ne";
                }
                if (ins.startsWith("nw")) {
                    this.goNW();
                    direction = "nw";
                }
                if (ins.startsWith("se")) {
                    this.goSE();
                    direction = "se";
                }
                if (ins.startsWith("sw")) {
                    this.goSW();
                    direction = "sw";
                }
                ins = ins.replaceFirst(direction, "");
            }
        }

        public List<Tile> getNeighbors(List<Tile> visited) {
            List<Tile> neighbors = new ArrayList<>();
            try {
                neighborTile(visited, Tile::goE, neighbors);
                neighborTile(visited, Tile::goW, neighbors);
                neighborTile(visited, Tile::goNE, neighbors);
                neighborTile(visited, Tile::goNW, neighbors);
                neighborTile(visited, Tile::goSE, neighbors);
                neighborTile(visited, Tile::goSW, neighbors);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return neighbors;
        }

        private void neighborTile(List<Tile> visited, Function<Tile, Tile> function, List<Tile> neighbors) throws CloneNotSupportedException {
            Tile tile = function.apply(((Tile) this.clone()));
            int index = visited.indexOf(tile);
            if (index != -1) {
                neighbors.add(visited.get(index));
            } else {
                tile.colour = false;
                neighbors.add(tile);
            }
        }

        @Override
        public String toString() {
            return "[" + x + "," + y + "]";
        }

        @Override
        public boolean equals(Object t) {
            return t instanceof Tile && this.x == ((Tile) t).x && this.y == ((Tile) t).y;
        }
    }
}
