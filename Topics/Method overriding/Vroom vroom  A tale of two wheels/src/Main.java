import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int speed = scanner.nextInt();
        scanner.close();

        Vehicle car = new Car(speed);
        Vehicle motorcycle = new Motorcycle(speed);

        System.out.println(car.getInfo());
        System.out.println(motorcycle.getInfo());
    }
}

class Vehicle {
    protected int speed;

    public Vehicle(int speed) {
        this.speed = speed;
    }

    public String getInfo() {
        return "";
    }
}

class Car extends Vehicle {
    public Car(int speed) {
        super(speed);
    }
    @Override
    public String getInfo() {
        return "Car: Speed " + speed  + " mph," + " Doors: 4" ;
    }
}

class Motorcycle extends Vehicle {
    public Motorcycle(int speed) {
        super(speed);
    }
    @Override
    public String getInfo() {
        return "Motorcycle: Speed " + speed  + " mph," + " Sidecar: false" ;
    }
}