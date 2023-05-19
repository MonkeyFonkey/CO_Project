package GUI;

import Benchmark.Ram.MemoryBandwidthBenchamrk;
import Benchmark.CpuInfo;
import Benchmark.PICalculatorNewtonRaphson;
import Timing.Timing;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import static Benchmark.NetworkInfo.getPCID;
import static GUI.GuiMethods.*;
import static Logging.TimeUnit.*;

public class GUI extends JFrame implements ActionListener {

    private final JTextField digitsTextField;

    private final JLabel cpuUsageLabel;


    private long score = 0;


    public GUI() {
        super("The Bit Busters");


        JLabel digitsLabel = new JLabel("Digits of PI to calculate:");
        digitsTextField = new JTextField(10);
        JButton calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(this);
        digitsTextField.addActionListener(this);
        digitsTextField.setActionCommand("calculate");


        JLabel digitsForCalculateRamLabel = new JLabel("RAM BENCHMARK by measuring the BANDWIDTH");


        JButton calculateRamButton = new JButton("Calculate");
        JButton showPCConfigurationButton = new JButton("Show CPU Configuration");
        JButton average_score_to_document_button = new JButton("Add Average CPU Score to Document");


        JLabel digitsForAvgScoreLabel = new JLabel("Digits used for Average CPU Score to Document: 1000");


        showPCConfigurationButton.addActionListener(e -> {
            // Get CPU information and display in a message dialog
            String cpuInfo = getCPUInformation();
            JOptionPane.showMessageDialog(null, cpuInfo, "CPU Information", JOptionPane.INFORMATION_MESSAGE);
        });


        average_score_to_document_button.addActionListener(e -> {
            CpuInfo cpu = new CpuInfo();


            //Checking if pcID could be retrieved
            String pcID;
            try {
                pcID = getPCID();
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(null, "Error: Could not determine PC ID.");
                return;
            }


            //Checking if CPU model could be retrieved
            String cpuModel;
            try {
                cpuModel = cpu.getCpuModel();
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(null, "Error: Could not determine CPU model.");
                return;
            }



            Timing t = new Timing();
            PICalculatorNewtonRaphson pi = new PICalculatorNewtonRaphson();
            pi.initialize(1000);


            t.start();
            pi.run();
            long totalTime = t.stop();



            //Calculating the score:
            int numCores = Runtime.getRuntime().availableProcessors();

            score = (long)(((pi.getNumDigits()/ toTimeUnit(totalTime, Sec)))/numCores);

            String scoreRecord = pcID + "," + cpuModel + "," + score;


            //Checking if there is a unique pcID
            try {
                if (!isPCIDUnique(pcID)) {
                    JOptionPane.showMessageDialog(null, "Error: Unique PC ID already exists in score file");
                    return;
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }


            try {
                saveScoreToFile(scoreRecord);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Could not save to score file");
            }


        });


        calculateRamButton.addActionListener(e -> {
            MemoryBandwidthBenchamrk mem1 = new MemoryBandwidthBenchamrk();

            // Call the initialize function with the arraySize
            mem1.run();



            // Display the result and the time in a new JFrame
            JFrame resultFrame = new JFrame("Result");
            resultFrame.setSize(400, 250);

            JTextArea resultTextArea = new JTextArea(10, 30);
            resultTextArea.setEditable(false);
            resultTextArea.append("Score: " + mem1.getScore() + "\n");

            JScrollPane scrollPane = new JScrollPane(resultTextArea);

            JButton backButton = new JButton("Back");
            backButton.addActionListener(e12 -> resultFrame.dispose());

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(backButton);

            JPanel resultPanel = new JPanel();
            resultPanel.setLayout(new BorderLayout());
            resultPanel.add(scrollPane, BorderLayout.CENTER);
            resultPanel.add(buttonPanel, BorderLayout.SOUTH);

            resultFrame.add(resultPanel);
            resultFrame.setVisible(true);
        });



        // Add components to content pane
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();



        //adding a cpu usage label
        cpuUsageLabel = new JLabel("CPU Usage: ");
        JPanel cpuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cpuPanel.add(cpuUsageLabel);


        //Adding digitsLabel
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        panel.add(digitsLabel, constraints);

        //Adding digitsTextField
        constraints.gridx = 1;
        panel.add(digitsTextField, constraints);

        //Adding calculate button
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        panel.add(calculateButton, constraints);


        //Adding show PC configuration Button
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        panel.add(showPCConfigurationButton, constraints);


        //added CPU USAGE LABEL
        constraints.gridx = 0;
        constraints.gridy = 7;
        panel.add(cpuUsageLabel, constraints); // Add CPU usage label to the GUI


        //Digits for average score label
        constraints.gridx = 0;
        constraints.gridy = 4;
        panel.add(digitsForAvgScoreLabel, constraints);


        //Button to calculate average score and add it to the document
        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.gridwidth = 2;
        panel.add(average_score_to_document_button, constraints);



        constraints.gridx = 0;
        constraints.gridy = 8;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 5, 5, 5);
        panel.add(digitsForCalculateRamLabel, constraints);


        constraints.gridx = 0;
        constraints.gridy = 9;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        panel.add(calculateRamButton, constraints);




        getContentPane().add(panel);

        //Center the panel in the middle of the screen
        setLocationRelativeTo(null);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);







