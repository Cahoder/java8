package api;
import java.util.Random;
import java.util.Scanner;

public class RandomClass {

    private Random random;

    public RandomClass(){
        this.random = new Random();
    }


    public void createRandomNumber(){
        //范围是在int类型的取值范围内
        System.out.println("产生的无范围随机数是："+ this.random.nextInt());
    }

    public void createRandomNumber(int min, int max){
        //random.nextInt(int params) 产出的是 【x|0<= x < params】
        System.out.println("产生的有范围随机数是："+ (random.nextInt(max - min + 1) + min));
    }

    public void randomNumberGame(){
        int number = this.random.nextInt(100)+1;
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入你猜测的数字：");
        while (true){
            int inputNumber = scanner.nextInt();
            if (number>inputNumber){
                System.out.println("输入的数字太小了，请重试：");
                continue;
            }

            if (number<inputNumber){
                System.out.println("输入的数字太大了，请重试：");
                continue;
            }

            System.out.println("恭喜你，猜对了！");
            break;
        }
    }
}
