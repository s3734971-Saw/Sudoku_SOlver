/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package grid;

import java.io.*;
import java.util.Arrays;


/**
 * Class implementing the grid for standard Sudoku.
 * Extends SudokuGrid (hence implements all abstract methods in that abstract
 * class).
 * You will need to complete the implementation for this for task A and
 * subsequently use it to complete the other classes.
 * See the comments in SudokuGrid to understand what each overriden method is
 * aiming to do (and hence what you should aim for in your implementation).
 */
public class StdSudokuGrid extends SudokuGrid {
    public int size;
    public int[] validValues;
    int sqrt;
    public int[][] grid;


    public StdSudokuGrid() {

        super();
        // this.grid = new int[size][size];


    } // end of StdSudokuGrid()

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    /* ********************************************************* */

    @Override
    public void initGrid(String filename)
            throws FileNotFoundException, IOException {
        String line;
        int lineNum = 1;

        String filepath = "sampleGames/";
        BufferedReader Filename = new BufferedReader(new FileReader(filepath + filename));

        while ((line = Filename.readLine()) != null) {


            if (lineNum == 1) {

                String[] tokens = line.split(" ");
                if (tokens.length == 1) {
                    int puzzSize = Integer.parseInt(line);
                    setSize(puzzSize);
                    this.grid = new int[puzzSize][puzzSize];
                    this.sqrt = (int) Math.sqrt(size);
                }
            } else if (lineNum == 2) {
                String[] values = line.split(" ");
                validValues = new int[size];
                for (int i = 0; i < size; i++) {
                    validValues[i] = Integer.parseInt(values[i]);

                }
            } else {

                String[] val = line.replace(",", " ").split(" ");
                int value = Integer.parseInt(val[2]);
                grid[Integer.parseInt(val[0])][Integer.parseInt(val[1])] = value;

            }
            lineNum++;
        }

    }
    // end of initBoard()


    @Override

    public void outputGrid(String filename)
            throws FileNotFoundException, IOException {
        PrintWriter outWriter = new PrintWriter(new FileWriter(filename), true);

        outWriter.println(toString());
        outWriter.close();

    } // end of outputBoard()


    @Override
    public String toString() {
        String outgrid = "";
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (col == size - 1)
                    outgrid = outgrid + String.valueOf(valueOfCell(row, col));
                else
                    outgrid = outgrid + String.valueOf(valueOfCell(row, col)) + ",";
            }
            outgrid = outgrid + "\n";
        }
        return outgrid;
    } // end of toString()


    public int valueOfCell(int row, int col) {
        return grid[row][col];
    }

    public void AssignCellValue(int row, int col, int value) {
        grid[row][col] = value;
    }

    @Override
    public boolean validate() {

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int number = valueOfCell(row, col);

                //  check if a possible number is already in a column
                for (int i = 0; i < col; i++)
                    if (grid[row][i] == number)
                        return false;

                // check if a possible number is already in a column
                for (int i = 0; i < row; i++)
                    if (grid[i][col] == number)
                        return false;

                this.sqrt = (int) Math.sqrt(size);
                //  check if a possible number is in box
                int r = row - row % sqrt;
                int c = col - col % sqrt;

                for (int i = r; i < r + sqrt; i++)
                    for (int j = c; j < c + sqrt; j++)
                        if (i == row && j == col)
                            continue;
                        else if (grid[i][j] == number)
                            return false;
            }
        }
        return true;
    } // end of validate()

    public boolean validateAssignedValues(int row, int col, int number) {

        // check if a possible number is already in a row
        for (int i = 0; i < size; i++)
            if (valueOfCell(row, i) == number)
                return false;

        // check if a possible number is already in a column
        for (int i = 0; i < size; i++)
            if (valueOfCell(i, col) == number)
                return false;

        this.sqrt = (int) Math.sqrt(size);
        // check if a possible number is in box
        int r = row - row % sqrt;
        int c = col - col % sqrt;

        for (int i = r; i < r + sqrt; i++)
            for (int j = c; j < c + sqrt; j++)
                if (valueOfCell(i, j) == number)
                    return false;

        return true;
    } // end of validate()


    //***************************************************************

    // create exact cover matrix
    public int[][] createECM() {
        int[][] ecm = new int[size * size * size][size * size * 4];

        int header = 0;
        header = createCellConstraints(ecm, header);
        header = createRowConstraints(ecm, header);
        header = createColumnConstraints(ecm, header);
        createBoxConstraints(ecm, header);
        return ecm;
    }

        //ECM transformation to the input grid
        public int[][]  gridToECM(){

            int[][] coverMatrix = createECM();

        for (int row = 1; row <= size; row++) {
            for (int column = 1; column <= size; column++) {
                int n = grid[row - 1][column - 1];

                if (n != 0) {
                    for (int i = 0; i < size; i++) {
                        if (validValues[i] != n) {
                            Arrays.fill(coverMatrix[getIndexInECM(row, column, validValues[i])], 0);
                        }
                    }
                }
            }
        }
        return coverMatrix;
    }

    private int createCellConstraints(int[][] matrix, int header) {
        for (int row = 1; row <= size; row++) {
            for (int column = 1; column <= size; column++, header++) {
                for (int n = 1; n <= size; n++) {
                    int index = getIndexInECM(row, column, validValues[n - 1]);
                    matrix[index][header] = 1;
                }
            }
        }
        return header;
    }

    private int createBoxConstraints(int[][] matrix, int header) {
        for (int row = 1; row <= size; row += sqrt) {
            for (int column = 1; column <= size; column += sqrt) {
                for (int n = 1; n <= size; n++, header++) {

                    for (int rowDelta = 0; rowDelta < sqrt; rowDelta++) {
                        for (int columnDelta = 0; columnDelta < sqrt; columnDelta++) {
                            int index = getIndexInECM(row + rowDelta, column + columnDelta, n);
                            matrix[index][header] = 1;
                        }
                    }
                }
            }
        }
        return header;
    }

    private int createColumnConstraints(int[][] matrix, int header) {
        for (int column = 1; column <= size; column++) {
            for (int n = 1; n <= size; n++, header++) {
                for (int row = 1; row <= size; row++) {
                    int index = getIndexInECM(row, column, validValues[n - 1]);
                    matrix[index][header] = 1;
                }
            }
        }
        return header;
    }

    private int createRowConstraints(int[][] matrix, int header) {
        for (int row = 1; row <= size; row++) {
            for (int n = 1; n <= size; n++, header++) {
                for (int column = 1; column <= size; column++) {
                    int index = getIndexInECM(row, column, validValues[n - 1]);
                    matrix[index][header] = 1;
                }
            }
        }
        return header;
    }
    private int getIndexInECM(int row, int column, int num) {
        int numLocation = 0;
        for (int i = 0; i < size; i++) {
            if (validValues[i] == num) {
                numLocation = i;
                break;
            }
        }
        return (row - 1) * size * size + (column - 1) * size + (numLocation);
    }
}//end of class StdSudokuGrid


