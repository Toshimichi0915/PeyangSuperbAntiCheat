package ml.peya.plugins.Commands;

import ml.peya.plugins.*;
import ml.peya.plugins.Gui.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.scheduler.*;

public class CommandTarget implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(MessageEngihe.get("error.requirePlayer"));
            return true;
        }

        if (ErrorMessageSender.invalidLengthMessage(sender, args, 1, 1))
            return true;
        if (ErrorMessageSender.unPermMessage(sender, "psac.target"))
            return true;

        Player player = Bukkit.getPlayer(args[0]);

        if (player == null)
        {
            sender.sendMessage(MessageEngihe.get("error.playerNotFound"));
            return true;
        }

        new BukkitRunnable()
        {

            @Override
            public void run()
            {
                ((Player) sender).performCommand("bans -a " + player.getName());
                this.cancel();
            }
        }.runTaskLater(PeyangSuperbAntiCheat.getPlugin(), 15L);

        GuiItem.giveAllItems((Player) sender, player.getName());
        return true;
    }
}