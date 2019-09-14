package foundation;


import java.lang.String;

/**
 * 自定义异常类
 * 1.可以继承 RuntimeException（运行期异常可以不处理）      <== RuntimeException extends Exception
 * 2.可以继承 Exception（编译期异常必须处理）             <=== Exception extends Throwable
 */
public class CustomException extends /*RuntimeException,*/ Exception {

    public CustomException(){
        super();
    }

    public CustomException(String msg){
        super(msg);
    }

}
