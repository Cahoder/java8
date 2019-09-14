package api;

import java.io.*;
import java.lang.String;
import java.util.Arrays;
import java.util.Scanner;

/**
 * 以字节为单位进行流操作
 */
public class FileByteStreamClass {

    public void FileStaticAttribute(){
        /*
          名称分隔符
          windows 反斜杠 \
          linux 正斜杠 /
         */
        //File Static char separatorChar  与系统有关的默认名称分隔符
        System.out.println(File.separatorChar);
        //File Static String separator  与系统有关的默认名称分隔符，为了方便，它被表示为一个字符串
        System.out.println(File.separator);

        /*
           路径分隔符
           windows 分号 ;
           linux 冒号 :
        */
        //File Static char pathSeparatorChar 与系统有关的路径分隔符
        System.out.println(File.pathSeparatorChar);
        //File Static String pathSeparator 与系统有关的路径分隔符，为了方便，它被表示为一个字符串
        System.out.println(File.pathSeparator);
    }

    public void FileConstructor(){
        String parent = "/";
        String child = "home/cahoder/IdeaProjects/java8";
        File f1 = new File(parent,child);
        System.out.println(f1); //   /home/cahoder/IdeaProjects/java8
        File f2 = new File(f1,"a.txt");
        System.out.println(f2); //  /home/cahoder/IdeaProjects/java8/a.txt
        File f3 = new File("/home/cahoder/IdeaProjects/java8");
        System.out.println(f3); //   /home/cahoder/IdeaProjects/java8

        //获取文件/文件夹信息
        File f4 = new File("a.txt");
        System.out.println(f4.getAbsolutePath()); //    /home/cahoder/IdeaProjects/java8/a.txt    始终返回绝对路径
        System.out.println(f4.getPath()); //   a.txt    根据构造函数创建的路径
        System.out.println(f4.getName()); //   a.txt    获取文件/文件夹名
        System.out.println(f4.length()); //    0        获取文件/文件夹 大小


        //判断文件/文件夹
        System.out.println(f4.exists());       //判断文件/文件夹是否真实存在
        System.out.println(f3.isDirectory());  //判断路径是否一个文件夹结尾(前提存在)
        System.out.println(f4.isFile());       //判断路径是否一个文件结尾(前提存在)


        // 文件/文件夹 的创建删除
        /*
        1.文件的创建 fileObj.createNewFile()
            路径不存在会抛出异常
            只能用于创建文件

        2.文件夹的创建
            fileObj.mkdir()
                只能够创建单级文件夹
                路径不存在不会抛出异常
            fileObj.mkdirs()
                能够创建单/多级文件夹
                路径不存在不会抛出异常

        3.文件/文件夹 的删除
            fileObj.delete()
                文件夹内有内容删除失败
                文件/文件夹路径错误删除失败
                直接进行物理删除，谨慎操作
        */
    }

    //文件夹的遍历

    /**
     * 前提是文件夹路径存在
     * @exception NullPointerException
     *
     * 1.File String[] list()        返回一个字符串数组，这些字符串指定此抽象路径名表示的目录中的文件和目录
     *
         String[] list = dir.list();
         for (String s : list) {
             System.out.println(s);
         }
     *
     * 2.File File[] listFiles()     返回一个抽象路径名数组，这些路径名表示此抽象路径名表示的目录中的文件
     */
    public void DirRecursionList(String filePath){
        File path = new File(filePath);

        if (!path.exists()) {
            System.out.println("路径不存在!");
            return;
        }
        if (!path.isDirectory()){
            System.out.println("文件夹不存在!");
            return;
        }

        System.out.println("\n---"+ path.getPath());
        File[] filesList = path.listFiles();
        if (filesList != null) {
            for (File f : filesList) {
                if (f.isDirectory()) {
                    DirRecursionList(f.getPath());
                }
                else System.out.println(f.getName());
            }
        }
    }



