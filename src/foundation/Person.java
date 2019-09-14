package foundation;

import java.lang.String;
import java.util.Objects;

public class Person implements Comparable<Person>{
    private char mySex;
    private int myAge;
    private String myName;

    private void fuckAge(int num){
        if (num>=0 && num<100){
            this.myAge = num;
        }
    }

    public Person() {}

    public Person(char mySex, int myAge, String myName) {
        this.mySex = mySex;
        this.myAge = myAge;
        this.myName = myName;
    }

    private void fuckName(String name){
        this.myName = name;
    }

    private void fuckSex(char sex){
        this.mySex = sex;
    }

    public void setInfo(String name, char sex, int num){
        fuckAge(num);
        fuckSex(sex);
        fuckName(name);
    }

    public void show(){
        System.out.println("你好我叫"+this.myName+",性别"+this.mySex+",今年"+this.myAge+"岁");
    }


    @Override
    public String toString() {
        return "Person{" +
                "mySex=" + mySex +
                ", myAge=" + myAge +
                ", myName='" + myName + '\'' +
                '}';
    }

    public String toString(String fuck) {
        return "Person{" +
                "mySex=" + mySex +
                ", myAge=" + myAge +
                ", myName='" + myName + '\'' +
                '}';
    }

    //想要能够使自定义的类实现Collections工具的自定义排序，必须实现Comparable接口的compareTo方法
    @Override
    public int compareTo(Person o) {
        //return 0;  //默认0表示默认需要排序的元素是相同的，无序排序
        //return this.myAge - o.myAge; // this.参数 - 对象.参数  ==>  表示升序排序
        //return o.myAge - this.myAge;  // 对象.参数 - this.参数   ==>  表示降序排序
        int result = this.myAge - o.myAge;
        if (result == 0){ result = o.myName.charAt(0) - this.myName.charAt(0);}
        return result;
    }

    public int getMyAge() {
        return myAge;
    }

    public String getMyName() {
        return myName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return mySex == person.mySex &&
                myAge == person.myAge &&
                Objects.equals(myName, person.myName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mySex, myAge, myName);
    }
}
