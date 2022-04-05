/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package grid;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class implementing the grid for Killer Sudoku. Extends SudokuGrid (hence
 * implements all abstract methods in that abstract class). You will need to
 * complete the implementation for this for task E and subsequently use it to
 * complete the other classes. See the comments in SudokuGrid to understand what
 * each overriden method is aiming to do (and hence what you should aim for in
 * your implementation).
 */
public class KillerSudokuGrid extends SudokuGrid {

    int[][] grid;
    public int gridSize = 0;
    int cageNo = 0;
    public int[] validNumbers;
    List<Cage> cageList = new ArrayList<>();

    public KillerSudokuGrid() {
        super();

    } // end of KillerSudokuGrid()


    /* ********************************************************* */
    @Override
    public void initGrid(String filename)
            throws FileNotFoundException, IOException {
        int count = 1;

        File f = new File("sampleGames/"+filename);
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) {
            if (count == 1) {
                gridSize = Integer.parseInt(line);
                grid = new int[gridSize][gridSize];
            } else if (count == 2) {
                char charNum[] = line.replace(" ", "").toCharArray();

                validNumbers = new int[charNum.length];
                for (int i = 0; i < charNum.length; i++) {
                    validNumbers[i] = Integer.parseInt(String.valueOf(charNum[i]));
                }
            } else if (count == 3) {
                cageNo = Integer.parseInt(line);
            } else {

                String step1[] = line.split(" ");
                int total = Integer.parseInt(step1[0]);
                Cage cage = new Cage();
                cage.setTotal(total);
                List<Index> indexList = new ArrayList<>();
                for (int i = 1; i < step1.length; i++) {
                    Index index = new Index();
                    index.setX(Integer.parseInt(step1[i].split(",")[0]));
                    index.setY(Integer.parseInt(step1[i].split(",")[1]));
                    indexList.add(index);
                }
                cage.setIndexList(indexList);
                cageList.add(cage);

//            }
            }
            count++;
        }
    } // end of initBoard()

    @Override
    public void outputGrid(String filename)
            throws FileNotFoundException, IOException {

        PrintWriter outWriter = new PrintWriter(new FileWriter(filename), true);

        outWriter.println(toString());
        outWriter.close();

    } // end of outputBoard()

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        String separator = ",";
        for (int i = 0; i < gridSize; ++i) {
            for (int j = 0; j < grid[i].length; ++j) {
                if (j == gridSize - 1) {
                    result.append(grid[i][j]);
                } else {
                    result.append(grid[i][j]).append(separator);
                }
            }
            result.append('\n');
        }

        // placeholder
        return String.valueOf(result.toString());
    } // end of toString()


    public boolean validate() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (grid[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    } // end of validate()


    public int[][] getGrid() {
        return grid;
    }


    public int getGridSize() {
        return gridSize;
    }


    public void setGrid(int[][] grid) {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                this.grid[i][j] = grid[i][j];
            }
        }
    }

    public int getCageNo() {
        return cageNo;
    }

    public void setCageNo(int cageNo) {
        this.cageNo = cageNo;
    }

    public int[] getValidNumbers() {
        return validNumbers;
    }

    public void setValidNumbers(int[] validNumbers) {
        this.validNumbers = validNumbers;
    }

    public List<Cage> getCageList() {
        return cageList;
    }
    public class Index {

        private int x;
        private int y;

        public Index(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Index() {
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

    }
    public class Cage {

        private int total;
        private List<Index> indexList;

        public Cage() {
        }

        public Cage(int total, List<Index> indexList) {
            this.total = total;
            this.indexList = indexList;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<Index> getIndexList() {
            return indexList;
        }

        public void setIndexList(List<Index> indexList) {
            this.indexList = indexList;
        }

    }

} // end of class KillerSudokuGrid
