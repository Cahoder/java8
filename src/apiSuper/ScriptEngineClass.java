package apiSuper;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.String;
import java.util.List;

/**
 * 脚本引擎管理，把复杂易变的业务逻辑交给脚本语言处理，可以提高开发效率
 * JDK1.6 ++
 * javax.script.ScriptEngine
 */
public class ScriptEngineClass {

    /**
     * Rhino引擎：基于java编写的javascript的开源实现，原先由Mozilla开发，后来被集成于jdk1.6
     * Nashorn引擎：JDK1.8++替代了Rhino引擎
     * 可以实现java和js之间上下文数据交互
     */
    public void JsScriptEngine(){
        ScriptEngine jsEngine = ScriptEngineClass.getScriptEngine("js");
        System.out.println(jsEngine);
        Invocable jsInvokeFunc = (Invocable) jsEngine;   //调用函数转化成Invocable对象
        try {

            //通过脚本引擎实现 java 和 js 之间的交互
            jsEngine.put("a",1); //定义脚本变量
            jsEngine.put("b",2);
            String jsScriptString = "print(a+b);";
            jsScriptString += "var json = {name:'C.HDe',age:18,school:['广东技术师范大学','广东轻工职业技术学院']};";
            jsScriptString += "json = JSON.stringify(json)";
            jsEngine.eval(jsScriptString);    //执行脚本
            String json = (String)jsEngine.get("json"); //获取变量
            System.out.println(json);   //{"name":"C.HDe","age":18,"school":["广东技术师范大学","广东轻工职业技术学院"]}

            //调用js函数的方式
            jsScriptString = "function sum(a,b){return a+b;}";
            jsEngine.eval(jsScriptString);
            Object sum = jsInvokeFunc.invokeFunction("sum", 10, 20);
            System.out.println(sum);

            //载入js文件的方式
            FileReader fr = new FileReader("./HelloJsEngine.js");
            jsEngine.eval(fr);
            jsInvokeFunc.invokeFunction("getDate");

            //在js文件中导入java的类
            jsScriptString = "var Arrays = Java.type('java.util.Arrays'); var lists = Arrays.asList(\"a\",\"b\",\"c\");";
            jsEngine.eval(jsScriptString);
            List<String> lists = (List<String>)jsEngine.get("lists");
            for (String list : lists) {
                System.out.print(list);
            }

        } catch (ScriptException | NoSuchMethodException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //根据脚本名字获取指定的脚本引擎
    public static ScriptEngine getScriptEngine(String engineName){
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine se = sem.getEngineByName(engineName);
        if (se==null) throw new IllegalArgumentException(engineName + "脚本引擎不存在！");
        return se;
    }
}
