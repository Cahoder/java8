package designPattern.decorator;

/**
 * 研磨过滤咖啡粉
 * @author caihd
 * @date 2022-1-20
 */
public class HouseBlend implements Beverage {
    @Override
    public double cost() {
        return 1;
    }
}
