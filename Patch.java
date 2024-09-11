import java.awt.*;
/**
 * Patch.
 * 
 * INCOMPLETE 
 * Assignment 6 -- Prisoner's Dilemma -- 2ip90
 * part Patch
 * assignment copyright Kees Huizing
 * 
 * @author Vanessa Cupsan
 * @id 1824139
 * @author Meher Shroff
 * @id 1785680
 */

class Patch extends Button {

    // boolean variable which shows whether a player needs to change strategy or not
    private boolean changeColor;
    //boolean variable which shows whether a player is cooperating or not
    private boolean cooperates;
    // boolean to know if patch should adopt the original strategy next round
    private boolean justChanged; 
    private double interactionScore;
    Patch[] neighbors = new Patch[9]; //the array for the player's neighbors

    /**
     * Determine if this patch is cooperating.
     * 
     * @return true if and only if the patch is cooperating.
     */

    boolean isCooperating() {
        return this.cooperates;
    }
    
    /**
     * Set strategy to cooperate (C) when isC is true, D otherwise.
     * 
     * @param isC use cooperation strategy.
     */
    void setCooperating(boolean isC) {
        //we set the color of the patch to red if the player is defecting
        //otherwise we set it to blue
        this.cooperates = isC;
        if (this.cooperates) { 
            this.setBackground(Color.blue);
        } else {
            this.setBackground(Color.red);
        }
    }
    
    /**
     * Toggle strategy between C and D.
     */
    void toggleStrategy() {
        //if the player is cooperating we set the color to orange
        //in order to show that the player just changed strategy and is now defecting
        // otherwise the patch becomes light blue (the explanation as above)
        if (this.isCooperating()) {
            this.setBackground(Color.orange);
        } else {
            this.setBackground(Color.cyan);
        }
        this.cooperates = !cooperates; //the player changed strategy
        this.justChanged = true;
    }
    
    /**
     * Score of this patch in the current round.
     * 
     * @return the score
     */
    double getScore() {
        return this.interactionScore;
    }

    /**
     * Calculates the new score of the patch.
     * @param score the previous score of the patch
     */

    void scoreIncrement(double score) {
        this.interactionScore = this.interactionScore + score;
    }

    /**
     * resets the score of the patch.
     */

    void resetScore() {
        this.interactionScore = 0.0;
    }

    /**
     * Sets the state of changeStrategy.
     * @param x indicates whether a patch should change strategy or not
     */

    void setChangeStrategy(boolean x) {
        this.changeColor = x;
    }

    /**
     * Gets the state of changeStrategy.
     * @return whether the player should change startegy or not
     */
    boolean getChangeStrategy() {
        return this.changeColor;
    }

    /**
     * Gets the state of justChanged.
     * @return whether a player has just changed startegy or not
     */
    public boolean getJustChanged() {
        return this.justChanged;
    }
   
   
}