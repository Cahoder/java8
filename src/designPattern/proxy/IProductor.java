package designPattern.proxy;

/**
 * 生产商
 */
interface IProductor {
    /**
     * 生产商品
     * @param money 厂商售价
     */
    void product(float money);

    /**
     * 售后服务
     * @param money 厂商售后服务价
     */
    void afterSale(float money);
}
