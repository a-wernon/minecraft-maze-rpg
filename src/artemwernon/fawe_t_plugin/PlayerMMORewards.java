package artemwernon.fawe_t_plugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import static artemwernon.fawe_t_plugin.FileArrayProvider.readLines;

public class PlayerMMORewards {
    Random r;
    void PlayerMMORewards(){

    }
    java.lang.String paa = "D:\\schem_stor\\item_gen\\";
    java.lang.String[][] TierNames;
    int[][] LevelChances;
    String temp;
    int[][] LevelMinMax;
    public void getDropChances() throws FileNotFoundException {
        java.lang.String filename = paa + "dropchances";
        File file = new File(filename);
        Scanner sc = new Scanner(file);
        int grquan = sc.nextInt();
        TierNames = new java.lang.String[grquan][];
        LevelChances = new int[grquan][];
        LevelMinMax = new int[grquan][];
        for (int i = 0; i < grquan; ++i){
            int lmi, lma;
            lmi = sc.nextInt();
            lma = sc.nextInt();
            LevelMinMax[i] = new int[]{lmi, lma};
            int qti = sc.nextInt();
            LevelChances[i] = new int[qti];
            TierNames[i] = new java.lang.String[qti];
            for (int j =0; j < qti; ++j){
                LevelChances[i][j] = sc.nextInt();
                TierNames[i][j] = sc.nextLine().replaceAll(" ", "");
            }
        }
        //System.out.println(TierNames.length);
        r = new Random();
    }
    public int getSectionNumber(int level){
        for (int i = 0; i < LevelMinMax.length; ++i){
            if(LevelMinMax[i][0] <= level && LevelMinMax[i][1] > level){
                return i;
            }
        }
        System.out.println("Section errrrorr");
        return 0;
    }

    public String getItemTier(int level){
        int Se = getSectionNumber(level);
        int su = 0;
        for(int i = 0; i < LevelChances[Se].length; ++i){
            su += LevelChances[Se][i];
        }
        //System.out.println("su " + su);
        int pred = r.nextInt(su);
        //System.out.println(pred + "/" + su);
        int pref = LevelChances[Se][0];
        int res = 0;
        //System.out.println(pref + " 0");
        while(pref <= pred && res < LevelChances[Se].length){
            res++;
            pref += LevelChances[Se][res];
            System.out.println(pref + " " + res);
        }
        //System.out.println(TierNames[res].length);
        //System.out.println(TierNames[Se][res]);
        return TierNames[Se][res];
    }
    String[] ittypes;
    public String getItemType(String clas){
        if (clas.toLowerCase().equals("warrior")){
            ittypes = new String[]{"SWORD","ARMOR"};
            return ittypes[r.nextInt(ittypes.length)];
        }
        else{
            System.out.println("no class in database");
            return "hueta";
        }
    }
    public String getItemId(String typ, String clas, String tier) throws IOException {
        String itempath = paa + clas.toLowerCase() + "//" + tier + "//" + typ;
        String[] ids = readLines(itempath);
        return ids[r.nextInt(ids.length)];
    }
    public void giveMMOReward(Player player, int level, String clas) throws IOException {
        //System.out.println("level " + level + " clas" + clas);
        String tier = getItemTier(level);
        String typ = getItemType(clas);
        String id = getItemId(typ, clas, tier);
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mi " + typ + " " + id + " " + player.getName());
        System.out.println("mi " + typ + " " + id + " " + player.getName());
    }
}
