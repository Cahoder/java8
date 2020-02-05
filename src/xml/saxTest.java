package xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

/**
 * 测试xml文件的解析和创建
 * ps: sax是基于事件驱动的xml解析器
 * 无需将整个xml一次性加载到内存,属于边读边解析,内存消耗较少,适合解析特别大的xml文件
 * @see org.xml.sax.XMLReader
 */
public class saxTest {

    public static void main(String[] args) {

        try {
            //创建sax解析工厂
            SAXParserFactory saxFactory = SAXParserFactory.newInstance();

            //通过sax工厂获取sax解析器
            SAXParser saxParser = saxFactory.newSAXParser();

            //通过sax解析器的parse方法
            saxParser.parse("src/xml/periodic_table.xml",new DefaultHandler(){
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    System.out.println(qName);
                }

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    System.out.print(new String(ch,start,length));
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    System.out.println(qName);
                }
            });


        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

    }

}
