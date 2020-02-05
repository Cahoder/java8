package xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.util.Iterator;

/**
 * 测试xml文件的解析和创建
 * ps: dom4j是一次性将xml加载到内存
 */
public class dom4jTest {

    /**
     * dom4j也支持xpath语法，但是需要 javaxen.jar 包的支持
     *
     * XPath 使用路径表达式来选取 XML 文档中的节点或节点集。节点是通过沿着路径 (path) 或者步 (steps) 来选取的。
     *
     常用语法如下:

       表达式	    描述
       nodename	    选取此节点的所有子节点。
       /	        从根节点选取。
       //	        从匹配选择的当前节点选择文档中的节点，而不考虑它们的位置。
       .	        选取当前节点。
       ..	        选取当前节点的父节点。
       @	        选取属性。

     * 更多语法 @link https://www.w3school.com.cn/xpath/xpath_syntax.asp
     */
    public static void xPathParseTest(){
        try {
            //创建解析器
            SAXReader reader = new SAXReader();

            //读取xml文件
            Document document = reader.read("src/xml/periodic_table.xml");

            //通过使用xpath语法获取某个元素; 例如:我们要获取 PERIODIC_TABLE -> 第一个 ATOM -> DENSITY 元素
            Node node = document.selectSingleNode("/PERIODIC_TABLE/ATOM[1]/DENSITY");
            //获取该元素的属性值
            /*
            <DENSITY UNITS="grams/cubic centimeter"><!-- At 300K -->
                10.07
            </DENSITY>
             */
            String attrVal = node.valueOf("@UNITS");
            System.out.println(attrVal);
            //获取该节点元素内的值
            System.out.println(node.getStringValue());

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 普通解析测试
     */
    public static void parseTest(){
        try {
            //创建解析器
            SAXReader reader = new SAXReader();

            //读取xml文件
            Document document = reader.read("src/xml/periodic_table.xml");

            //获取根节点
            Element rootElement = document.getRootElement();

            //遍历根节点
            for (Iterator<Element> it = rootElement.elementIterator("ATOM"); it.hasNext();) {
                Element ATOM = it.next();
                // 再次循环内层节点
                System.out.println(ATOM.getName());
                for (Iterator<Element> innerIt = ATOM.elementIterator();innerIt.hasNext();){
                    Element innerEle = innerIt.next();

                    System.out.println("\t"+innerEle.getName()+": "+innerEle.getStringValue());  //获取内部节点名字+节点内的值
                }
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        //parseTest();
        xPathParseTest();

    }

}
