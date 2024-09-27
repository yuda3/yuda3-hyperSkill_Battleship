class Shape {
    public double area() {
        return 0;
    }
}

class Triangle extends Shape {
    double height;
    double base;

    // override the method here

    @Override
    public double area() {
        return 0.5 * height * base;
    }
}

class Circle extends Shape {
    double radius;

    // override the method here
    @Override
    public double area() {
        return Math.PI * Math.pow(radius, 2);
    }
}

class Square extends Shape {
    double side;

    // override the method here
    @Override
    public double area() {
        return Math.pow(side, 2);
    }
}

class Rectangle extends Shape {
    double width;
    double height;

    // override the method here
    @Override
    public double area() {
        return width * height;
    }
}