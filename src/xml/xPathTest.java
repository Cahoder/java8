package xml;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;

/**
 * 使用java内置的xml解析器 javax.xml.parsers.DocumentBuilder
 * 解析xml文件获取Document对象树
 * 再使用java内置的XPath分析Document对象树
 */
public class xPathTest {

    public static void main(String[] args) {
        try {
            //创建DocumentBuilderFactory解析工厂
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

            //使用工厂创建DocumentBuilder解析器
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            //解析xml文件，生成一个Document[org.w3c.dom.Document]对象树
            Document document = documentBuilder.parse("src/xml/periodic_table.xml");

            //创建xpath对象工厂
            XPathFactory xPathFactory = XPathFactory.newInstance();

            //使用工厂创建XPATH对象
            XPath xPath = xPathFactory.newXPath();

            //解析Document对象树
            //例如：获取 PERIODIC_TABLE 元素 -> 第二个 ATOM 元素 -> COVALENT_RADIUS 元素，并且它的UNITS属性的值为'Angstroms',
            //直接返回该元素的值text()
            Object evaluate = xPath.evaluate("/PERIODIC_TABLE/ATOM[2]/COVALENT_RADIUS[@UNITS='Angstroms']/text()"
                    , document, XPathConstants.STRING);
            System.out.println(evaluate);

        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
            e.printStackTrace();
        }
    }

}
