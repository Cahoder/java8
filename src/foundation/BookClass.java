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
    public void finalize() {
        if(checkedOut)
            System.out.println("Error: checked out");
    }
}