    /**
     * 文件过滤器
     * 使用FileFilter 测试指定抽象路径名是否应该包含在某个路径名列表中
     * */
    public void FileFilterImplement(String filePath){
        File path = new File(filePath);

        if (!path.exists()) {
            System.out.println("路径不存在!");
            return;
        }
        if (!path.isDirectory()){
            System.out.println("文件夹不存在!");
            return;
        }


        // lambda简写方式 ：
        File[] filesList = path.listFiles(file -> file.isDirectory() || file.getName().toLowerCase().endsWith(".java"));
//        File[] filesList = path.listFiles(new FileFilter() {
//            @Override
//            public boolean accept(File file) {
//                return file.isDirectory() || file.getName().toLowerCase().endsWith(".java");
//            }
//        });
        if (filesList != null) {
            for (File f : filesList) {
                if (f.isDirectory()) {
                    FileFilterImplement(f.getPath());
                }
                else System.out.println(f.getName());
            }
        }
    }

    /**
     * 文件过滤器
     * 使用FileNameFilter 测试指定文件是否应该包含在某一文件列表中
     * */
    public void FileNameFilterImplement(String filePath){
        File path = new File(filePath);

        if (!path.exists()) {
            System.out.println("路径不存在!");
            return;
        }
        if (!path.isDirectory()){
            System.out.println("文件夹不存在!");
            return;
        }


        // lambda简写方式 ：
        File[] filesList = path.listFiles((dir, name) -> new File(dir,name).isDirectory() || name.toLowerCase().endsWith(".java"));
//        File[] filesList = path.listFiles(new FilenameFilter() {
//            @Override
//            public boolean accept(File dir, String name) {
//                return new File(dir,name).isDirectory() || name.toLowerCase().endsWith(".java");
//            }
//        });
        if (filesList != null) {
            for (File f : filesList) {
                if (f.isDirectory()) {
                    FileNameFilterImplement(f.getPath());
                }
                else System.out.println(f.getName());
            }
        }
    }


