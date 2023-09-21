import java.util.Objects;

public class Main {

    public static void main(String[] args) {
        long start = System.nanoTime();
        boolean[] pieces = {false, true, true, false, true, true, true, false, false, true, true, false,
                true, true, false, true, true, false, true, true, true, true, true, true, true, true, true, false, true,
                false, true, true, true, true, true, true, false, true, false, false, true, true, true, true, false, false,
                true, true, false, true, true, true, true, true, true, true, true, true, true, false, false, true};

        int[][][] cube = new int[4][4][4];
        xLoop:
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                for (int z = 0; z < 4; z++) {
                    for (int h = 1; h <= 6; h++) {
                        cube = solveFromStart(x,y,z,h, pieces);
                        System.out.println("Cube " + x + y + z + h + " completed");
                        if (Objects.nonNull(cube)) {
                            break xLoop;
                        }
                    }
                }
            }
        }

        if (Objects.nonNull(cube)) {
            System.out.println("Cube: ");
            for (int y = 3; y >= 0; y--) {
                for (int z = 3; z >= 0; z--) {
                    for (int x = 0; x < 4; x++) {
                        System.out.print(cube[x][y][z] + " ");
                    }
                    System.out.println();
                }
                System.out.println();
            }
        } else {
            System.out.println("No non-null cubes :(");
        }

        System.out.println((System.nanoTime() - start) / 1000000);
