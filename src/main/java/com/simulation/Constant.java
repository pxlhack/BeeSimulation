package com.simulation;

public class Constant {
    private static int bottom = 220;
    private static int top = 50;
    private static int left = 50;
    private static int right = 330;

    public static int getBottom() {
        return bottom;
    }

    public static int getTop() {
        return top;
    }

    public static int getLeft() {
        return left;
    }

    public static int getRight() {
        return right;
    }

    public static int[] getCornerCoordinates(int cornerIndex) {
        int[] angle = {getLeft(), getTop()};
        switch (cornerIndex) {
            case 0 -> angle[0] = getRight() + getLeft();
            case 1 -> {
                angle[0] = getRight() + getLeft();
                angle[1] = getBottom();
            }
            case 2 -> angle[1] = getBottom();
        }
        return angle;
    }
}
