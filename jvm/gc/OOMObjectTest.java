package src.jvm.gc;

import java.util.ArrayList;
import java.util.List;

public class OOMObjectTest {

    public byte[] placeholder = new byte[64 * 1024];

    public static void fillHeap(int num) throws InterruptedException {
        List<OOMObjectTest> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            // 稍作延时，令监视曲线的变化更加明显
            Thread.sleep(50);
            list.add(new OOMObjectTest());
        }
        System.gc();
    }

    /**
     * -Xms100m -Xmx100m -XX:+UseSerialGC
     */
    public static void main(String[] args) throws InterruptedException {
        fillHeap(1000);
        while (true);
    }

}