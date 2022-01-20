package designPattern.decorator;

/**
 * @author caihd
 * @date 2022-1-20
 */
public class Main {

    public static void main(String[] args) {
        Beverage beverage = new HouseBlend();
        beverage = new Mocha(beverage);
        beverage = new Milk(beverage);
        System.out.println(beverage.cost());    //3.0
    }
}
