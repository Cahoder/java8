package foundation;

// Using finalize() to detect an object that
// hasn't been properly cleaned up.
public class BookClass {
    boolean checkedOut = false;
    BookClass(boolean checkOut) {
        checkedOut = checkOut;
    }
    void checkIn() {
        checkedOut = false;
    }

    @Override
    //相当于析构函数,一旦这个对象会被gc回收,就会触发此函数
    public void finalize() {
        if(checkedOut)
            System.out.println("Error: checked out");
    }
}
