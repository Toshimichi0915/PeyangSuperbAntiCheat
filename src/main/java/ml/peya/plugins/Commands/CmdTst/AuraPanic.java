package ml.peya.plugins.Commands.CmdTst;

import ml.peya.plugins.*;
import ml.peya.plugins.Detect.*;
import ml.peya.plugins.Enum.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;

public class AuraPanic implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (ErrorMessageSender.unPermMessage(sender, "psr.aurapanic") || ErrorMessageSender.invalidLengthMessage(sender, args, 1, 2))
            return true;

        int sec = 5;

        if (args.length == 2)
        {
            try
            {
                sec = Integer.parseInt(args[1]);
            }
            catch (Exception e)
            {
                sender.sendMessage(MessageEngihe.get("error.aura.notNumeric"));

                return true;
            }
        }

        Player player = Bukkit.getPlayer(args[0]);
        if (player == null)
        {
            sender.sendMessage(MessageEngihe.get("error.playerNotFound"));

            return true;
        }

        String name = player.getDisplayName() + (player.getDisplayName().equals(player.getName()) ? "": (" (" + player.getName() + ") "));

        if(PeyangSuperbAntiCheat.cheatMeta.exists(player.getUniqueId()))
        {
            sender.sendMessage(MessageEngihe.get("error.aura.testingNow"));

            return true;
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("type", "AuraPanicBot");
        map.put("seconds", String.valueOf(sec));

        sender.sendMessage(MessageEngihe.get("message.aura.summon", map));


        DetectType type = DetectType.AURA_PANIC;
        type.setPanicTime(sec);

        NPCConnection.scan(player, type, sender);
        return true;
    }
}