    /**
     * FileOutputStream extends OutputStream
     * 构造器作用：
     * 1.创建一个OutputStream 对象
     * 2.根据构造器传入的文件/文件路径 -> 在硬盘中创建对应的文件
     * 3.将对象句柄指向新创建的文件
     * 使用步骤：
     * 1.创建流
     * 2.写入流
     * 3.关闭流
     *
     * 注：
     * linux 换行符 \n
     * windows 换行符 \r\n
     * macOs 换行符 \r
     */
    public void FileOutputStream(){
        try {
            FileOutputStream fos = new FileOutputStream("./a.txt");
            byte[] output = new byte[60];
            for (byte i = 0 ; i < output.length; i++) {
                output[i] = (byte) (65+i);
            }
            fos.write(output);
            fos.write(new byte[]{-28, -69, -118, 69});
            fos.write("\n".getBytes());  //linux 换行符 \n    === ASCII 10
            System.out.println(Arrays.toString("\r\n".getBytes()));
            fos.write(10);
            fos.close();

            //追加写
            fos = new FileOutputStream("./a.txt",true);
            byte[] str = "今天天气真好！".getBytes();  //一个中文一般占两个字节，UTF8编码则一般占据3个字节，UTF16编码则占据4个字节
            System.out.println("今天天气真好！ 的字节数组是："+ Arrays.toString(str));
            fos.write(str);
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * FileInputStream extends OutputStream
     * 构造器作用：
     * 1.创建一个InputStream 对象
     * 2.将对象句柄指向构造器传入的文件/文件路径
     * 使用步骤：
     * 1.创建流
     * 2.读取流
     * 3.关闭流
     *
     **/
    public void FileInputStream(){
        try {
            File file = new File("./a.txt");
            FileInputStream fis = new FileInputStream(file);

            //一次读取一个字节
            /*int b = fis.read();  //每次调用read方法读取一个字节之后 文件指针会自动向下移动一位
            System.out.println(b);
            //依次读取文件全部字节
            for (int i = 1; i <= file.length(); i++) {
                b = fis.read();  //遇到文件尾部则会返回 -1
                System.out.println(b);
            }*/

            //一次读取多个字节
            byte[] bytes = new byte[1024];  //用于存储每次缓冲获取的字节数组,一般设置为1024 byte = 1kb 或者1024的整数倍
            int len = 0;   //用于存储每次读取的有效字节数
            while ((len = fis.read(bytes))!=-1){
                System.out.println("有效读取的字节数是： " + len + "个");
                // 通过使用平台的默认字符集解码指定的 byte 子数组，构造一个新的 String
                System.out.println(new String(bytes,0,len));
            }
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 文件复制原理
     * @param fromPath where is the file or directory from path
     * @param toPath where is the file or directory new path
     * @param copyName please input the new file name, if is null it will be a copy + path.getName()
     * @param isDirectory It is the type of the path a file or a directory ?
     *
     */
    public void FileCopyMethod(String fromPath, String toPath, String copyName, boolean isDirectory){
        File sourceFile = new File(fromPath);
        if (!checkFilePath(sourceFile,isDirectory))return;
        if (toPath.equals("")) toPath = ".";
        if (copyName.equals("")) copyName = "copy." + sourceFile.getName();

        //根据文件类型进行拷贝
        if (isDirectory){
            copyDirectoryFiles(sourceFile,toPath,copyName);
        }else {
            copySingleFile(sourceFile,toPath,copyName);
        }
    }

    //检测文件是否符合条件
    private boolean checkFilePath(File path,boolean isDirectory){
        if (!path.exists()) {
            System.out.println("输入的文件/文件夹路径不存在！");
            return false;
        }
        if (isDirectory && !path.isDirectory()) {
            System.out.println("输入的不是一个文件夹类型噢！");
            return false;
        }
        if (!isDirectory && path.isDirectory()) {
            System.out.println("输入的不是一个文件类型噢！");
            return false;
        }
        return true;
    }

    //单文件的复制
    private void copySingleFile(File copyedFile, String newFilePath, String newFileName) {
        File newFile = new File(newFilePath + "/" + newFileName);
        int conflictCode = 0;

        //文件已经存在需要确认是否进行覆盖
        while (newFile.exists()){
            conflictCode = copyConflict(newFileName);
            if (conflictCode > 0) break;
        }
        if (conflictCode == 400) return;

        //进行IO复制操作
        try {
            FileInputStream oldFileStream = new FileInputStream(copyedFile);
            FileOutputStream newFileStream = new FileOutputStream(newFile,true);
            if (conflictCode == 200)  newFileStream = new FileOutputStream(newFile);

            FILEIOCOPYFILE(oldFileStream,newFileStream); //io流操作
        }catch (IOException e){
            System.out.println(newFileName + " 文件复制出错:"+ e.getMessage());
        }
    }

    //文件夹的复制
    private void copyDirectoryFiles(File copyedPath, String newSavePath, String newSaveName){
        if (!copyedPath.isDirectory()) return;
        File[] files = copyedPath.listFiles();
        if ((files != null ? files.length : 0) == 0) {
            //System.out.println("文件夹为空，复制取消！");
            return;
        }

        //遍历当前文件夹下的文件
        for (File file : files) {
            File newSubPath = new File(newSavePath + "/" + newSaveName + "/" + file.getName());
            if (file.isDirectory()){
                //如果是文件夹
                if (!newSubPath.exists()) newSubPath.mkdirs();
                copyDirectoryFiles(file,newSubPath.getParent(),file.getName());
            }else {
                //如果是文件
                copySingleFile(file,newSubPath.getParent(),file.getName());
            }
        }
    }

    //文件已经存在需要用户进行确认操作
    private int copyConflict(String newFileName){
        Scanner scan = new Scanner(System.in);
        System.out.println("Errors:复制出现冲突: \""+newFileName+"\" 文件已经存在了！");
        System.out.println("请确认是否进行覆盖：（Yes进行覆盖| No停止操作| Append追加写入| AppendALL对之后的文件都进行追加写入）");
        String userInput = scan.next().toLowerCase();

        if (userInput.equals("yes")) return 200;
        if (userInput.equals("append")) return 202;
        if (userInput.equals("appendall")) return 204;
        if (userInput.equals("no")) return 400;
        return 0;
    }


    // I/O 输入流 到 输出流
    private void FILEIOCOPYFILE (FileInputStream inputStream , FileOutputStream outputStream) throws IOException {
        if (inputStream == null || outputStream ==null) return;

        byte[] bytes = new byte[1024];  //用于存储每次缓冲获取的字节数组,一般设置为1024 byte = 1kb 或者1024的整数倍
        int validByte = 0;   //用于存储每次读取的有效字节数

        while ((validByte = inputStream.read(bytes))!=-1){
            outputStream.write(bytes,0, validByte);
        }

        /*必须先关闭写后关闭读: 因为写完了肯定读完了,读完了不一定写完了*/
        outputStream.close();
        inputStream.close();

    }
}
