package tajmi.som;

import org.junit.Test;
import java.util.*;
import java.io.*;
import java.util.regex.Pattern;
import tajmi.abstracts.som.ShowStatusFunc;
import tajmi.frontends.SOMMaker;
import tajmi.instances.vectorial.Vector;

/**
 *
 * @author badi
 */
public class SOMTest {
    
    @Test
    public void TestSOM() throws FileNotFoundException, IOException {
        List<Vector> data = readVectorData("test-data" + File.separator + "yeast.data.short");
        System.out.println(data);

        SOMMaker sommaker = new SOMMaker();
        sommaker.field_size(10, 10);
        sommaker.setShow_status_func(new ShowStatusFunc() {

            @Override
            public Void call() {
                return null;
            }
        });

        SOM<Vector> result = sommaker.makeVectorialSOM(data);

        System.out.println("Running SOM");
        result.call();
    }

    public List<Vector> readVectorData(String path) throws FileNotFoundException, IOException {

        BufferedReader reader = new BufferedReader(new FileReader(path));
        List<Vector> vs = new LinkedList<Vector>();
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            Pattern p = Pattern.compile("\\s+"); // split on whitespace
            String[] data = p.split(line);
            Vector v = new Vector(data.length);
            for (String d : data) {
                v.add(Double.parseDouble(d));
            }
            vs.add(v);
        }
        return vs;
    }
}