        // Start a timer to update the CPU usage label every 1 second
        Timer timer = new Timer(1000, e -> {
            double cpuUsage = getCpuUsage();
            DecimalFormat df = new DecimalFormat("#.#");
            cpuUsageLabel.setText("CPU Usage: " + df.format(cpuUsage) + "%");
        });
        timer.start();
    }




    // Get CPU usage as a percentage
    public double getCpuUsage() {
        CpuInfo cpu = new CpuInfo();
        return cpu.getCpuUsage() * 100;
    }


    //get CPU information function
    public String getCPUInformation(){
        CpuInfo cpu = new CpuInfo();
        return "Your PC has: " + cpu.getCpuModel() + " with the architecture: " + cpu.getArchitecture()
                + " and has " + cpu.getAvailableProcessors() + " numbers of processors";
    }


    //action when pressing the "calculate" button
    public void actionPerformed(ActionEvent e) {
        // Get the number of digits to calculate
        int numDigits;
        try {
            numDigits = Integer.parseInt(digitsTextField.getText());


        } catch (NumberFormatException ex) {
            // Display an error message and highlight the text field in red
            JOptionPane.showMessageDialog(this, "Please enter a valid integer", "Error", JOptionPane.ERROR_MESSAGE);
            digitsTextField.setBorder(BorderFactory.createLineBorder(Color.RED));
            return;
        }


        // Check if numDigits is 0
        if (numDigits == 0) {
            // Display an error message and highlight the text field in red
            JOptionPane.showMessageDialog(this, "Number of digits cannot be 0", "Error", JOptionPane.ERROR_MESSAGE);
            digitsTextField.setBorder(BorderFactory.createLineBorder(Color.RED));
            return;
        }


        // Reset the background color of the text field
        digitsTextField.setBorder(BorderFactory.createLineBorder(Color.black));

        Timing t = new Timing();
        PICalculatorNewtonRaphson pi = new PICalculatorNewtonRaphson();
        pi.initialize(numDigits);



        /* Calculate PI using the Newton - Raphson formula */

        t.start();
        pi.run();
        long totalTime = t.stop();

        double secondsTotalTime =  toTimeUnit(totalTime, Sec);

        //Calculating the score:
        int numCores = Runtime.getRuntime().availableProcessors();
        score = (long)(((pi.getNumDigits()/secondsTotalTime))/numCores) ;//number of digits over the time it took to run the program multiplied by 1000 then divided by
        //the number of cores!





        //Display the result and the time it took in a new JFrame

        JFrame resultFrame = new JFrame("Result");
        resultFrame.setSize(700, 450);
        JTextArea resultTextArea = new JTextArea(10, 30);
        resultTextArea.setEditable(false);
        resultTextArea.append(String.format("Pi number with %d digits is:\n", numDigits));
        String piResult = pi.getResult();
        int lineLength = 80;
        for (int i = 0; i < piResult.length(); i += lineLength) {
            int endIndex = Math.min(i + lineLength, piResult.length());
            resultTextArea.append(String.format("%s\n", piResult.substring(i, endIndex)));
        }
        resultTextArea.append(String.format("Time taken: %s milliseconds\n", toTimeUnit(totalTime, Milli)));
        resultTextArea.append(String.format("The score is: %d\n\n", score));


        //Adding the resultTextArea to the new frame
        resultFrame.add(new JScrollPane(resultTextArea));
        resultFrame.setVisible(true);


        //Adding a back button to the result frame
        JButton backButton = new JButton("Back");

        //creating the function for the backButton
        backButton.addActionListener(e1 -> resultFrame.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(backButton);
        resultFrame.add(buttonPanel, BorderLayout.SOUTH);

        resultFrame.setVisible(true);
    }
}
