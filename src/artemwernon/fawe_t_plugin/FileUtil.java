package artemwernon.fawe_t_plugin;
import java.io.*;

public class FileUtil {
    public static boolean fileSame(String f_file, String s_file) throws FileNotFoundException, IOException {

        File f1 = new File(f_file);// OUTFILE
        File f2 = new File(s_file);// INPUT

        FileReader fR1 = new FileReader(f1);
        FileReader fR2 = new FileReader(f2);

        BufferedReader reader1 = new BufferedReader(fR1);
        BufferedReader reader2 = new BufferedReader(fR2);

        String line1 = null;
        String line2 = null;
        boolean flag = true;
        while ((flag == true) && ((line1 = reader1.readLine()) != null)
                && ((line2 = reader2.readLine()) != null)) {
            if (!line1.equalsIgnoreCase(line2))
                flag = false;
        }
        reader1.close();
        reader2.close();
        //System.out.println("Flag " + flag);
        return flag;
    }
}
