package artemwernon.fawe_t_plugin;

import javafx.util.Pair;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DivQuest{
    public String type = "";
    public String rgstart = "";
    public String rgfinish = "";
    public String rgfinish2 = "";
    public int npcgiverid;
    public String mask = "";
    public int level;
    public String name;
    public String ask = "notset";
    public String finish = "notset";
    public String[] questreq = {}; //<questreq> : <reqquests>
    public String qname;
    public String[] questblock = {}; //<questblock> : <blockquests>

    public static void DivQuest(String[] args) {

    }


    public void setType(String t){
        this.type = t;
    }

    static String readFile(String path)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded);
    }

    public String makeFlat3(String[] arg, String argnam){  //here 3 is quan of \t that is appended to elements
        if (arg.length == 0){
            return "";
        }
        String res = argnam + ":";
        for (int i = 0; i < arg.length; ++i){
            res += "\n      - " + arg[i];
        }
        return res;
    }

    public void saveQuest(String filename) throws IOException {
        String q_pa = "D:\\mcserv\\plugins\\Quests\\quests.yml";
        //System.out.println("Hello Java!2");
        this.mask = readFile(filename);
        mask = mask.replaceAll("<qname>", qname);
        //System.out.println(mask);
        mask = mask.replaceAll("<name>", name);
        mask = mask.replaceAll("<ask>", ask);
        mask = mask.replaceAll("<finish>", finish);
        mask = mask.replaceAll("<npcgiverid>", npcgiverid + "");
        mask = mask.replaceAll("<rgstart>", rgstart);
        mask = mask.replaceAll("<rgfinish>", rgfinish);
        mask = mask.replaceAll("<rgfinish2>", rgfinish2);
        mask = mask.replaceAll("<level>", level + "");
        System.out.println(makeFlat3(questreq, "quests"));
        System.out.println(questreq.length);
        System.out.println(makeFlat3(questblock, "quests"));
        System.out.println(questblock.length);
        mask = mask.replaceAll("<questreq> : <reqquests>", makeFlat3(questreq, "quests"));
        mask = mask.replaceAll("<questblock> : <blockquests>", makeFlat3(questblock, "quest-blocks"));
        try {
            FileWriter writer = new FileWriter(q_pa, true);
            BufferedWriter bufferWriter = new BufferedWriter(writer);
            bufferWriter.write(mask);
            bufferWriter.close();
            //System.out.println(mask);
        }
        catch (IOException e) {
            System.out.println("treant");
            System.out.println(e);
        }

    }

    public void setParam(){
        name = "newmob";
        String te = "te";
        ask = te;
        finish = te;
        npcgiverid = 43;
        rgstart = "reg_quar_32_150_-32";
        rgfinish = "room_36_154_-28";
        level = 1;
        qname = rgstart + "_" + level;
    }



}
