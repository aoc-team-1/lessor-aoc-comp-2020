package dk.lessor;

import java.util.List;

public class Day17 {
    public static void main(String[] args) {
        new Day17().runner();
    }

    public void runner() {
        puzzleOne();
        puzzleTwo();
    }

    private void puzzleOne() {
        Cube cube = new Cube(FileUtilsKt.readFile("day_17.txt"));
        for (int index = 0; index < 5; index++) {
            cube.runCycle();
        }
        System.out.println(cube.runCycle());
    }

    private void puzzleTwo() {
        CubeFour cube = new CubeFour(FileUtilsKt.readFile("day_17.txt"));
        for (int index = 0; index < 5; index++) {
            cube.runCycle();
        }
        System.out.println(cube.runCycle());
    }

    private static class Cube {

        boolean[][][] cube;

        public Cube(List<String> input) {
            setSize(input);
            for (int y = 0; y < input.size(); y++) {
                char[] line = input.get(y).toCharArray();
                for (int x = 0; x < line.length; x++) {
                    cube[0][y][x] = '#' == line[x];
                }
            }
        }

        private void setSize(List<String> input) {
            setSize(1, input.size(), input.get(0).length());
        }

        private void setSize(int z, int x, int y) {
            cube = new boolean[z][y][x];
        }

        public long runCycle() {
            int zSize = cube.length;
            int ySize = cube[0].length;
            int xSize = cube[0][0].length;

            boolean[][][] cube = new boolean[zSize + 2][ySize +2][xSize +2];

            long count = 0;

            for (int z = - 1; z < zSize + 1; z++) {
                for (int y = - 1; y < ySize + 1; y++) {
                    for (int x = -1; x < xSize +1 ; x++) {
                        boolean newStatus = findNewStatus(x, y, z);
                        if(newStatus){
                            count++;
                        }
                        cube[z+1][y+1][x+1] = newStatus;
                    }
                }
            }
            this.cube = cube;
            return count;
        }

        public boolean findNewStatus(int x, int y, int z) {
            int countActives = 0;
            boolean currentStatus = getCurrentStatus(x, y, z);

            for (int i =  z- 1; i <= z + 1; i++) {
                int searchZ = i;
                if (searchZ < 0 || searchZ >= cube.length) {
                    continue;
                }
                for (int j = y-1; j <= y + 1; j++) {
                    int searchY = j;
                    if (searchY < 0 || searchY >= cube[searchZ].length) {
                        continue;
                    }

                    for (int k = x-1; k <= x + 1; k++) {
                        int searchX = k;
                        if (searchX < 0 || searchX >= cube[searchZ][searchY].length) {
                            continue;
                        }
                        if(searchZ == z && searchY == y && searchX == x){
                            continue;
                        }
                        if (cube[searchZ][searchY][searchX]) {
                            countActives++;
                            if (countActives > 3) {
                                return false;
                            }
                        }
                    }
                }
            }
            boolean countThree = countActives == 3;
            boolean activeAndCountTwo = countActives == 2 && currentStatus;
            return countThree || activeAndCountTwo;
        }

        private boolean getCurrentStatus(int x, int y, int z) {
            if (cube.length - 1 >= z && z >= 0 && cube[z].length - 1  >= y && y >= 0 && cube[z][y].length -1  >= x && x >= 0) {
                return cube[z][y][x];
            } else {
                return false;
            }
        }
    }

    private static class CubeFour {

        boolean[][][][] cube;

        public CubeFour(List<String> input) {
            setSize(input);
            for (int y = 0; y < input.size(); y++) {
                char[] line = input.get(y).toCharArray();
                for (int x = 0; x < line.length; x++) {
                    cube[0][0][y][x] = '#' == line[x];
                }
            }
        }

        private void setSize(List<String> input) {
            setSize(1,1, input.size(), input.get(0).length());
        }

        private void setSize(int w, int z, int x, int y) {
            cube = new boolean[w][z][y][x];
        }

        public long runCycle() {
            int wSize = cube.length;
            int zSize = cube[0].length;
            int ySize = cube[0][0].length;
            int xSize = cube[0][0][0].length;

            boolean[][][][] cube = new boolean[wSize+2][zSize + 2][ySize +2][xSize +2];

            long count = 0;
            for (int w = - 1; w < zSize + 1; w++) {
                for (int z = -1; z < zSize + 1; z++) {
                    for (int y = -1; y < ySize + 1; y++) {
                        for (int x = -1; x < xSize + 1; x++) {
                            boolean newStatus = findNewStatus(w, x, y, z);
                            if (newStatus) {
                                count++;
                            }
                            cube[w + 1][z + 1][y + 1][x + 1] = newStatus;
                        }
                    }
                }
            }
            this.cube = cube;
            return count;
        }

        public boolean findNewStatus(int w, int x, int y, int z) {
            int countActives = 0;
            boolean currentStatus = getCurrentStatus(w, x, y, z);
            for (int l =  w- 1; l <= w + 1; l++) {
                int searchW = l;
                if (searchW < 0 || searchW >= cube.length) {
                    continue;
                }
                for (int i = z - 1; i <= z + 1; i++) {
                    int searchZ = i;
                    if (searchZ < 0 || searchZ >= cube[searchW].length) {
                        continue;
                    }
                    for (int j = y - 1; j <= y + 1; j++) {
                        int searchY = j;
                        if (searchY < 0 || searchY >= cube[searchW][searchZ].length) {
                            continue;
                        }

                        for (int k = x - 1; k <= x + 1; k++) {
                            int searchX = k;
                            if (searchX < 0 || searchX >= cube[searchW][searchZ][searchY].length) {
                                continue;
                            }
                            if (searchW == w && searchZ == z && searchY == y && searchX == x) {
                                continue;
                            }
                            if (cube[searchW][searchZ][searchY][searchX]) {
                                countActives++;
                                if (countActives > 3) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
            boolean countThree = countActives == 3;
            boolean activeAndCountTwo = countActives == 2 && currentStatus;
            return countThree || activeAndCountTwo;
        }

        private boolean getCurrentStatus(int w, int x, int y, int z) {
            if (cube.length - 1 >= w && w >= 0 &&
                    cube[w].length - 1  >= z && z >= 0 &&
                    cube[w][z].length -1  >= y && y >= 0 &&
                    cube[w][z][y].length -1  >= x && x >= 0) {
                return cube[w][z][y][x];
            } else {
                return false;
            }
        }
    }
}
