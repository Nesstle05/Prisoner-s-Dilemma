import java.awt.*;
import java.util.Random;
import java.util.Scanner;
import javax.swing.JPanel;


/**
 * Playingfield.
 * 
 * INCOMPLETE
 * Assignment 6 -- Prisoner's Dilemma -- 2ip90
 * part PlayingField
 * 
 * assignment copyright Kees Huizing
 * 
 * @author Vanessa Cupsan
 * @id 1824139
 * @author Meher Shroff
 * @id 1785680
 */
class PlayingField extends JPanel /* possible implements ... */ {
    public int gridLength  = 50;
    public int gridHeight = 50;
    Scanner scanner  = new Scanner(System.in);
    private Patch[][] grid = new Patch[gridLength][gridHeight]; 
    //we create the grid of patches

    private double alpha = 1; // defection award factor
    private boolean updateRule = false;

    // random number generator
    private static final long SEED = 50; // seed for random number generator; any number goes
    public static final Random RANDOM = new Random(SEED);

    /**
     * A getter method for the length of the grid.
     * @return the length of the grid
     */

    int getGridLength() {
        return this.gridLength;
    }
    /**
     * A getter method for the height of the grid.
     * @return the height of the grid.
     */

    public int getGridHeight() {
        return this.gridHeight;
    }

    //we create the grid of patches
    /**
     * This method fills the grid with patches.
     */

    public void gridWithPatches() {
        for (int i = 0; i < gridLength; i++) {
            for (int j = 0; j < gridHeight; j++) {
                grid[i][j] = new Patch();
            }
        }
    }

    /**
     * randomizes the strategy of patches.
     */

    public void randomPatchGrid() {
        for (int i = 0; i < gridLength; i++) {
            for (int j = 0; j < gridHeight; j++) {
                grid[i][j].setCooperating(RANDOM.nextBoolean());
            }
        }
    }
    /**
     * This is a getter method for the a patch inside the grid.
     * @param row the row on which the patch is located
     * @param col the column on which the patch is located
     * @return the current patch
     */

    public Patch getPatch(int row, int col) {
        return grid[row][col];
    }
    /**
     * This is a setter method for the defect award factor alpha.
     * @param alpha the defect award factor
     */

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    /**
     * Alpha of this playing field..
     * 
     * @return alpha value for this field.
     */
    public double getAlpha() {
        return this.alpha; // CHANGE THIS
    }
    /**
     * Calculate and execute one step in the simulation.
     */

    public void step() {
        // setting color back to original if patch just changed
        for (int i = 0; i < gridLength; i++) {
            for (int j = 0; j < gridHeight; j++) {
                if (grid[i][j].getJustChanged()) {
                    grid[i][j].setCooperating(grid[i][j].isCooperating());
                }
            }
        }

        //we reset the score for every patch in the grid
        for (int i = 0; i < gridLength; i++) {
            for (int j = 0; j < gridHeight; j++) {
                grid[i][j].resetScore();
            }
        }

        calculateScore();

        //we determine the next strategy for every patch
        for (int i = 0; i < gridLength; i++) {           
            for (int j = 0; j < gridHeight; j++) {               
                nextStrategy(i, j);            
            }        
        }

        //if it's time to change startegy then the patch changes color
        //we set the variable which indicates whether it's time for a strategy change back to false
        for (int i = 0; i < gridLength; i++) {
            for (int j = 0; j < gridHeight; j++) {
                if (grid[i][j].getChangeStrategy()) {
                    grid[i][j].toggleStrategy();
                    grid[i][j].setChangeStrategy(false);
                }
            }
        }
    }

    /**
     * This method determines the next strategy of a player
     *  based on the highest score of it's neighbors.
     * The rule is that the patch gets the color of neighbor with the highest score
     * @param x the raw on which the current patch is situated
     * @param y the column on which the current patch is situated
     */

