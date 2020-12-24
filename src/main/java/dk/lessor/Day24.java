package dk.lessor;

import java.util.ArrayList;
import java.util.List;

public class Day24 {

    public static void main(String[] args) {
        new Day24().runner();
    }

    public void runner() {
        puzzleOne();
//        puzzleTwo();
    }

    private void puzzleOne() {
        List<String> ins = FileUtilsKt.readFile("day_24.txt");
        List<Tile> visited = new ArrayList<>();
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
        for (Tile t : visited) {
            System.out.println(t.toString());
        }
        System.out.println(visited.stream().filter(Tile::isBlack).count());
    }

    private class Tile {
        private int x = 0;
        private int y = 0;
        private Boolean colour = false;

        public Tile() { }

        public void goE() {
            x += 2;
        }

        public void goW() {
            x -= 2;
        }

        public void goNE() {
            y++;
            x++;
        }

        public void goNW() {
            y++;
            x--;
        }

        public void goSE() {
            y--;
            x++;
        }

        public void goSW() {
            y--;
            x--;
        }

        public void toggleColour() {
            colour ^= true;
        }

        public boolean getColour() { return colour; }

        public boolean isBlack() {
            return colour;
        }

        public boolean isWhite() {
            return !colour;
        }

        public Tile navigate(String ins) {
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
            return this;
        }

        public List<Tile> getNeighbors() throws CloneNotSupportedException {
            Tile e = ((Tile) this.clone());
            Tile w = ((Tile) this.clone());
            Tile ne = ((Tile) this.clone());
            Tile nw = ((Tile) this.clone());
            Tile se = ((Tile) this.clone());
            Tile sw = ((Tile) this.clone());
            e.goE();
            w.goW();
            ne.goNE();
            nw.goNW();
            se.goSE();
            sw.goSW();
            return List.of(e, w, ne, nw, se, sw);
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
