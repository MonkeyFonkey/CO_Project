package GUI;


import Benchmark.CpuInfo;
import Benchmark.PICalculatorNewtonRaphson;
import Timing.Timing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

import static Logging.TimeUnit.*;

public class GUI extends JFrame implements ActionListener {
    private JLabel digitsLabel;
    private JTextField digitsTextField;
    private JButton calculateButton;
    private JTextArea outputTextArea;
    private JButton showPCConfigurationButton;
    private JLabel cpuUsageLabel;

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

        //adding outputTextArea
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
        constraints.gridy = 4;
        panel.add(cpuUsageLabel, constraints); // Add CPU usage label to the GUI


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


    public void actionPerformed(ActionEvent e) {
        //if ("calculate".equals(e.getActionCommand())) {
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

            //resultTextArea.append(String.format("Time taken: " + toTimeUnit(totalTime, Milli) + " milliseconds\n"));

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
      //  }
    }



}
