package GUI;


import Benchmark.PICalculatorNewtonRaphson;
import Timing.Timing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static Logging.TimeUnit.*;

public class GUI extends JFrame implements ActionListener {
    private JLabel digitsLabel;
    private JTextField digitsTextField;
    private JButton calculateButton;
    private JTextArea outputTextArea;

    public GUI() {
        super("Pi Calculator");

        // Create and configure GUI components
        digitsLabel = new JLabel("Digits of PI to calculate:");
        digitsTextField = new JTextField(10);
        calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(this);
        digitsTextField.addActionListener(this);
        digitsTextField.setActionCommand("calculate");

//        outputTextArea = new JTextArea(10, 30);
//        outputTextArea.setEditable(false);

        // Add components to content pane
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        panel.add(digitsLabel, constraints);
        constraints.gridx = 1;
        panel.add(digitsTextField, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        panel.add(calculateButton, constraints);
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        panel.add(new JScrollPane(outputTextArea), constraints);

        getContentPane().add(panel);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
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
