/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package solver;

import grid.KillerSudokuGrid;
import grid.SudokuGrid;
import java.util.ArrayList;
import java.util.List;

/**
 * Backtracking solver for Killer Sudoku.
 */
public class KillerBackTrackingSolver extends KillerSudokuSolver {

    private int[][] inputGrid;
    private int gridSize;
    List<KillerSudokuGrid.Cage> cageList = new ArrayList<>();
    int sqrt;

    public KillerBackTrackingSolver() {

    } // end of KillerBackTrackingSolver()

    @Override
    public boolean solve(SudokuGrid grid) {
        if (grid instanceof KillerSudokuGrid) {
            KillerSudokuGrid killersudoku = (KillerSudokuGrid) grid;
            this.cageList = killersudoku.getCageList();
            this.inputGrid = killersudoku.getGrid();
            this.gridSize = killersudoku.getGridSize();
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    //  searching an empty cell
                    int EMPTY = 0;
                    if (inputGrid[i][j] == EMPTY) {
                        //  trying possible numbers
                        for (int k : killersudoku.validNumbers) {
                            if (validateAssignedValues(i, j, k)) {
                                inputGrid[i][j] = k;
                                if (solve(killersudoku)) { // backtracking recursively
                                    return true;
                                } else {  // if not a correct solution, empty the cell and continue
                                    inputGrid[i][j] = EMPTY;
                                }
                            }
                        }
                        return false;
                    }
                }
            }

        }
        return true;

    }// end of solve()

    public boolean validateAssignedValues(int row, int col, int input) {

        // check if a possible number is already in a row
        for (int i = 0; i < gridSize; i++)
            if (inputGrid[row][i] == input)
                return false;

        // check if a possible number is already in a column
        for (int i = 0; i < gridSize; i++)
            if (inputGrid[i][col] == input)
                return false;

        this.sqrt = (int) Math.sqrt(gridSize);
        // check if a possible number is in box
        int r = row - row % sqrt;
        int c = col - col % sqrt;

        for (int i = r; i < r + sqrt; i++)
            for (int j = c; j < c + sqrt; j++)
                if (inputGrid[i][j] == input)
                    return false;

        if (isIncage(row, col)) {

            if (isEmptyvaluesIncage(row, col)) {
                return getCageCurrentTotal(row, col) < getCageTotal(row, col);
            } else {
                return getCageCurrentTotal(row, col) + input == getCageTotal(row, col);
            }

        }

        return true;
    }// end of validate()



    private int getCageTotal(int row, int column) {

        for (KillerSudokuGrid.Cage cage : cageList) {
            for (int j = 0; j < cage.getIndexList().size(); j++) {
                if (cage.getIndexList().get(j).getX() == row && cage.getIndexList().get(j).getY() == column) {
                    return cage.getTotal();
                }
            }
        }

        return 0;
    }

    private boolean isIncage(int row, int column) {
        for (KillerSudokuGrid.Cage cage : cageList) {
            for (int j = 0; j < cage.getIndexList().size(); j++) {
                if (cage.getIndexList().get(j).getX() == row && cage.getIndexList().get(j).getY() == column) {
                    return true;
                }
            }
        }
        return false;
    }

    private int getCageCurrentTotal(int row, int column) {
        int tot = 0;
        for (KillerSudokuGrid.Cage cage : cageList) {
            for (int j = 0; j < cage.getIndexList().size(); j++) {
                if (cage.getIndexList().get(j).getX() == row && cage.getIndexList().get(j).getY() == column) {

                    for (int k = 0; k < cage.getIndexList().size(); k++) {
                        tot += inputGrid[cage.getIndexList().get(k).getX()][cage.getIndexList().get(k).getY()];
                    }
                }
            }
        }

        return tot;
    }

    private boolean isEmptyvaluesIncage(int row, int column) {
        for (KillerSudokuGrid.Cage cage : cageList) {
            for (int j = 0; j < cage.getIndexList().size(); j++) {
                if (cage.getIndexList().get(j).getX() == row && cage.getIndexList().get(j).getY() == column) {

                    for (int k = 0; k < cage.getIndexList().size(); k++) {

                        if (k == cage.getIndexList().size() - 1) {
                            return false;
                        }

                        if (inputGrid[cage.getIndexList().get(k).getX()][cage.getIndexList().get(k).getY()] == 0) {

                            return true;

                        }
                    }
                }
            }
        }
        return false;
    }

} // end of class KillerBackTrackingSolver()
