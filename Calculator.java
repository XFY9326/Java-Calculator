import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;

public class Calculator extends BaseFrame implements ActionListener, KeyEventPostProcessor {
    private final static int frameWidth = 300;
    private final static int frameHeight = 400;
    private final static int panelMargin = 5;
    private final static CalculatorBtn.Button[][] BTN_TEXT = {
            {CalculatorBtn.Button.ACTION_AC, CalculatorBtn.Button.ACTION_DEL, CalculatorBtn.Button.SYMBOL_PERCENT, CalculatorBtn.Button.CALCULATE_ADD},
            {CalculatorBtn.Button.NUM_7, CalculatorBtn.Button.NUM_8, CalculatorBtn.Button.NUM_9, CalculatorBtn.Button.CALCULATE_SUB},
            {CalculatorBtn.Button.NUM_4, CalculatorBtn.Button.NUM_5, CalculatorBtn.Button.NUM_6, CalculatorBtn.Button.CALCULATE_MUL},
            {CalculatorBtn.Button.NUM_1, CalculatorBtn.Button.NUM_2, CalculatorBtn.Button.NUM_3, CalculatorBtn.Button.CALCULATE_DIV},
            {CalculatorBtn.Button.NUM_0, CalculatorBtn.Button.SYMBOL_DOT, CalculatorBtn.Button.ACTION_CHANGE_SYMBOL, CalculatorBtn.Button.ACTION_EQUALS}
    };
    private GridBagLayout gridBagLayout;
    private JTextArea outputText;

    private BigDecimal lastInputNum = new BigDecimal("0");
    private boolean needInputNewNum = true;
    private CalculatorBtn.Button calculateSymbol = null;

    @Override
    protected JPanel buildFrame(int width, int height) {
        setCenterFrameSize(frameWidth, frameHeight);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventPostProcessor(this);

        JPanel jPanel = new JPanel();
        gridBagLayout = new GridBagLayout();
        jPanel.setLayout(gridBagLayout);
        jPanel.setBorder(new EmptyBorder(panelMargin, panelMargin, panelMargin, panelMargin));
        return jPanel;
    }

    @Override
    protected void initComponents(JPanel basePanel) {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;

        outputText = new JTextArea();
        outputText.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        outputText.setLineWrap(true);
        outputText.setWrapStyleWord(true);
        outputText.setDisabledTextColor(Color.BLACK);
        outputText.setEditable(false);
        outputText.setEnabled(false);
        JScrollPane scroll = new JScrollPane(outputText);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        gridBagLayout.setConstraints(scroll, gridBagConstraints);
        basePanel.add(scroll, gridBagConstraints);

        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;

        for (CalculatorBtn.Button[] btnType : BTN_TEXT) {
            for (int j = 0; j < btnType.length; j++) {
                CalculatorBtn btn = new CalculatorBtn(btnType[j]);
                btn.addActionListener(this);
                if (j == btnType.length - 1) {
                    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
                } else {
                    gridBagConstraints.gridwidth = 1;
                }
                basePanel.add(btn, gridBagConstraints);
            }
        }

        allClear();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String currentOutput = outputText.getText();
        CalculatorBtn.Button btn = ((CalculatorBtn) e.getSource()).getBtnType();

        if (btn == CalculatorBtn.Button.ACTION_AC) {
            allClear();
        } else if (btn == CalculatorBtn.Button.ACTION_DEL) {
            if (currentOutput.length() > 1) {
                outputText.setText(currentOutput.substring(0, currentOutput.length() - 1));
            } else {
                outputText.setText("0");
            }
        } else if (btn == CalculatorBtn.Button.ACTION_CHANGE_SYMBOL) {
            BigDecimal num = new BigDecimal(currentOutput);
            outputText.setText(num.multiply(new BigDecimal("-1")).stripTrailingZeros().toPlainString());
        } else if (btn == CalculatorBtn.Button.ACTION_EQUALS) {
            if (calculateSymbol != null) {
                BigDecimal output = null;
                BigDecimal num = new BigDecimal(currentOutput);
                if (needInputNewNum) {
                    if (calculateSymbol == CalculatorBtn.Button.CALCULATE_ADD) {
                        output = num.add(lastInputNum);
                    } else if (calculateSymbol == CalculatorBtn.Button.CALCULATE_SUB) {
                        output = num.subtract(lastInputNum);
                    } else if (calculateSymbol == CalculatorBtn.Button.CALCULATE_MUL) {
                        output = num.multiply(lastInputNum);
                    } else if (calculateSymbol == CalculatorBtn.Button.CALCULATE_DIV) {
                        output = num.divide(lastInputNum, 10, BigDecimal.ROUND_DOWN);
                    }
                } else {
                    if (calculateSymbol == CalculatorBtn.Button.CALCULATE_ADD) {
                        output = lastInputNum.add(num);
                    } else if (calculateSymbol == CalculatorBtn.Button.CALCULATE_SUB) {
                        output = lastInputNum.subtract(num);
                    } else if (calculateSymbol == CalculatorBtn.Button.CALCULATE_MUL) {
                        output = lastInputNum.multiply(num);
                    } else if (calculateSymbol == CalculatorBtn.Button.CALCULATE_DIV) {
                        output = lastInputNum.divide(num, 10, BigDecimal.ROUND_DOWN);
                    }
                }
                if (output != null) {
                    outputText.setText(output.stripTrailingZeros().toPlainString());
                    if (!needInputNewNum) {
                        lastInputNum = num;
                    }
                    needInputNewNum = true;
                }
            }
        } else if (btn == CalculatorBtn.Button.SYMBOL_PERCENT) {
            if (!"0".equals(currentOutput)) {
                BigDecimal num = new BigDecimal(currentOutput);
                outputText.setText(num.multiply(new BigDecimal("0.01")).stripTrailingZeros().toPlainString());
            }
        } else if (btn == CalculatorBtn.Button.SYMBOL_DOT) {
            if (!currentOutput.contains(CalculatorBtn.Button.SYMBOL_DOT.getLabel())) {
                outputText.setText(currentOutput + btn.getLabel());
            }
        } else if (btn.getType() == CalculatorBtn.Button.Type.CALCULATE) {
            calculateSymbol = btn;
            needInputNewNum = true;
            lastInputNum = new BigDecimal(currentOutput).stripTrailingZeros();
        } else if (btn.getType() == CalculatorBtn.Button.Type.NUMBER) {
            addOutput(btn.getLabel());
        }
    }

    public boolean postProcessKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_TYPED) {
            String currentOutput = outputText.getText();
            if (e.getKeyChar() == '.' && !currentOutput.contains(".") || e.getKeyChar() >= '0' && e.getKeyChar() <= '9') {
                addOutput(String.valueOf(e.getKeyChar()));
                return true;
            }
        }
        return false;
    }

    private void addOutput(String add) {
        String currentOutput = outputText.getText();
        if (needInputNewNum) {
            if (!"0".equals(add)) {
                needInputNewNum = false;
                outputText.setText(add);
            }
        } else {
            outputText.setText(currentOutput + add);
        }
    }

    private void allClear() {
        outputText.setText("0");
        lastInputNum = new BigDecimal("0");
        calculateSymbol = null;
        needInputNewNum = true;
    }
}
