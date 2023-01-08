public enum Operator {
    ADDITION("+"), SUBTRACTION("-"), MULTIPLICATION("*"), DIVISION("/");

    public final String text;

    Operator(String text) {
        this.text = text;
    }
}
