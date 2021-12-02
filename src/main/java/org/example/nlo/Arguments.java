package org.example.nlo;

public class Arguments {

    private final double a;
    private final double b;
    private final double currentX;

    private Arguments(double a, double b, double currentX) {
        this.a = a;
        this.b = b;
        this.currentX = currentX;
    }

    public double a() {
        return a;
    }

    public double b() {
        return b;
    }

    public double currentX() {
        return currentX;
    }

    public static Arguments of(String[] args) {
        int length = args.length;
        if (length < 3) {
            throw new IllegalArgumentException("Args is too little!");
        }

        double a = Double.parseDouble(args[0]);
        double b = Double.parseDouble(args[1]);
        double currentX = Double.parseDouble(args[2]);

        return new Arguments(a, b, currentX);
    }
}
