/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import grid.*;
import java.util.*;


/**
 * Dancing links solver for standard Sudoku.
 */
public class DancingLinksSolver extends StdSudokuSolver
{
    public List<DancingNode> answer = new LinkedList<DancingNode>();
    public List<DancingNode> result ;
    public ColumnNode header;
    int size = 0;
    int[] validValues;

    public DancingLinksSolver() {
    } // end of DancingLinksSolver()

    @Override
    public boolean solve(SudokuGrid grid) {
        if(grid instanceof StdSudokuGrid){
            StdSudokuGrid stdgrid = (StdSudokuGrid) grid;
            size = stdgrid.size;
            this.validValues = stdgrid.validValues;
            int[][] coverMatrix = stdgrid.gridToECM();
            this.result = new LinkedList<>(answer);
            header = createDancingList(coverMatrix);
            algoX(0);
            int[][] outputGrid = convertDLListToGrid(result);


            //  initial grid with the solution grid
            for(int row = 0; row<size; row++){
                for(int col = 0; col<size; col++){
                    if(stdgrid.valueOfCell(row, col) == 0 && outputGrid[row][col] == 0)
                        return false;
                       else if (stdgrid.valueOfCell(row, col) == 0)
                            stdgrid.AssignCellValue(row, col, outputGrid[row][col]);

                }
            }
            return true;
        }
        return false;
    } // end of solve()

    //Creating the grid of DancingNodes and ColumnNodes
    private ColumnNode createDancingList(int[][] ecm) {
        final int num_col = ecm[0].length;
        ColumnNode headerNode = new ColumnNode("header");
        List<ColumnNode> columnNodes = new ArrayList<>();

        // Create Column nodes
        for (int i = 0; i < num_col; i++) {
            ColumnNode n = new ColumnNode(i + "");
            columnNodes.add(n);
            headerNode = (ColumnNode) headerNode.linkRight(n);
        }

        headerNode = headerNode.right.column;
        
        // Create Dancing nodes
        for (int[] aGrid : ecm) {
            DancingNode prev = null;
            for (int j = 0; j < num_col; j++) {
                if (aGrid[j] == 1) {
                    ColumnNode col = columnNodes.get(j);
                    DancingNode newNode = new DancingNode(col);

                    if (prev == null)
                        prev = newNode;

                    col.top.linkDown(newNode);
                    prev = prev.linkRight(newNode);
                    col.size++;
                }
            }
        }
        headerNode.size = num_col;
        return headerNode;
    }

    //search to find column which has the minimum number of dancing nodes
    private ColumnNode selectColumnNodeHeuristic() {
        int min = Integer.MAX_VALUE;
        ColumnNode ret = null;
        for (ColumnNode c = (ColumnNode) header.right; c != header; c = (ColumnNode) c.right) {
            if (c.size < min) {
                min = c.size;
                ret = c;
            }
        }
        return ret;
    }

    //Algorithm x for Dancing links
    private void algoX(int k) {
        if (header.right == header) {// End of Algorithm X
            result = new LinkedList<>(answer);
            return;
        } else {
            ColumnNode c = selectColumnNodeHeuristic();
            c.cover();

            for (DancingNode r = c.bottom; r != c; r = r.bottom) {
                // Add r line to partial solution
                if(r !=null){
                    answer.add(r);

                    // Cover columns
                    for (DancingNode j = r.right; j != r; j = j.right) {
                        j.column.cover();
                    }

                    // recursive call to level k + 1
                    algoX(k + 1);

                    // We go back
                    r = answer.remove(answer.size() - 1);
                    c = r.column;

                    // We uncover columns
                    for (DancingNode j = r.left; j != r; j = j.left) {
                        j.column.uncover();
                    }
                }
            }

            c.uncover();
        }
    }

    public int[][] convertDLListToGrid(List<DancingNode> answer) {
        int[][] outputgrid = new int[size][size];
       //answer = new LinkedList<DancingNode>(n);


        for (DancingNode n : answer) {
            //this.answer = new LinkedList<DancingNode>();
            DancingNode rcNode = n;
            int min = Integer.parseInt(rcNode.column.name);

            for (DancingNode tmp = n.right; tmp != n; tmp = tmp.right) {
                int val = Integer.parseInt(tmp.column.name);

                if (val < min) {
                    min = val;
                    rcNode = tmp;
                }
            }
            // we get line and column
            int ans1 = Integer.parseInt(rcNode.column.name);
            int ans2 = Integer.parseInt(rcNode.right.column.name);
            int r = ans1 / size;
            int c = ans1 % size;
            // and the affected value
            int num = validValues[ans2 % size];
            // we affect that on the result grid
            outputgrid[r][c] = num;
        }
        return outputgrid;
    }


} // end of class DancingLinksSolver

class DancingNode {
    public DancingNode left, right, top, bottom;
    public ColumnNode column;

    public DancingNode() {left = right = top = bottom = this;}

    public DancingNode(ColumnNode c) {
        this();
        column = c;
    }

    public DancingNode linkDown(DancingNode node) {
        node.bottom = bottom;
        node.bottom.top = node;
        node.top = this;
        bottom = node;
        return node;
    }

    public DancingNode linkRight(DancingNode node) {
        node.right = right;
        node.right.left = node;
        node.left = this;
        right = node;
        return node;
    }

    public void removeLeftRight() {
        left.right = right;
        right.left = left;
    }

    public void reinsertLeftRight() {
        left.right = this;
        right.left = this;
    }

    public void removeTopBottom() {
        top.bottom = bottom;
        bottom.top = top;
    }

    public void reinsertTopBottom() {
        top.bottom = this;
        bottom.top = this;
    }
}


class ColumnNode extends DancingNode {

    public int size;
    public String name;

    public ColumnNode(String n) {
        super();
        size = 0;
        name = n;
        column = this;
    }

    public void cover() {
        removeLeftRight();
        for (DancingNode i = bottom; i != this; i = i.bottom) {
            for (DancingNode j = i.right; j != i; j = j.right) {
                j.removeTopBottom();
                j.column.size--;
            }
        }
    }

    public void uncover() {
        for (DancingNode i = top; i != this; i = i.top) {
            for (DancingNode j = i.left; j != i; j = j.left) {
                j.column.size++;
                j.reinsertTopBottom();
            }
        }
        reinsertLeftRight();
    }
}
