package com.company;

import java.util.Arrays;

public enum Range {
    TWO_HUNDRED(200, 299),
    THREE_HUNDRED(300, 399),
    FIVE_HUNDRED(500, 599), // Yes in the API there is no range of 400
    SIX_HUNDRED(600, 699),
    SEVEN_HUNDRED(700, 799),
    EIGHT_HUNDRED(800, 800),
    EIGHT_HUNDRED_PLUS(801, 899),
    OTHER(0, -1);

    private final int minValue;
    private final int maxValue;

    Range(int min, int max) {
        this.minValue = min;
        this.maxValue = max;
    }

    public static Range from(int number){
        return Arrays.stream(Range.values())
                .filter(range -> number >= range.minValue && number <= range.maxValue)
                .findAny()
                .orElse(OTHER);
    }
}