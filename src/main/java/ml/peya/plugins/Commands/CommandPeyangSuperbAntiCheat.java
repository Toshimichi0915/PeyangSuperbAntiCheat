package ml.peya.plugins.Commands;

import ml.peya.plugins.Enum.*;
import ml.peya.plugins.Gui.*;
import ml.peya.plugins.*;
import ml.peya.plugins.Utils.*;
import net.md_5.bungee.api.chat.*;
import org.bukkit.*;
import org.bukkit.command.*;

import java.sql.*;
import java.util.*;

public class CommandPeyangSuperbAntiCheat implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!sender.hasPermission("psr.admin"))
        {
            sender.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "エラー: あなたには権限がありません！");
            return true;
        }

        if (args.length == 0)
        {
            sender.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "エラー：変数が不足しています。/" + label + " help でヘルプを見てください。");
            return true;
        }

        switch (args[0])
        {
            case "help":

                sender.sendMessage(ChatColor.AQUA + "-----" +
                        ChatColor.GREEN + "[" +
                        ChatColor.BLUE + "PeyangSuperbAntiCheat" +
                        ChatColor.GREEN + "]" +
                        ChatColor.AQUA + "-----");
                sender.sendMessage(ChatColor.AQUA + "/" + label + " view");
                sender.sendMessage(ChatColor.GREEN + "view");
                break;
            case "view":
                sender.sendMessage(ChatColor.AQUA + "-----" +
                        ChatColor.GREEN + "[" +
                        ChatColor.BLUE + "PeyangSuperbAntiCheat" +
                        ChatColor.GREEN + "]" +
                        ChatColor.AQUA + "-----");
                int start = 0;
                int next;
                int previous;
                try
                {
                    if (args.length == 2)
                        start = Integer.parseInt(args[1]);
                }
                catch (Exception e)
                {
                    sender.sendMessage(ChatColor.RED + ChatColor.BOLD.toString()  + "エラー: 取得開始指定が数字ではありません！");
                    return true;
                }

                next = start + 5;
                previous = start - 5;

                TextComponent nextBtn = TextBuilder.getNextButton(next);
                TextComponent prevBtn= TextBuilder.getPrevButton(previous);

                int count = 0;

                try (Connection connection = PeyangSuperbAntiCheat.hManager.getConnection();
                Statement statement = connection.createStatement())
                {
                    ResultSet result = statement.executeQuery("SELECT * FROM watcheye LIMIT 5 OFFSET " + start);
                    while (result.next())
                    {
                        String id = result.getString("ID");
                        String issuebyid = result.getString("ISSUEBYID");
                        String mngid = result.getString("MNGID");

                        ResultSet reason = statement.executeQuery("SELECT * FROM watchreason WHERE MNGID='" + mngid + "'");

                        ArrayList<EnumCheatType> types = new ArrayList<>();
                        while(reason.next())
                            types.add(CheatTypeUtils.getCheatTypeFromString(reason.getString("REASON")));
                        ComponentBuilder line = TextBuilder.getLine(id, issuebyid, types, mngid);

                        sender.spigot().sendMessage(line.create());
                        count++;
                    }


                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

                sender.spigot().sendMessage(TextBuilder.getNextPrevButtonText(prevBtn, nextBtn, !(previous < 0) , !(count < 5)).create());
                break;
            case "show":
                if (args.)
        }

        return true;

    }
}
