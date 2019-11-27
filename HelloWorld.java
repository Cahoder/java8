public class HelloWorld {
    public static void main(String[] args) {
        HelloWorld qf = new HelloWorld();
        qf.twoCitySchedCost(new int[][]{{10,20},{30,200},{400,50},{30,20}});
        System.out.println("hello world!");
    }

    /**
     * 公司计划面试 2N 人。第 i 人飞往 A 市的费用为 costs[i][0]，飞往 B 市的费用为 costs[i][1]。
     * 返回将每个人都飞到某座城市的最低费用，要求每个城市都有 N 人抵达
     */
    /**
     * 解决思路一：假设2N个人都去A城市，然后再选出N个去B城市
     * 改变他们的行程去B城则需要额外支付PriceB-PriceA的费用（可正可负）
     * 选出PriceB-PriceA最小的N个人
     * @param costs
     * @return
     */
    public int twoCitySchedCost(int[][] costs) {
        int EveryCityPeopleNum = costs.length/2;  //每个城市应该去的人数
        int[] changeToBCityExtraCost = new int[costs.length];
        int CostTotal = 0;  //总费用
        for (int i = 0; i < costs.length; i++) {
            CostTotal += costs[i][0];  //假设全部去A城总消费
            changeToBCityExtraCost[i] = costs[i][1]-costs[i][0]; //算出每个人如果改变行程去B城则需要额外支付PriceB-PriceA的费用（可正可负）
        }
        Arrays.sort(changeToBCityExtraCost); //ASC排序
        for (int i = 0; i < EveryCityPeopleNum; i++) {
            CostTotal += changeToBCityExtraCost[i];  //加上前N个最少PriceB-PriceA的费用
        }
        return CostTotal;
    }
}