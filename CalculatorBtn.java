import javax.swing.*;

public class CalculatorBtn extends JButton {
    private Button btnType;

    public CalculatorBtn(Button btnType) {
        super(btnType.getLabel());
        this.btnType = btnType;
    }

    public Button getBtnType() {
        return btnType;
    }

    public enum Button {
        NUM_0("0", Type.NUMBER),
        NUM_1("1", Type.NUMBER),
        NUM_2("2", Type.NUMBER),
        NUM_3("3", Type.NUMBER),
        NUM_4("4", Type.NUMBER),
        NUM_5("5", Type.NUMBER),
        NUM_6("6", Type.NUMBER),
        NUM_7("7", Type.NUMBER),
        NUM_8("8", Type.NUMBER),
        NUM_9("9", Type.NUMBER),
        SYMBOL_DOT(".", Type.SYMBOL),
        SYMBOL_PERCENT("%", Type.SYMBOL),
        ACTION_AC("AC", Type.ACTION),
        ACTION_DEL("DEL", Type.ACTION),
        ACTION_CHANGE_SYMBOL("+/-", Type.ACTION),
        ACTION_EQUALS("=", Type.ACTION),
        CALCULATE_ADD("+", Type.CALCULATE),
        CALCULATE_SUB("-", Type.CALCULATE),
        CALCULATE_MUL("*", Type.CALCULATE),
        CALCULATE_DIV("/", Type.CALCULATE);

        private final String label;
        private final Type type;

        Button(String label, Type type) {
            this.label = label;
            this.type = type;
        }

        public String getLabel() {
            return label;
        }

        public Type getType() {
            return type;
        }

        public enum Type {
            NUMBER,
            ACTION,
            SYMBOL,
            CALCULATE
        }
    }
}