package designPattern.decorator;

/**
 * 抹茶+调料
 * @author caihd
 * @date 2022-1-20
 */
public class Mocha extends CondimentDecorator {

    public Mocha(Beverage beverage) {
        this.beverage = beverage;
    }

    @Override
    public double cost() {
        return 1 + beverage.cost();
    }
}
