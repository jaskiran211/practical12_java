package com.calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;

public class Calculator extends JFrame implements ActionListener {
    JTextField textField;
    String expression = "";

    JButton[] numberButtons = new JButton[10];
    JButton addButton, subButton, mulButton, divButton;
    JButton decButton, equButton, delButton, clrButton;
    JPanel panel;

    public Calculator() {
        setTitle("Simple Calculator");
        setSize(420, 550);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textField = new JTextField();
        textField.setBounds(50, 25, 300, 50);
        textField.setEditable(false);
        textField.setFont(new Font("Arial", Font.PLAIN, 24));
        add(textField);

        addButton = new JButton("+");
        subButton = new JButton("-");
        mulButton = new JButton("*");
        divButton = new JButton("/");
        decButton = new JButton(".");
        equButton = new JButton("=");
        delButton = new JButton("Del");
        clrButton = new JButton("C");

        JButton[] functionButtons = {
            addButton, subButton, mulButton, divButton,
            decButton, equButton, delButton, clrButton
        };

        for (JButton btn : functionButtons) {
            btn.addActionListener(this);
            btn.setFont(new Font("Arial", Font.BOLD, 20));
            btn.setFocusable(false);
        }

        for (int i = 0; i < 10; i++) {
            numberButtons[i] = new JButton(String.valueOf(i));
            numberButtons[i].addActionListener(this);
            numberButtons[i].setFont(new Font("Arial", Font.BOLD, 20));
            numberButtons[i].setFocusable(false);
        }

        panel = new JPanel();
        panel.setBounds(50, 100, 300, 300);
        panel.setLayout(new GridLayout(4, 4, 10, 10));

        panel.add(numberButtons[1]);
        panel.add(numberButtons[2]);
        panel.add(numberButtons[3]);
        panel.add(addButton);
        panel.add(numberButtons[4]);
        panel.add(numberButtons[5]);
        panel.add(numberButtons[6]);
        panel.add(subButton);
        panel.add(numberButtons[7]);
        panel.add(numberButtons[8]);
        panel.add(numberButtons[9]);
        panel.add(mulButton);
        panel.add(decButton);
        panel.add(numberButtons[0]);
        panel.add(equButton);
        panel.add(divButton);

        add(panel);

        delButton.setBounds(50, 420, 145, 50);
        clrButton.setBounds(205, 420, 145, 50);
        add(delButton);
        add(clrButton);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < 10; i++) {
            if (e.getSource() == numberButtons[i]) {
                expression += String.valueOf(i);
                textField.setText(expression);
            }
        }

        if (e.getSource() == decButton) {
            expression += ".";
            textField.setText(expression);
        }

        if (e.getSource() == addButton) {
            expression += "+";
            textField.setText(expression);
        }

        if (e.getSource() == subButton) {
            expression += "-";
            textField.setText(expression);
        }

        if (e.getSource() == mulButton) {
            expression += "*";
            textField.setText(expression);
        }

        if (e.getSource() == divButton) {
            expression += "/";
            textField.setText(expression);
        }

        if (e.getSource() == equButton) {
            try {
                double result = evaluate(expression);
                textField.setText(String.valueOf(result));
                expression = String.valueOf(result);
            } catch (Exception exx) {
                textField.setText("Error");
                expression = "";
            }
        }

        if (e.getSource() == clrButton) {
            expression = "";
            textField.setText("");
        }

        if (e.getSource() == delButton) {
            if (expression.length() > 0) {
                expression = expression.substring(0, expression.length() - 1);
                textField.setText(expression);
            }
        }
    }

    private double evaluate(String expr) {
        Stack<Double> values = new Stack<>();
        Stack<Character> ops = new Stack<>();
        String numBuffer = "";
        boolean expectUnary = true;

        for (int i = 0; i < expr.length(); i++) {
            char ch = expr.charAt(i);

            if (Character.isDigit(ch) || ch == '.') {
                numBuffer += ch;
                expectUnary = false;
            } else if (ch == '-' && expectUnary) {
                numBuffer += ch;
                expectUnary = false;
            } else {
                if (!numBuffer.isEmpty()) {
                    values.push(Double.parseDouble(numBuffer));
                    numBuffer = "";
                }

                while (!ops.isEmpty() && precedence(ops.peek()) >= precedence(ch)) {
                    double b = values.pop();
                    double a = values.pop();
                    values.push(applyOp(ops.pop(), b, a));
                }
                ops.push(ch);
                expectUnary = true;
            }
        }

        if (!numBuffer.isEmpty()) {
            values.push(Double.parseDouble(numBuffer));
        }

        while (!ops.isEmpty()) {
            double b = values.pop();
            double a = values.pop();
            values.push(applyOp(ops.pop(), b, a));
        }

        return values.pop();
    }

    private int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/') return 2;
        return 0;
    }

    private double applyOp(char op, double b, double a) {
        if (op == '+') return a + b;
        if (op == '-') return a - b;
        if (op == '*') return a * b;
        if (op == '/') return b != 0 ? a / b : 0;
        return 0;
    }

    public static void main(String[] args) {
        new Calculator();
    }
}
