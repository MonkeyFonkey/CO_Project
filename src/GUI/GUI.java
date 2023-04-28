package GUI;

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
