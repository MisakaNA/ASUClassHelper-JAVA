import java.io.IOException;
import java.util.List;

public class FunctionTester {
    public static void main(String[] args) throws IOException {
        ASUClassFinder finder = new ASUClassFinder("Spring 2021","cse 486", false);
        ProfessorRating pr = new ProfessorRating("Yinong Chen");
        pr.getRatingInfo();
        System.out.println();
    }
}
