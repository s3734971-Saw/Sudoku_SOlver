/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package solver;

import grid.StdSudokuGrid;
import grid.SudokuGrid;

import java.util.LinkedList;
import java.util.List;


/**
 * Algorithm X solver for standard Sudoku.
 */
public class AlgorXSolver extends StdSudokuSolver
{
//    public List<DancingNode> answer;
//    public List<DancingNode> result;
//    public ColumnNode header;
//    int size = 0;
//    int[] validValues;

    public AlgorXSolver() {

    } // end of AlgorXSolver()


    @Override
    public boolean solve(SudokuGrid grid) {
//        if(grid instanceof StdSudokuGrid){
//            StdSudokuGrid stdgrid = (StdSudokuGrid) grid;
//            size = stdgrid.size;
//            this.validValues = stdgrid.validValues;
//            int[][] cover = stdgrid.gridToECM();
//            this.answer = new LinkedList<DancingNode>();
////            header = createDLList(cover);
////            algoX(0);
////            int[][] solved_grid = convertDLListToGrid(result);
//
//            // Merge the initial grid with the solution grid
//            for(int row = 0; row<size; row++){
//                for(int col = 0; col<size; col++){
//                    if(stdgrid.cellValue(row, col)==0)
//                    //    stdgrid.AssignCellValue(row, col, solved_grid[row][col]);
//                }
//            }
//            return true;
//        }
//        return false;
//
//       if(grid instanceof StdSudokuGrid) {
//            StdSudokuGrid stdgrid = (StdSudokuGrid) grid;
//           if (header.right == header) {// End of Algorithm X
//               result = new LinkedList<>(answer);
//               return;
//           } else {
//               ColumnNode c = selectColumnNodeHeuristic();
//               c.cover();
//
//               for (DancingNode r = c.bottom; r != c; r = r.bottom) {
//                   // Add r line to partial solution
//                   if(r !=null){
//                       answer.add(r);
//
//                       // Cover columns
//                       for (DancingNode j = r.right; j != r; j = j.right) {
//                           j.column.cover();
//                       }
//
//                       // recursive call to level k + 1
//                       algoX(k + 1);
//
//                       // We go back
//                       r = answer.remove(answer.size() - 1);
//                       c = r.column;
//
//                       // We uncover columns
//                       for (DancingNode j = r.left; j != r; j = j.left) {
//                           j.column.uncover();
//                       }
//                   }
//               }
//
//               c.uncover();
//           }
//       }
//
        return false;
    }
    // end of solve()
//
//
////    public selectColumnNodeHeuristic() {
////        int min = Integer.MAX_VALUE;
////        ColumnNode ret = null;
////        for (ColumnNode c = (ColumnNode) header.right; c != header; c = (ColumnNode) c.right) {
////            if (c.size < min) {
////                min = c.size;
////                ret = c;
////            }
////        }
////        return ret;
////    }
//
} // end of class AlgorXSolver
