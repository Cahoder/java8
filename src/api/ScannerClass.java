package api;
import java.lang.String;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ScannerClass {

    private Scanner scanner;

    public ScannerClass(){
        //使用匿名方式进行赋值
        this.scanner = new Scanner(System.in);
    }

    private void format(int input){
        System.out.println("你输入到数字是："+ input);
    }

    private void format(String input){
        System.out.println("你输入到字符串是："+ input);
    }

    public void getInput(){
        String scannerInput = this.scanner.next();
        Pattern patternNum = Pattern.compile("[0-9]*");
        Matcher matcherNum = patternNum.matcher(scannerInput);

        //如果正则匹配到了数字
        if (matcherNum.matches()){
            format(Integer.valueOf(scannerInput));
            return;
        }

        format(scannerInput);

    }

}
