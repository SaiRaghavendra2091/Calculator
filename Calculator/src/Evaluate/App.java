package Evaluate;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class App implements ActionListener {
    JFrame frame;
    JTextField textField;
    JButton[] numberButtons = new JButton[10];
    JButton[] functionButtons = new JButton[10];
    JButton addB, subB, equB, delB, clrB, divB, mulB, decB, openB, closeB;
    JPanel panel;

    Font myFont = new Font("Ink Free", Font.BOLD, 25);

    double result = 0;

    App() {

        frame = new JFrame("Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 550);
        frame.setLayout(null);

        textField = new JTextField();
        textField.setBounds(50, 25, 300, 50);
        textField.setFont(myFont);
        textField.setEditable(false);

        addB = new JButton("+");
        subB = new JButton("-");
        mulB = new JButton("*");
        divB = new JButton("/");
        decB = new JButton(".");
        delB = new JButton("<--");
        clrB = new JButton("clr");
        equB = new JButton("=");
        openB = new JButton("(");
        closeB = new JButton(")");

        functionButtons[0] = addB;
        functionButtons[1] = subB;
        functionButtons[2] = mulB;
        functionButtons[3] = divB;
        functionButtons[4] = decB;
        functionButtons[5] = delB;
        functionButtons[6] = clrB;
        functionButtons[7] = equB;
        functionButtons[8] = openB;
        functionButtons[9] = closeB;

        for (int i = 0; i < 10; i++) {
            functionButtons[i].addActionListener(this);
            functionButtons[i].setFont(myFont);
            functionButtons[i].setFocusable(false);
        }

        for (int i = 0; i < 10; i++) {
            numberButtons[i] = new JButton(String.valueOf(i));
            numberButtons[i].addActionListener(this);
            numberButtons[i].setFont(myFont);
            numberButtons[i].setFocusable(false);
        }

        panel = new JPanel();
        panel.setBounds(50, 100, 300, 300);
        panel.setLayout(new GridLayout(5, 4, 10, 10));

        panel.add(openB);
        panel.add(closeB);
        panel.add(delB);
        panel.add(clrB);
        panel.add(numberButtons[1]);
        panel.add(numberButtons[2]);
        panel.add(numberButtons[3]);
        panel.add(addB);
        panel.add(numberButtons[4]);
        panel.add(numberButtons[5]);
        panel.add(numberButtons[6]);
        panel.add(subB);
        panel.add(numberButtons[7]);
        panel.add(numberButtons[8]);
        panel.add(numberButtons[9]);
        panel.add(mulB);
        panel.add(decB);
        panel.add(numberButtons[0]);
        panel.add(equB);
        panel.add(divB);

        frame.add(textField);
        frame.add(panel);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < 10; i++) {
            if (e.getSource() == numberButtons[i]) {
                textField.setText(textField.getText().concat((String.valueOf(i))));
                return;
            }
        }

        if (e.getSource() == decB) {
            textField.setText(textField.getText().concat("."));
        }

        if (e.getSource() == addB) {
            textField.setText(textField.getText().concat("+"));
        }

        if (e.getSource() == subB) {
            textField.setText(textField.getText().concat("-"));
        }

        if (e.getSource() == mulB) {
            textField.setText(textField.getText().concat("*"));
        }

        if (e.getSource() == divB) {
            textField.setText(textField.getText().concat("/"));
        }

        if (e.getSource() == equB) {
            String exp = textField.getText();
            result = evaluate(exp);
            textField.setText(String.valueOf(result));
        }

        if (e.getSource() == clrB) {
            textField.setText("");
        }

        if (e.getSource() == delB) {
            String str = textField.getText();
            textField.setText("");
            for (int j = 0; j < str.length() - 1; j++) {
                textField.setText(textField.getText() + str.charAt(j));
            }
        }

        if (e.getSource() == openB) {
            textField.setText(textField.getText().concat("("));
        }

        if (e.getSource() == closeB) {
            textField.setText(textField.getText().concat(")"));
        }

    }

    public static double evaluate(String expression) {
        String str = "";
        char[] tokens = expression.toCharArray();

        // Stack for numbers: 'values'
        Stack<Double> values = new Stack<Double>();

        // Stack for Operators: 'ops'
        Stack<Character> ops = new Stack<Character>();

        for (int i = 0; i < tokens.length; i++) {

            // Current token is a
            // whitespace, skip it
            if (tokens[i] == ' ')
                continue;

            // Current token is a number,
            // push it to stack for numbers
            if ((tokens[i] >= '0' &&
                    tokens[i] <= '9') || tokens[i] == '.') {
                StringBuffer sbuf = new StringBuffer();

                // There may be more than one
                // digits in number
                while (i < tokens.length &&
                        ((tokens[i] >= '0' &&
                                tokens[i] <= '9') || tokens[i] == '.')) {
                    if (str.isEmpty())
                        sbuf.append(tokens[i++]);
                    else {
                        sbuf.append(str + tokens[i++]);
                        str = "";
                    }
                }
                values.push(Double.parseDouble(sbuf.toString()));

                // right now the i points to
                // the character next to the digit,
                // since the for loop also increases
                // the i, we would skip one
                // token position; we need to
                // decrease the value of i by 1 to
                // correct the offset.
                i--;
            }

            // Current token is an opening brace,
            // push it to 'ops'
            else if (tokens[i] == '(')
                ops.push(tokens[i]);

            // Closing brace encountered,
            // solve entire brace
            else if (tokens[i] == ')') {
                while (ops.peek() != '(')
                    values.push(applyOp(ops.pop(),
                            values.pop(),
                            values.pop()));
                ops.pop();
            }

            // Current token is an operator.
            else if (tokens[i] == '+' ||
                    tokens[i] == '-' ||
                    tokens[i] == '*' ||
                    tokens[i] == '/') {
                if (i == 0) {
                    str += tokens[i];
                } else if (tokens[i - 1] == '+' ||
                        tokens[i - 1] == '-' ||
                        tokens[i - 1] == '*' ||
                        tokens[i - 1] == '/' ||
                        tokens[i - 1] == '(') {
                    str += tokens[i];
                } else {
                    // While top of 'ops' has same
                    // or greater precedence to current
                    // token, which is an operator.
                    // Apply operator on top of 'ops'
                    // to top two elements in values stack
                    while (!ops.empty() && hasPrecedence(tokens[i], ops.peek()))
                        values.push(applyOp(ops.pop(), values.pop(), values.pop()));

                    // Push current token to 'ops'.
                    ops.push(tokens[i]);
                }
            }
        }
        // Entire expression has been
        // parsed at this point, apply remaining
        // ops to remaining values
        while (!ops.empty())
            values.push(applyOp(ops.pop(),
                    values.pop(),
                    values.pop()));

        // Top of 'values' contains
        // result, return it
        return values.pop();
    }

    // Returns true if 'op2' has higher
    // or same precedence as 'op1',
    // otherwise returns false.
    public static boolean hasPrecedence(
            char op1, char op2) {
        if (op2 == '(' || op2 == ')')
            return false;
        if ((op1 == '*' || op1 == '/') &&
                (op2 == '+' || op2 == '-'))
            return false;
        else
            return true;
    }

    // A utility method to apply an
    // operator 'op' on operands 'a'
    // and 'b'. Return the result.
    public static double applyOp(char op,
            double b, double a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0)
                    throw new UnsupportedOperationException(
                            "Infinity");
                else
                    return a / b;
        }
        return 0;
    }

    public static void main(String[] args) throws Exception {

        App calc = new App();
    }

}
