package bg.tu_varna.sit.oop1;

public class Main {

    public static void main(String[] args) {
	// write your code here
        try {
            Student student = new Student("Vasil", 0, "Test", 2);
            System.out.println(student.getStatus());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
