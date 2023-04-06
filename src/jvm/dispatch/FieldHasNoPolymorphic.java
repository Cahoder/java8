package jvm.dispatch;

/**
 * 成员方法存在多态
 * 成员属性不存在多态
 * 每个类只会看到己类里的同名属性
 **/
public class FieldHasNoPolymorphic {

    static class Father {
        public int money = 1;
        public Father() {
            money = 2;
            showMeTheMoney();
        }
        public void showMeTheMoney() {
            System.out.println("I am Father, i have $" + money);
        }
    }
    static class Son extends Father {
        public int money = 3;
        public Son() {
            //super();  //此行编译器会自动加
            money = 4;
            showMeTheMoney();
        }
        @Override
        public void showMeTheMoney() {
            System.out.println("I am Son, i have $" + money);
        }
    }
    public static void main(String[] args) {
        Father gay = new Son();
        System.out.println("This gay has $" + gay.money);
    }


}
