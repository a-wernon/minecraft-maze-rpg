package artemwernon.fawe_t_plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

public class QuestGen implements CommandExecutor {
    String[] generatedquests; //add method to get all gen quests

    void createSecretQuest(){

    }

    void createMobQuest(Player player, Vector<int[]> coords, int room_q, int level, int f_co_full, int s_co_full){
        System.out.println(level + "");
        System.out.println(coords.size());
        System.out.println(coords.get(0).length);

        String questfile = "D:\\schem_stor\\masks\\mob.yml";
        if (room_q != 1){
            player.chat("strange mob quar with > 1 room");
        }
        else{
            DivQuest qtd = new DivQuest();
            qtd.ask = "Wanna kill some zombies?";
            qtd.finish = "Area cleared!";
            qtd.level = level;
            qtd.npcgiverid = WGRegBuild.npid("announ_" + f_co_full + "_150_" + s_co_full,player);
            qtd.rgstart = "reg_quar_" + f_co_full + "_150_" + s_co_full;
            qtd.rgfinish = "room_" +(f_co_full + coords.get(0)[0]*4) + "_" + (150 + coords.get(0)[1] *4)+ "_" + (s_co_full +coords.get(0)[2] * 4);
            qtd.rgfinish2 = "";
            qtd.name = "mob challenge " + qtd.rgstart + " level" + qtd.level;
            qtd.qname = qtd.rgstart + "_" + qtd.level;
            qtd.type = "mob";
            try {
                qtd.saveQuest(questfile);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    void createDeliveryQuest(){

    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        org.bukkit.entity.Player player = (Player) sender;
        //player.chat("" + args.length);
        //need two int flags for access to in_rooms/room_out_in_quar
        int f_co = Integer.parseInt(args[0]);
        int s_co = Integer.parseInt(args[1]);
        int f_co_full = f_co * 32;
        int s_co_full = s_co * 32;

        String filename  = "D:\\schem_stor\\in_rooms\\room_out_in_quar_" + args[0] + "_" + args[1] + ".txt";
        player.chat(filename);
        File file = new File(filename);
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            player.chat("hueta");
            e.printStackTrace();
        }
        String room_type = sc.nextLine();

        int room_q = sc.nextInt();
        String gh = sc.nextLine();
        Vector<int[]> coords = new Vector<int[]>();
        if (room_type.equals("mob")){
            if (room_q != 1){
                player.chat("strange mob quar with > 1 room");
            }
            else {
                String c_type = sc.nextLine();
                int[] r_co = {0, 0, 0, 0, 0, 0};
                for (int i = 0; i < r_co.length; ++i) {
                    r_co[i] = sc.nextInt();
                }
                int [] te = {r_co[0], r_co[1], r_co[2]};
                coords.add(te);

                createMobQuest(player, coords, room_q, 1, f_co_full, s_co_full);
                createMobQuest(player, coords, room_q, 2, f_co_full, s_co_full);
                createMobQuest(player, coords, room_q, 3, f_co_full, s_co_full);
                createMobQuest(player, coords, room_q, 4, f_co_full, s_co_full);
            }
            //player.chat("1");
        }
        if (room_type.equalsIgnoreCase("delivery")){
            player.chat(room_type);
            String questfile = "D:\\schem_stor\\masks\\deliver.yml";
            if (room_q != 3){
                player.chat("strange deliv quar with !=3 room");
            }
            else{
                String c_type = sc.nextLine();
                int[] r_co1 = {0,0,0,0,0,0,0};
                int[] r_co2 = {0,0,0,0,0,0,0};
                int[] r_co3 = {0,0,0,0,0,0,0};
                for(int i = 0; i < r_co1.length; ++i){
                    r_co1[i] = sc.nextInt();
                }
                int d1 = sc.nextInt();
                String stte = sc.nextLine();
                for (int i = 0; i < d1; ++i){
                    stte = sc.nextLine();
                }
                stte = sc.nextLine();
                //System.out.println("1");
                for(int i = 0; i < r_co2.length; ++i){
                    r_co2[i] = sc.nextInt();
                }
                //System.out.println("2");
                int d2 = sc.nextInt();
                stte = sc.nextLine();
                for (int i = 0; i < d2; ++i){
                    stte = sc.nextLine();
                }
                stte = sc.nextLine();
                for(int i = 0; i < r_co3.length; ++i){
                    r_co3[i] = sc.nextInt();
                }
                //System.out.println("3");

                String f_room = "room_" +(f_co_full + r_co1[0]*4) + "_" + (150 + r_co1[1]*4)+ "_" + (s_co_full + r_co1[2]*4);
                String s_room = "room_" +(f_co_full + r_co2[0]*4) + "_" + (150 + r_co2[1]*4)+ "_" + (s_co_full + r_co2[2]*4);
                String t_room = "room_" +(f_co_full + r_co3[0]*4) + "_" + (150 + r_co3[1]*4)+ "_" + (s_co_full + r_co3[2]*4);
                DivQuest qtd = new DivQuest();
                qtd.ask = "I know a guy, that needs a delivery boy";
                qtd.finish = "Special delivery!";

                qtd.level = 1;
                String un_rgstart = "reg_quar_" + f_co_full + "_150_" + s_co_full;
                //qtd.rgfinish = "room_" +(f_co_full + r_co1[0]*4) + "_" + (150 + r_co1[1]*4)+ "_" + (s_co_full + r_co1[2]*4);
                //qtd.name = "reg_quar_" + f_co_full + "_150_" + s_co_full +  "_" + qtd.level;
                qtd.type = "deliver";
                qtd.rgstart = f_room;
                qtd.npcgiverid = WGRegBuild.npid(qtd.rgstart,player);
                qtd.qname = "delivery_" + qtd.rgstart;
                qtd.name = "delivery_" + qtd.rgstart;
                qtd.rgfinish = s_room;
                qtd.rgfinish2 = t_room;
                qtd.questblock = new String[]{"delivery_" + qtd.rgfinish, "delivery_" + qtd.rgfinish2};
                qtd.rgstart = un_rgstart;
                try {
                    qtd.saveQuest(questfile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                qtd.rgstart = s_room;
                qtd.npcgiverid = WGRegBuild.npid(qtd.rgstart,player);
                qtd.qname = "delivery_" + qtd.rgstart;
                qtd.name = "delivery_" + qtd.rgstart;
                qtd.rgfinish = f_room;
                qtd.rgfinish2 = t_room;
                qtd.questblock = new String[]{"delivery_" + qtd.rgfinish, "delivery_" + qtd.rgfinish2};
                qtd.rgstart = un_rgstart;
                try {
                    qtd.saveQuest(questfile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                qtd.rgstart = t_room;
                qtd.npcgiverid = WGRegBuild.npid(qtd.rgstart,player);
                qtd.qname = "delivery_" + qtd.rgstart;
                qtd.name = "delivery_" + qtd.rgstart;
                qtd.rgfinish = s_room;
                qtd.rgfinish2 = f_room;
                qtd.questblock = new String[]{"delivery_" + qtd.rgfinish, "delivery_" + qtd.rgfinish2};
                qtd.rgstart = un_rgstart;
                try {
                    qtd.saveQuest(questfile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        if(room_type.equals("secret") || room_type.equals("chill")){
            player.chat(room_type);
            String questfile = "D:\\schem_stor\\masks\\secret.yml";
            if (room_q != 1){
                player.chat("strange secret quar with > 1 room");
            }
            else{
                String c_type = sc.nextLine();
                int[] r_co = {0,0,0,0,0,0};
                for(int i = 0; i < r_co.length; ++i){
                    r_co[i] = sc.nextInt();
                }
                DivQuest qtd = new DivQuest();
                qtd.ask = "Find mystery area";
                qtd.finish = "Wow, you finally found it!";
                qtd.level = 1;
                qtd.npcgiverid = WGRegBuild.npid("announ_" + f_co_full + "_150_" + s_co_full,player);
                qtd.type = "secret";
                qtd.rgstart = "reg_quar_" + f_co_full + "_150_" + s_co_full;
                qtd.rgfinish = "room_" +(f_co_full + r_co[0]*4) + "_" + (150 + r_co[1]*4)+ "_" + (s_co_full + r_co[2]*4);
                qtd.name = "secret room " + qtd.rgstart;
                qtd.qname = qtd.rgstart + "_" + qtd.level;
                try {
                    qtd.saveQuest(questfile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //generate mob quests
        //player.chat(room_type);
        return true;
    }
}
