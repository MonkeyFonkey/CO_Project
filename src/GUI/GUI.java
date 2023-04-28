package GUI;

/*
import Benchmark.PICalculatorNewtonRaphson;
import Timing.Timing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static Logging.TimeUnit.Sec;
import static Logging.TimeUnit.toTimeUnit;
import static java.lang.System.exit;

public class GUI implements ActionListener {

    private int count = 0;
    private JFrame frame;
    private JPanel panel;
    private JButton run_bench_button;
    private JLabel label;

    public GUI(){
        frame = new JFrame();

        run_bench_button = new JButton("Calculate PI using NewtonRaphson");
        run_bench_button.addActionListener(this);

        panel = new JPanel();

        label = new JLabel("Output: ");

        panel.setBorder(BorderFactory.createEmptyBorder(30,30,10,30));
        panel.setLayout(new GridLayout(0, 1));
        panel.add(run_bench_button);
        panel.add(label);



        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Benchmark");
        frame.pack();
        frame.setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {
        count++;
        if(count == 1){
            Timing clock = new Timing();
            PICalculatorNewtonRaphson pi1 = new PICalculatorNewtonRaphson();
            pi1.initialize(100);
            clock.start();

            pi1.run();

            long time = clock.stop();

            label.setText("Output: " + pi1.getResult() + "\n it took " + toTimeUnit(time, Sec));
        }
        else if(count == 2){
            exit(1);
        }
    }
}
*/

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
        // Get the number of digits to calculate
        int numDigits = Integer.parseInt(digitsTextField.getText());

        Timing t = new Timing();
        PICalculatorNewtonRaphson pi = new PICalculatorNewtonRaphson();
        pi.initialize(numDigits);


        // Calculate PI using the Newton-Raphson formula

        t.start();
        pi.run();
        long totalTime = t.stop();

        //Display the result and the time it took in a new JFrame

        JFrame resultFrame = new JFrame("Result");
        resultFrame.setSize(400, 300);
        JTextArea resultTextArea = new JTextArea(10, 30);
        resultTextArea.setEditable(false);
        resultTextArea.append(String.format("Pi number with " + numDigits + "digits is: " + pi.getResult() + "\n"));
        resultTextArea.append(String.format("Time taken: " + toTimeUnit(totalTime, Milli) + " milliseconds\n"));

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
