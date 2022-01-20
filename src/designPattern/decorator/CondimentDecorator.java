package designPattern.decorator;

/**
 * 调味装饰器
 * @author caihd
 * @date 2022-1-20
 */
public abstract class CondimentDecorator implements Beverage {
    protected Beverage beverage;
}
