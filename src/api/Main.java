package api;


import java.lang.String;

public class Main {
    public static void main(String[] args) {
//        operationApi();   //常用的api类
        streamApi();      //常用的流api类
    }

    private static void streamApi(){
        //Stream 类
//        FileByteStreamClass byteStream = new FileByteStreamClass();  //字节流
//        byteStream.FileStaticAttribute();
//        byteStream.FileConstructor();
//        byteStream.DirRecursionList("./");
//        byteStream.FileFilterImplement("./");
//        byteStream.FileNameFilterImplement("./");
//        byteStream.FileOutputStream();
//        byteStream.FileInputStream();
//        byteStream.FileCopyMethod("./out","./","copy.out",true);
//        FileCharStreamClass charStream = new FileCharStreamClass();   //字符流
//        charStream.FileCharacterWriter();
//        charStream.FileCharacterReader();
//        PropertiesClass properties = new PropertiesClass();   //Properties集合
//        properties.storeKeyValueToFile();
//        properties.loadKeyValueFromFile();
//        FileBufferStreamClass fbsc = new FileBufferStreamClass();   //缓存流
//        fbsc.FileBufferedInputStream();
//        fbsc.FileBufferedOutputStream();
//        fbsc.FileBufferedCharacterWriter();
//        fbsc.FileBufferedCharacterReader();
//        fbsc.FileBufferedWriterTester();
        FileConvertStreamClass fcs = new FileConvertStreamClass();  //转换流
//        fcs.FileOutputStreamWriter();
//        fcs.FileInputStreamReader();
        fcs.FileCodingConvert();
//        ObjectSerializableStreamClass ossc = new ObjectSerializableStreamClass();   //序列化流
//        ossc.serializeObject();
//        ossc.unSerializeObject();
//        ossc.SerializeCollection();
//        PrintStreamClass psc = new PrintStreamClass();      //打印流
//        psc.outPrintStream();
//        psc.outPrintStreamToLogs();
    }


    private static void operationApi(){
        //Scanner 类
//        ScannerClass scannerClass = new ScannerClass();
//        scannerClass.getInput();

        //Random 类
//        RandomClass randomClass = new RandomClass();
//        randomClass.createRandomNumber();
//        randomClass.createRandomNumber(-100,10);
//        randomClass.randomNumberGame();

        //ArrayListClass 类
//        ArrayListClass arrayListClass = new ArrayListClass();
//        arrayListClass.getArrayListString();
//        arrayListClass.getArrayListInteger();

        //StringClass 类
//        StringClass stringClass = new StringClass();
//        stringClass.createString();
//        stringClass.operString();

        //StaticClass 类
//        StaticClass staticClass1 = new StaticClass("蔡宏德",20);
//        System.out.println(staticClass1);  //其实是调用了该类的toString()方法
//        staticClass1.getInfo();
//        StaticClass staticClass2 = new StaticClass("章子怡",22);
//        staticClass2.getInfo();

        //Math 类
//        MathClass.getMathUtils();

        //Object基类
//        ObjectClass objectClass1 = new ObjectClass("cacaca");
//        ObjectClass objectClass2 = new ObjectClass("cacaca");
//        ObjectClass objectClass3 = null;
//        System.out.println(objectClass1.equals(objectClass2));
//        System.out.println(objectClass1.toString());
//        System.out.println(objectClass1.equals(objectClass3));

        //Date 类
//        DateClass dateClass = new DateClass();
//        dateClass.getDate();
//        dateClass.calculateBirthDay();

        //Calendar 类
//        CalendarClass calendarClass = new CalendarClass();
//        calendarClass.operationCalendar();

        //System 类
//        SystemClass systemClass = new SystemClass();
//        systemClass.getTimeMillis();
//        systemClass.copyArray();

        //StringBuilder 类
//        StringBuilderClass sbc = new StringBuilderClass();
//        sbc.appendStr();

        //包装 类
//        WrapClass wrapClass = new WrapClass();
//        wrapClass.integerWarp();
//        wrapClass.elementDataToString();


    }
}
