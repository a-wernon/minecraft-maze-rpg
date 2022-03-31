package artemwernon.fawe_t_plugin;

import javafx.util.Pair;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Scanner;

public class FileArrayProvider {
    public static String[] readLines(String filename) throws IOException {
        FileReader fileReader = new FileReader(filename);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> lines = new ArrayList<String>();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        bufferedReader.close();
        return lines.toArray(new String[lines.size()]);
    }

    public static int[][] read2dlines(String filename) throws IOException {
        /*FileReader fileReader = new FileReader(filename);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        int rows = (int) bufferedReader.readLine();
        List<String> lines = new ArrayList<String>();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        bufferedReader.close();
        return lines.toArray(new String[lines.size()]);*/
        Scanner sc = new Scanner(new BufferedReader(new FileReader(filename)));
        int rows = sc.nextInt();
        int columns = 3;
        int [][] coor = new int[rows][columns];
        while(sc.hasNextInt()) {
            for (int i=0; i<coor.length; i++) {
                //String[] line = sc.nextLine().trim().split(" ");
                for (int j=0; j<columns; j++) {
                    coor[i][j] = sc.nextInt();
                }
            }
        }
        return  coor;
    }

    public static int[][] read2d4lines(String filename) throws IOException {
        /*FileReader fileReader = new FileReader(filename);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        int rows = (int) bufferedReader.readLine();
        List<String> lines = new ArrayList<String>();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        bufferedReader.close();
        return lines.toArray(new String[lines.size()]);*/
        Scanner sc = new Scanner(new BufferedReader(new FileReader(filename)));
        int rows = sc.nextInt();
        int columns = 4;
        int [][] coor = new int[rows][columns];
        //System.out.println(filename);
        while(sc.hasNextInt()) {
            for (int i=0; i<coor.length; i++) {
                //String[] line = sc.nextLine().trim().split(" ");
                for (int j=0; j<columns; j++) {
                    //System.out.println(i + " " + j);
                    coor[i][j] = sc.nextInt();
                }
            }
        }
        return  coor;
    }

    public static Pair<int[][], String[]> readquestlines(String filename, Player player) throws IOException{
        File file = new File(filename);
        Scanner sc = new Scanner(file);
        int rows = sc.nextInt();
        int columns = 9;
        //player.chat("tes");
        int [][] coor = new int[rows][columns];
        String[] typ = new String[rows];
        for (int i=0; i<coor.length; i++) {
            //String[] line = sc.nextLine().trim().split(" ");
            for (int j=0; j<columns; j++) {
                coor[i][j] = sc.nextInt();
                //player.chat("" + i + j);
            }
            String gh = sc.nextLine();
            typ[i] = sc.nextLine();
        }
        int tr = 0;
        tr = sc.nextInt();
        tr = sc.nextInt();
        tr = sc.nextInt();
        Pair<int[][], String[]> ans= new Pair(coor, typ);
        return ans;
    }
}