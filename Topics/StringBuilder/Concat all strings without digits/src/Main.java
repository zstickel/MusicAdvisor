import java.util.Scanner;

class ConcatenateStringsProblem {

    public static String concatenateStringsWithoutDigits(String[] strings) {
        // write your code with StringBuilder here
        StringBuilder stringBuilder = new StringBuilder();
        for(String string: strings){
            stringBuilder.append(string);
        }
        for (int i = 0; i < stringBuilder.length(); ) {
            char c = stringBuilder.charAt(i);
            // Check if the character is a digit
            if (Character.isDigit(c)) {
                // If it is a digit, delete it from the StringBuilder
                stringBuilder.deleteCharAt(i);
            } else {
                // If it is not a digit, move to the next character
                i++;
            }
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] strings = scanner.nextLine().split("\\s+");
        String result = concatenateStringsWithoutDigits(strings);
        System.out.println(result);
    }
}