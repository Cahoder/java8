package api;

import java.lang.String;
import java.util.Objects;

public class ObjectClass {

    private String name;


    public ObjectClass(String name){
        this.name = name;
    }

    @Override
    //重写基类的equals方法
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (obj instanceof ObjectClass){
            return Objects.equals(this.name,((ObjectClass) obj).name);   //Objects.equals 容忍空指针
        }
        return false;
    }

    @Override
    //重写基类的toString方法
    public String toString() {
//        return apiInterface.toString();
        return this.name;
    }
}
