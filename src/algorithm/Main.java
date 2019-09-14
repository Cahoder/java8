package algorithm;

import java.lang.String;

public class Main {
    public static void main(String[] args) {

    }

    //欧几里德求最大公约数算法
    private static int gcd(int p,int q){
        if (q == 0) return p;
        int r = p % q;
        return gcd(q,r);
    }
}
