package javassist;

import java.lang.String;

public class Main {
    public static void main(String[] args) throws Exception {
        CompileTimeApiClass ctc = new CompileTimeApiClass();
        ctc.CtClassMakeClass();
        ctc.CtClassUseClass();
    }
}
