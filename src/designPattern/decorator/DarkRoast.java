package designPattern.decorator;

/**
 * 焦糖咖啡
 * @author caihd
 * @date 2022-1-20
 */
public class DarkRoast implements Beverage {
    @Override
    public double cost() {
        return 1;
    }
}
