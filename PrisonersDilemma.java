import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Prisoners Dilemma application.
 * 
 * assignment copyright Kees Huizing
 * 
 * @author Vanessa Cupsan
 * @id 1824139
 * @author Meher Shroff
 * @id 1785680
 */
class PrisonersDilemma /* possible extends... */ {
    // ...
    private int timerDelay = 1000;

    /**
     * Build the GUI for the Prisoner's Dilemma application.
     */
    void buildGUI() {
        SwingUtilities.invokeLater(() -> {
            // we create and customize the frame
            JFrame frame = new JFrame("Prisoner's Dilemma");
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(700, 700);
            frame.setResizable(false);
            frame.setLayout(new BorderLayout());

            // we create and customize the playing field
            PlayingField playingField = new PlayingField();
            playingField.gridWithPatches();
            playingField.setLayout(new GridLayout(playingField.getGridLength(),
                    playingField.getGridHeight()));
            playingField.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            playingField.randomPatchGrid();
            playingField.setVisible(true);

            playingField.neighbors(); // we determine the neighbors for every player in the field

            for (int i = 0; i < playingField.getGridLength(); i++) {
                for (int j = 0; j < playingField.getGridHeight(); j++) {
                    Patch patch;
                    patch = playingField.getPatch(i, j);
                    // clicking on the patch changes the color
                    patch.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            patch.toggleStrategy();
                        }
                    });
                    playingField.add(playingField.getPatch(i, j));
                }
            }
            frame.add(playingField, BorderLayout.CENTER); // we add the playing field on the frame

            // we create the bottom part of the frame
            // we create and customize the panel on which we will place the buttons and the
            // slider
            JPanel bottomPanel = new JPanel();
            frame.add(bottomPanel, BorderLayout.SOUTH);
            bottomPanel.setLayout(new GridLayout(4, 2));
            bottomPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            // We create a label for the alpha reward factor and add it to the panel
            JLabel rewardLabel = new JLabel("Reward alpha: " + playingField.getAlpha());
            bottomPanel.add(rewardLabel);

            // we create the slider
            JSlider rewardSlider = new JSlider(0, 30, 10);
            rewardSlider.setMajorTickSpacing(1);
            rewardSlider.setPaintTicks(true);
            rewardSlider.setPaintLabels(true);
            //we create a hash table in order to associate arbitrary JComponents
            //to floating point values
            Hashtable labelTable = new Hashtable();
            labelTable.put(0, new JLabel("0.0"));
            labelTable.put(10, new JLabel("1.0"));
            labelTable.put(20, new JLabel("2.0"));
            labelTable.put(30, new JLabel("3.0"));
            rewardSlider.setLabelTable(labelTable);
            //we add a change listener in order to get the value of alpha
            //based on the place we pointed our cursor on the slider
            rewardSlider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    JSlider source = (JSlider) e.getSource();
                    playingField.setAlpha(source.getValue() / 10.0);
                    rewardLabel.setText("Reward alpha: " + playingField.getAlpha());
                }
            });
            //the simulation should still be running even tho we are making changes on the sliders
            ActionListener taskPerformer = new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    playingField.step();
                }
            };
            Timer timer = new Timer(timerDelay, taskPerformer);
            // We create a label for the speed and add it to the bottom panel
            // and also add the slider for the reward factor
            JLabel speedLabel = new JLabel("Speed in milliseconds: " + timerDelay);
            bottomPanel.add(speedLabel);
            bottomPanel.add(rewardSlider);
            // We create a slider for the speed and add it to the panel
            JSlider speedSlider = new JSlider(0, 3000, 1000);
            speedSlider.setMajorTickSpacing(500);
            speedSlider.setPaintTicks(true);
            speedSlider.setPaintLabels(true);
            //we create a change listener in order to modity the speed of the animation
            speedSlider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    JSlider source = (JSlider) e.getSource();
                    timerDelay = source.getValue();
                    timer.setDelay(timerDelay);
                    speedLabel.setText("Speed in milliseconds: " + timerDelay);
                }
            });
            bottomPanel.add(speedSlider);
            
            //we create the buttons

            // We create a button for the update rule and add it to the panel
            JButton ruleButton = new JButton("Update Rule is Off");
            bottomPanel.add(ruleButton);
            ruleButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // if we choose not to change the player's strategy with the highest score,
                    // the extra rule is turned off
                    // Otherwise the extra rule is turned on
                    if (playingField.getUpdateRule()) {
                        playingField.setUpdateRule(false);
                        ruleButton.setText("Update Rule is Off ");
                    } else {
                        playingField.setUpdateRule(true);
                        ruleButton.setText("Update Rule is On");
                    }
                }
            });

            // We create a button for "go" and turn it into a "pause" button once go has
            // been clicked
            JButton goButton = new JButton("Go");
            goButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (timer.isRunning()) {
                        timer.stop();
                    } else {
                        timer.start();
                    }
                    if (goButton.getText().equals("Go")) {
                        goButton.setText("Pause");
                    } else {
                        goButton.setText("Go");
                    }
                }
            });
            // We add the go/pause button to the panel
            bottomPanel.add(goButton);

            // We create a reset button and add it to the panel
            JButton resetButton = new JButton("Reset");
            //we added an action listener
            // in order to get a new, random grid when the reset button is clicked
            resetButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    playingField.randomPatchGrid();
                }
            });
            bottomPanel.add(resetButton);
            
        });
    }

    public static void main(String[] a) {
        new PrisonersDilemma().buildGUI();
    }
}