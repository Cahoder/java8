package jvm.gc;

public class EdenGCTest {

    private static final int _1MB = 1024 * 1024;

    /**
      -XX:SurvivorRatio=8
      -XX:+PrintGCDetails
      -XX:+PrintGC
      -Xms20M
      -Xmx20M
      -Xmn10M
      -XX:MaxTenuringThreshold=15
      -XX:+PrintTenuringDistribution
     */
    public static void main(String[] args) {
        byte[] allocation1, allocation2, allocation3, allocation4;
        /*allocation1 = new byte[2 * _1MB];
        allocation2 = new byte[2 * _1MB];
        allocation3 = new byte[2 * _1MB];
        // 出现一次 Minor GC
        allocation4 = new byte[4 * _1MB];*/

        allocation1 = new byte[_1MB / 4];
        allocation2 = new byte[_1MB / 4];
        allocation3 = new byte[4 * _1MB];
        allocation4 = new byte[4 * _1MB];
        allocation4 = null;
    }

}