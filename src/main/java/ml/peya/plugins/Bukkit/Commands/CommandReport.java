package ml.peya.plugins.Bukkit.Commands;

import ml.peya.plugins.Bukkit.DetectClasses.*;
import ml.peya.plugins.Bukkit.Enum.*;
import ml.peya.plugins.Bukkit.Moderate.*;
import ml.peya.plugins.Bukkit.Utils.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;

import java.util.*;

import static ml.peya.plugins.Bukkit.Utils.MessageEngine.get;
import static ml.peya.plugins.Bukkit.Utils.MessageEngine.pair;
import static ml.peya.plugins.Bukkit.Variables.config;

/**
 * 報告コマンド系クラス。
 */
public class CommandReport implements CommandExecutor
{
    /**
     * コマンド動作。
     *
     * @param sender  イベントsender。
     * @param command コマンド。
     * @param label   ラベル。
     * @param args    引数。
     * @return 正常に終わったかどうか。
     */
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (ErrorMessageSender.unPermMessage(sender, "psac.report"))
            return true;

        if (args.length == 0)
        {
            sender.sendMessage(get("error.minArgs", pair("label", label)));
            return true;
        }
        else if (args.length == 1)
        {
            if (args[0].equals("help"))
            {
                sender.sendMessage(get("base.prefix"));
                sender.sendMessage(get("command.report.help", pair("label", label)));
                return true;
            }

            if (args[0].equals("$$cancel$$"))
            {
                sender.sendMessage(get("message.report.cancel"));
                return true;
            }
            else if (Bukkit.getPlayer(args[0]) == null)
            {
                sender.sendMessage(get("error.playerNotFound"));
                return true;
            }
            else if (sender instanceof ConsoleCommandSender)
            {
                sender.sendMessage(get("error.requirePlayer"));
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            ItemStack book = Books.getReportBook(target, CheatTypeUtils.getFullType());
            BookUtil.openBook(book, (Player) sender);
            return true;
        }

        ArrayList<String> reasonsV = new ArrayList<>(Arrays.asList(args));
        reasonsV.remove(0);

        ArrayList<String> reasons = new ArrayList<>();

        reasonsV.parallelStream().forEach(reason -> {
            if (reasons.contains(reason))
            {
                reasons.remove(reason);
                return;
            }
            reasons.add(reason);
        });

        ArrayList<EnumCheatType> types = CheatTypeUtils.getCheatTypeArrayFromString(reasons.toArray(new String[0]));

        if (Bukkit.getPlayer(args[0]) == null)
        {
            sender.sendMessage(get("error.playerNotFound"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (reasons.size() != 0 && reasons.get(reasons.size() - 1).equals("\\"))
        {
            ItemStack book = Books.getReportBook(target, types.toArray(new EnumCheatType[0]));
            BookUtil.openBook(book, (Player) sender);
            return true;
        }


        types.removeIf(type -> !type.isSelected());

        if (types.size() == 0)
        {
            if (!reasons.contains("$__BOOKS__;"))
                sender.sendMessage(get("error.report.invalidReason"));
            else if (args.length == 2 && reasons.contains("$__BOOKS__;"))
                sender.sendMessage(get("error.report.reasonNotSelected"));

            return true;
        }

        report(sender, types, target);
        return true;
    }

    /**
     * 思いっきり通報する。
     *
     * @param sender イベントsender。
     * @param types  判定タイプ。
     * @param target ターゲット。
     */
    private void report(CommandSender sender, ArrayList<EnumCheatType> types, Player target)
    {
        String senderUUID = sender instanceof ConsoleCommandSender ? "[CONSOLE]": ((Player) sender).getUniqueId().toString().replace("-", "");

        if (WatchEyeManagement.isExistsRecord(target.getUniqueId().toString().replace("-", ""), senderUUID))
        {
            sender.sendMessage(get("error.report.alreadyReported"));
            return;
        }

        String id = WatchEyeManagement.add(target, sender instanceof ConsoleCommandSender ? "[CONSOLE]": sender.getName(), senderUUID, SeverityLevels.getSeverity(types).getLevel());
        boolean successFlag = false;
        for (EnumCheatType type : types)
            successFlag = WatchEyeManagement.setReason(id, type, 0);


        if (successFlag)
        {
            sender.sendMessage(get("message.report.thanks"));

            if (!config.getBoolean("message.lynx"))
            {
                Utils.adminNotification(id);
                return;
            }

            Utils.adminNotification(target.getName(), id, types.parallelStream().map(EnumCheatType::getText).toArray(String[]::new));
        }
        else
            sender.sendMessage(get("error.unknownSQLError"));
    }
}
