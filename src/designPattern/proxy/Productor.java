package designPattern.proxy;

public class Productor implements IProductor{
    @Override
    public void product(float money) {
        System.out.println("厂商赚了" + money + "元");
    }

    @Override
    public void afterSale(float money) {
        System.out.println("厂商赚了" + money + "元");
    }
}