    private void nextStrategy(int x, int y) {
        double highestScore = 0;
        Patch bestPatch = grid[x][y]; // bestPatch is the patch with highest score
        //we set the current patch as the patch with the highest score
        for (Patch neighbor : grid[x][y].neighbors) {
            //we update the highest score and the patch with the highest score 
            //every time we find a player which has a score higher than the current best score.
            if (neighbor.getScore() > highestScore) {
                bestPatch = neighbor;
                highestScore = neighbor.getScore();
            }

            //if we find a neighbor whose score is equal to the highest score,
            //then the patch with the highest score becomes
            // 1.one of the neighbors with the highest score (the regular rule)
            // 2.that neighbor (the update rule)
            if (neighbor.getScore() == highestScore && updateRule) {
                bestPatch = neighbor;
            } else if (neighbor.getScore() == highestScore) {
                if (RANDOM.nextBoolean()) {
                    bestPatch = neighbor;
                }
            }
            
        }
        //the current patch should adopt the startegy of the neighbor with the highest score
        if (grid[x][y].isCooperating() == bestPatch.isCooperating()) {
            grid[x][y].setChangeStrategy(false);
        } else {
            grid[x][y].setChangeStrategy(true);
        }
    }
    /**
     * This method calculates the score for each of the patches in the grid.
     */

    private void calculateScore() {
        for (int i = 0; i < gridLength; i++) {
            for (int j = 0; j < gridHeight; j++) {
                //if the current player is cooperating 
                //then its score is the number of neighbors that also cooperated
                if (grid[i][j].isCooperating()) {
                    for (Patch neighbour : grid[i][j].neighbors) {
                        if (neighbour.isCooperating() && (neighbour != grid[i][j])) {
                            grid[i][j].scoreIncrement(1.0);
                        }
                    }
                //if the current player is defecting 
                //then its score is the product of the award factor alpha and
                //the number of neighbors that cooperate
                } else {
                    for (Patch neighbour : grid[i][j].neighbors) {
                        if (neighbour.isCooperating() && (neighbour != grid[i][j])) {
                            grid[i][j].scoreIncrement(alpha);
                        }
                    }
                }
            }
        }
    }
    /**
     * This method adds the neighbors of a given patch to the array of neighbors.
     * @param row the row on which the patch is located
     * @param col the column on which the patch is located
     */

    private void patchNeighbors(int row, int col) {
        int neighborRow;
        int neighborCol;
        int i = 0;
        for (int x = row - 1; x <= row + 1; x++) {
            for (int y = col - 1; y <= col + 1; y++) {
                neighborRow = x;
                neighborCol = y;
                // The wrapping around of the grid
                if (x < 0) {
                    neighborRow = gridLength - 1;
                }
                if (y < 0) {
                    neighborCol = gridHeight - 1;
                }
                if (x >= gridLength) {
                    neighborRow = 0;
                }
                if (y >= gridHeight) {
                    neighborCol = 0;
                }
                grid[row][col].neighbors[i] = grid[neighborRow][neighborCol];
                i++;
            }
        }
    }
    /**
     * This method determines the 8 neighbors for every patch in the grid.
     */

    public void neighbors() {
        for (int i = 0; i < gridLength; i++) {
            for (int j = 0; j < gridHeight; j++) {
                patchNeighbors(i, j);
            }
        }
    }

    /**
     * Grid as 2D boolean array.
     * 
     * Precondition: Grid is rectangular, has a non-zero size, and all elements are
     * non-null.
     * 
     * @return 2D boolean array, with true for cooperators and false for defectors
     */
    public boolean[][] getGrid() {
        boolean[][] resultGrid = new boolean[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                resultGrid[i][j] = grid[i][j].isCooperating();
            }
        }

        return resultGrid;
    }

    /**
     * Set this fields grid.
     * 
     * All patches in the grid become cooperating is the corresponding item in
     * inGrid is true.
     * 
     * @param inGrid 2D array, with true for cooperators and false for defectors.
     */
    public void setGrid(boolean[][] inGrid) {
        for (int i = 0; i < inGrid.length; i++) {
            for (int j = 0; j < inGrid[0].length; j++) {
                grid[i][j].setCooperating(inGrid[i][j]);
            }
        }
    }
    /**
     * A setter method for the update rule.
     * @param x indicates whether we should apply the update rule or not
     */

    public void setUpdateRule(boolean x) {
        this.updateRule = x;
    }
    /**
     * A getter method for the update rule.
     * @return whether we should apply the update rule or not
     */

    public boolean getUpdateRule() {
        return this.updateRule;
    }
}