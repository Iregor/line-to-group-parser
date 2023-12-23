package org.example;

import java.util.Arrays;

public class LineValidator {
    public static boolean validLineStart(String line) {
        return line.charAt(0) != ';';
    }

    public static boolean validWordArr(String[] line) {
        return Arrays.stream(line).filter(LineValidator::validWord).count() == line.length;
    }

    public static boolean validWord(String word) {
        return (word.charAt(0) == '"'
                && word.charAt(word.length() - 1) == '"'
                && word.chars().filter(ch -> ch == '"').count() == 2);
    }
}
