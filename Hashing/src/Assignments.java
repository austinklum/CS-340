import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Assignments {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(args[0]));
        SymbolTable hash = new SymbolTable(10);
        String line = "";
        
        while ((line = reader.readLine()) != null) {
            String[] str = line.split(" = ");
            hash.insert(str[0]);
            hash.setValue(str[0], new ExpressionTree(str[1]).evaluate(hash));
        }
        
        for (String string : hash) {
            System.out.println(string);
        }
        
    }
}