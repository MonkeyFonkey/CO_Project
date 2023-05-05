package GUI;


import Benchmark.CpuInfo;
import java.text.NumberFormat;
import Benchmark.PICalculatorNewtonRaphson;
import Timing.Timing;
import com.sun.management.OperatingSystemMXBean;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.util.Scanner;
import static Benchmark.NetworkInfo.getPCID;
import static Logging.TimeUnit.*;

public class GUI extends JFrame implements ActionListener {
    private JLabel digitsLabel;
    private JTextField digitsForAvgScoreTextField;
    private JLabel digitsForAvgScoreLabel;
    private JTextField digitsTextField;

    private JButton calculateButton;
    private JTextArea outputTextArea;
    private JButton showPCConfigurationButton;
    private JButton average_score_to_document_button;
    private JLabel cpuUsageLabel;

    private static final String SCORE_FILE = "scores.txt";

    private double score = 0;

    private OperatingSystemMXBean osBean; // for the number of cores

    public GUI() {
        super("Pi Calculator");

        // Create and configure GUI components
        digitsLabel = new JLabel("Digits of PI to calculate:");
        digitsTextField = new JTextField(10);
        calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(this);
        digitsTextField.addActionListener(this);
        digitsTextField.setActionCommand("calculate");


        showPCConfigurationButton = new JButton("Show CPU Configuration");
        showPCConfigurationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get CPU information and display in a message dialog
                String cpuInfo = getCPUInformation();
                JOptionPane.showMessageDialog(null, cpuInfo, "CPU Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        average_score_to_document_button = new JButton("Add Average Score to Document!");
        average_score_to_document_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CpuInfo cpu = new CpuInfo();


                //Checking if pcID could be retrived
                String pcID;
                try {
                    pcID = getPCID();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, "Error: Could not determine PC ID.");
                    return;
                }

//                System.out.println(pcID);
//
//                pcID = getPCID().replace("-andre", "");
//
//                System.out.println(pcID);

                //Checking if CPU model could be retrived
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

                score = Math.round(((pi.getNumDigits()/ toTimeUnit(totalTime, Sec))/numCores)) / 10;

                String scoreRecord = pcID + "," + cpuModel + "," + score;


                //Checking if there is a unique pcID
                try {
                    if (!isPCIDUnique(pcID)) {
                        JOptionPane.showMessageDialog(null, "Error: Unique PCID already exists in score file");
                        return;
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }


                try {
                    saveScoreToFile(scoreRecord);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Could not save to score file");
                    return;
                }


            }
        });


        digitsForAvgScoreTextField = new JTextField(10);
        digitsForAvgScoreLabel = new JLabel("Digits of PI to calculate for average score: 1000");

        //adding a cpu usage label
        cpuUsageLabel = new JLabel("CPU Usage: ");
        JPanel cpuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cpuPanel.add(cpuUsageLabel);

        // Add components to content pane
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

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

        //adding outputTextArea for a new PANEL
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        panel.add(new JScrollPane(outputTextArea), constraints);

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

        digitsForAvgScoreTextField = new JTextField("1000", 10);


        getContentPane().add(panel);

        //Center the panel in the middle of the screen
        setLocationRelativeTo(null);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Start a timer to update the CPU usage label every 1 second
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double cpuUsage = getCpuUsage();
                DecimalFormat df = new DecimalFormat("#.#");
                cpuUsageLabel.setText("CPU Usage: " + df.format(cpuUsage) + "%");
            }
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


    //Searches if there is not already a PC with the same PCID
    private static boolean isPCIDUnique(String pcID) throws Exception {
        try (Scanner scanner = new Scanner(new File("src/data/scores.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().startsWith(pcID.trim() + ",")) {
                    return false;
                }
            }
        }
        return true;
    }


    //add Score to File
    private static void saveScoreToFile(String scoreRecord) throws Exception {
        Files.write(Paths.get("src/data/scores.txt"), (scoreRecord + "\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }




    //action when pressing the "calculate" button
    public void actionPerformed(ActionEvent e) {
            // Get the number of digits to calculate
            int numDigits = 0;
            try {
                numDigits = Integer.parseInt(digitsTextField.getText());
            } catch (NumberFormatException ex) {
                // Display an error message and highlight the text field in red
                JOptionPane.showMessageDialog(this, "Please enter a valid integer", "Error", JOptionPane.ERROR_MESSAGE);
                digitsTextField.setBorder(BorderFactory.createLineBorder(Color.RED));
                return;
            }

            // Reset the background color of the text field
            digitsTextField.setBorder(BorderFactory.createLineBorder(Color.black));

            Timing t = new Timing();
            PICalculatorNewtonRaphson pi = new PICalculatorNewtonRaphson();
            pi.initialize(numDigits);



            // Calculate PI using the Newton-Raphson formula

            t.start();
            pi.run();
            long totalTime = t.stop();

            double secondsTotalTime =  toTimeUnit(totalTime, Sec);

            //Calculating the score:
            int numCores = Runtime.getRuntime().availableProcessors();
            score = Math.round((((pi.getNumDigits()/secondsTotalTime))/numCores)) / 10;//number of digits over the time it took to run the program multipled by 1000 then divided by
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

            resultTextArea.append(String.format("Score: %s/200\n\n", score));



            //resultTextArea.append(String.format("Score: %.2f%%\n\n", (double) score));


            //Adding the resultTextArea to the new frame
            resultFrame.add(new JScrollPane(resultTextArea));
            resultFrame.setVisible(true);


            //Adding a back button to the result frame
            JButton backButton = new JButton("Back");

            //creating the function for the backButton
            backButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    resultFrame.dispose();
                }
            });

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(backButton);
            resultFrame.add(buttonPanel, BorderLayout.SOUTH);

            resultFrame.setVisible(true);
    }

}
