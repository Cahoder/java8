package designPattern.decorator;

/**
 * 牛奶+配料
 * @author caihd
 * @date 2022-1-20
 */
public class Milk extends CondimentDecorator {

    public Milk(Beverage beverage) {
        this.beverage = beverage;
    }

    @Override
    public double cost() {
        return 1 + beverage.cost();
    }

}