//        start = System.nanoTime();
//        long cubeNumber = 0;
//
//        boolean[] testPieces = new boolean[62];
//        cubeNumber++;
//        if (checkIfSolvable(testPieces)) {
//            System.out.println(0);
//        }
//        for (int i = 0; i < 62; i++) {
//            if (testPieces[i]) {
//                testPieces[i] = false;
//            } else {
//                testPieces[i] = true;
//                System.out.println("" + cubeNumber + ": " + checkIfSolvable(testPieces));
//                i = 0;
//                cubeNumber++;
//            }
//        }
//
//        System.out.println((System.nanoTime() - start) / 1000000);
    }

    public static int[][][] solveFromStart(int x, int y, int z, int heading, boolean[] pieces) {
        // -1 = end
        // 0 = empty
        // 1 = left
        // 2 = down
        // 3 = in
        // 4 = right
        // 5 = up
        // 6 = out
        // cube[x (left-right)][y (up-down)][z (in-out)]
        int[][][] cube = new int[4][4][4];
        int[][][] backCube = new int[4][4][4];
        cube[x][y][z] = heading;
        backCube[x][y][z] = -1;
        if (heading == 1 && x > 0) {
            x--;
            backCube[x][y][z] = 4;
        } else if (heading == 2 && y > 0) {
            y--;
            backCube[x][y][z] = 5;
        } else if (heading == 3 && z > 0) {
            z--;
            backCube[x][y][z] = 6;
        } else if (heading == 4 && x < 3) {
            x++;
            backCube[x][y][z] = 1;
        } else if (heading == 5 && y < 3) {
            y++;
            backCube[x][y][z] = 2;
        } else if (heading == 6 && z < 3) {
            z++;
            backCube[x][y][z] = 3;
        } else {
            return null;
        }
        int piece = 0;
        boolean back = false;
        while (piece < pieces.length) {
            if (piece == -1) {
                return null;
            }
            if (pieces[piece]) {
                if (back) {
                    switch (backCube[x][y][z]) {
                        case 1 -> heading = cube[x - 1][y][z];
                        case 2 -> heading = cube[x][y - 1][z];
                        case 3 -> heading = cube[x][y][z - 1];
                        case 4 -> heading = cube[x + 1][y][z];
                        case 5 -> heading = cube[x][y + 1][z];
                        case 6 -> heading = cube[x][y][z + 1];
                    }
                }
                // try to go each direction, but not if it's out of bounds, we've tried going that direction before, it's
                // not possible due to the piece heading, or there's already a piece there
                if (x > 0 && cube[x][y][z] < 1 && heading % 3 != 1) {
                    if (cube[x - 1][y][z] == 0) {
                        cube[x][y][z] = 1;
                        x--;
                        backCube[x][y][z] = 4;
                        heading = 1;
                        back = false;
                        piece++;
                        continue;
                    }
                }
                if (y > 0 && cube[x][y][z] < 2 && heading % 3 != 2) {
                    if (cube[x][y - 1][z] == 0) {
                        cube[x][y][z] = 2;
                        y--;
                        backCube[x][y][z] = 5;
                        heading = 2;
                        back = false;
                        piece++;
                        continue;
                    }
                }
                if (z > 0 && cube[x][y][z] < 3 && heading % 3 != 0) {
                    if (cube[x][y][z - 1] == 0) {
                        cube[x][y][z] = 3;
                        z--;
                        backCube[x][y][z] = 6;
                        heading = 3;
                        back = false;
                        piece++;
                        continue;
                    }
                }
                if (x < 3 && cube[x][y][z] < 4 && heading % 3 != 1) {
                    if (cube[x + 1][y][z] == 0) {
                        cube[x][y][z] = 4;
                        x++;
                        backCube[x][y][z] = 1;
                        heading = 4;
                        back = false;
                        piece++;
                        continue;
                    }
                }
                if (y < 3 && cube[x][y][z] < 5 && heading % 3 != 2) {
                    if (cube[x][y + 1][z] == 0) {
                        cube[x][y][z] = 5;
                        y++;
                        backCube[x][y][z] = 2;
                        heading = 5;
                        back = false;
                        piece++;
                        continue;
                    }
                }
                if (z < 3 && cube[x][y][z] < 6 && heading % 3 != 0) {
                    if (cube[x][y][z + 1] == 0) {
                        cube[x][y][z] = 6;
                        z++;
                        backCube[x][y][z] = 3;
                        heading = 6;
                        back = false;
                        piece++;
                        continue;
                    }
                }
                if (backCube[x][y][z] == 1) {
                    backCube[x][y][z] = 0;
                    cube[x][y][z] = 0;
                    x--;
                } else if (backCube[x][y][z] == 2) {
                    backCube[x][y][z] = 0;
                    cube[x][y][z] = 0;
                    y--;
                } else if (backCube[x][y][z] == 3) {
                    backCube[x][y][z] = 0;
                    cube[x][y][z] = 0;
                    z--;
                } else if (backCube[x][y][z] == 4) {
                    backCube[x][y][z] = 0;
                    cube[x][y][z] = 0;
                    x++;
                } else if (backCube[x][y][z] == 5) {
                    backCube[x][y][z] = 0;
                    cube[x][y][z] = 0;
                    y++;
                } else if (backCube[x][y][z] == 6) {
                    backCube[x][y][z] = 0;
                    cube[x][y][z] = 0;
                    z++;
                }
                back = true;
                piece--;
                continue;
            }
            if (back) {
                if (backCube[x][y][z] == 1) {
                    backCube[x][y][z] = 0;
                    cube[x][y][z] = 0;
                    x--;
                } else if (backCube[x][y][z] == 2) {
                    backCube[x][y][z] = 0;
                    cube[x][y][z] = 0;
                    y--;
                } else if (backCube[x][y][z] == 3) {
                    backCube[x][y][z] = 0;
                    cube[x][y][z] = 0;
                    z--;
                } else if (backCube[x][y][z] == 4) {
                    backCube[x][y][z] = 0;
                    cube[x][y][z] = 0;
                    x++;
                } else if (backCube[x][y][z] == 5) {
                    backCube[x][y][z] = 0;
                    cube[x][y][z] = 0;
                    y++;
                } else if (backCube[x][y][z] == 6) {
                    backCube[x][y][z] = 0;
                    cube[x][y][z] = 0;
                    z++;
                }
                piece--;
                continue;
            }
            if (heading == 1 && x > 0) {
                if (cube[x - 1][y][z] == 0) {
                    cube[x][y][z] = 1;
                    x--;
                    backCube[x][y][z] = 4;
                    piece++;
                    continue;
                }
            }
            if (heading == 2 && y > 0) {
                if (cube[x][y - 1][z] == 0) {
                    cube[x][y][z] = 2;
                    y--;
                    backCube[x][y][z] = 5;
                    piece++;
                    continue;
                }
            }
            if (heading == 3 && z > 0) {
                if (cube[x][y][z - 1] == 0) {
                    cube[x][y][z] = 3;
                    z--;
                    backCube[x][y][z] = 6;
                    piece++;
                    continue;
                }
            }
            if (heading == 4 && x < 3) {
                if (cube[x + 1][y][z] == 0) {
                    cube[x][y][z] = 4;
                    x++;
                    backCube[x][y][z] = 1;
                    piece++;
                    continue;
                }
            }
            if (heading == 5 && y < 3) {
                if (cube[x][y + 1][z] == 0) {
                    cube[x][y][z] = 5;
                    y++;
                    backCube[x][y][z] = 2;
                    piece++;
                    continue;
                }
            }
            if (heading == 6 && z < 3) {
                if (cube[x][y][z + 1] == 0) {
                    cube[x][y][z] = 6;
                    z++;
                    backCube[x][y][z] = 3;
                    piece++;
                    continue;
                }
            }
            if (backCube[x][y][z] == 1) {
                backCube[x][y][z] = 0;
                cube[x][y][z] = 0;
                x--;
            } else if (backCube[x][y][z] == 2) {
                backCube[x][y][z] = 0;
                cube[x][y][z] = 0;
                y--;
            } else if (backCube[x][y][z] == 3) {
                backCube[x][y][z] = 0;
                cube[x][y][z] = 0;
                z--;
            } else if (backCube[x][y][z] == 4) {
                backCube[x][y][z] = 0;
                cube[x][y][z] = 0;
                x++;
            } else if (backCube[x][y][z] == 5) {
                backCube[x][y][z] = 0;
                cube[x][y][z] = 0;
                y++;
            } else if (backCube[x][y][z] == 6) {
                backCube[x][y][z] = 0;
                cube[x][y][z] = 0;
                z++;
            }
            back = true;
            piece--;
        }
        System.out.println("BackCube:");
        for (int by = 3; by >= 0; by--) {
            for (int bz = 3; bz >= 0; bz--) {
                for (int bx = 0; bx < 4; bx++) {
                    System.out.print(backCube[bx][by][bz] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
        return cube;
    }

    public static boolean checkIfSolvable(boolean[] pieces) {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                for (int z = 0; z < 4; z++) {
                    for (int h = 1; h <= 6; h++) {
                        int[][][] cube = solveFromStart(x,y,z,h, pieces);
                        if (Objects.nonNull(cube)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}