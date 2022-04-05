/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import grid.StdSudokuGrid;
import grid.SudokuGrid;


/**
 * Backtracking solver for standard Sudoku.
 */
public class BackTrackingSolver extends StdSudokuSolver
{


    public BackTrackingSolver() {

    } // end of BackTrackingSolver()

    @Override
    public boolean solve(SudokuGrid grid) {
        if(grid instanceof StdSudokuGrid){
            StdSudokuGrid stdgrid = (StdSudokuGrid) grid;
            for (int row = 0; row < stdgrid.size; row++) {
                for (int col = 0; col < stdgrid.size; col++) {
                    //  search an empty cell
                    if (stdgrid.valueOfCell(row,col) == 0) {
                        //  try all valid numbers
                        for(int number : stdgrid.validValues){
                            if (stdgrid.validateAssignedValues(row,col,number)) {
                                stdgrid.AssignCellValue(row,col,number);

                                if (solve(stdgrid)) { //  backtracking recursively
                                    return true;
                                } else { // if not a correct solution, empty the cell and continue
                                    stdgrid.AssignCellValue(row,col,0);
                                }
                            }
                        }
                        return false;
                    }
                }
            }

        }
        return true;
    } // end of solve()

} // end of class BackTrackingSolver()
