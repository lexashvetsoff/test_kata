import java.util.List;
import java.util.Scanner;

public class Main {
    static Operator getOperator(String input) {
        if (input.contains("+")) {
            return Operator.ADDITION;
        } else if (input.contains("-")) {
            return Operator.SUBTRACTION;
        } else if (input.contains("*")) {
            return Operator.MULTIPLICATION;
        } else if (input.contains("/")) {
            return Operator.DIVISION;
        }

        return null;
    }

    static String[] getOperands(String input, String separator) {
        String sep = "["+separator+"]";
        String[] splitInput = input.split(sep);
        String[] results = new String[splitInput.length];
        for (int i = 0; i < splitInput.length; i++) {
            results[i] = splitInput[i].strip();
        }
        return results;
    }

    static boolean isRoman(String input) {
        return !input.isEmpty() && input.toUpperCase().matches("M{0,3}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})");
    }

    static boolean isArabic(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    static int romanToArabic(String input) {
        String romanNumeral = input.toUpperCase();
        int result = 0;

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        int i = 0;

        while ((romanNumeral.length() > 0) && (i < romanNumerals.size())) {
            RomanNumeral symbol = romanNumerals.get(i);
            if (romanNumeral.startsWith(symbol.name())) {
                result += symbol.getValue();
                romanNumeral = romanNumeral.substring(symbol.name().length());
            } else {
                i++;
            }
        }

        if (romanNumeral.length() > 0) {
            throw new IllegalArgumentException(input + " cannot be converted to a Roman Numeral");
        }

        return result;
    }

    static String arabicToRoman(int number) {
        if ((number <= 0) || (number > 4000)) {
            throw new IllegalArgumentException(number + " is not in range (0,4000]");
        }

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        int i = 0;
        StringBuilder sb = new StringBuilder();

        while ((number > 0) && (i < romanNumerals.size())) {
            RomanNumeral currentSymbol = romanNumerals.get(i);
            if (currentSymbol.getValue() <= number) {
                sb.append(currentSymbol.name());
                number -= currentSymbol.getValue();
            } else {
                i++;
            }
        }

        return sb.toString();
    }

    static String makeCalculateArabic(Operator operator, String op1, String op2) {
        int result;
        int operand1 = Integer.parseInt(op1);
        int operand2 = Integer.parseInt(op2);

        switch (operator) {
            case ADDITION:
                result = operand1 + operand2;
                break;
            case SUBTRACTION:
                result = operand1 - operand2;
                break;
            case MULTIPLICATION:
                result = operand1 * operand2;
                break;
            case DIVISION:
                double div = operand1 / operand2;
                result = (int)(div);
                break;
            default:
                result = 0;
        }
        return Integer.toString(result);
    }

    static String makeCalculateRoman(Operator operator, String op1, String op2) throws Exception {
        int result;
        int operand1 = romanToArabic(op1);
        int operand2 = romanToArabic(op2);

        switch (operator) {
            case ADDITION:
                result = operand1 + operand2;
                break;
            case SUBTRACTION:
                if (operand1 > operand2) {
                    result = operand1 - operand2;
                } else throw new Exception("Недопустимая операция - в римской системе нет отрицательных чисел");
                break;
            case MULTIPLICATION:
                result = operand1 * operand2;
                break;
            case DIVISION:
                double div = operand1 / operand2;
                result = (int)(div);
                break;
            default:
                result = 0;
        }

        return arabicToRoman(result);
    }

    public static String calc(String input) throws Exception {
        String result = "";

        Operator operator = getOperator(input);

        if(operator != null) {
            String[] operands = getOperands(input, operator.text);

            if (isRoman(operands[0]) && isRoman(operands[1])) {
                if (romanToArabic(operands[0]) <= 10 && romanToArabic(operands[1]) <= 10) {
                    result = makeCalculateRoman(operator, operands[0], operands[1]);
                } else throw new Exception("Превышены допустимые значения операнда/ов");
            } else if (isArabic(operands[0]) && isArabic(operands[1])) {
                if (Integer.parseInt(operands[0]) <= 10 && Integer.parseInt(operands[1]) <= 10) {
                    result = makeCalculateArabic(operator, operands[0], operands[1]);
                } else throw new Exception("Превышены допустимые значения операнда/ов");
            } else throw new Exception("Используются одновременно разные системы счисления");
        } else throw new Exception("Не допустимая операция");

        return result;
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Калькулятор принимает на вход целые числа от 1 до 10 включительно, не более.");
        System.out.println("Введите выражение: ");
        String expression = scanner.nextLine();

        String result = calc(expression);
        System.out.println(result);
    }
}