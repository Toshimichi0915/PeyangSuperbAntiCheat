package ml.peya.plugins.Utils;

import ml.peya.plugins.*;
import ml.peya.plugins.Enum.*;
import net.md_5.bungee.api.chat.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.math.*;
import java.text.*;
import java.util.*;

public class TextBuilder
{
    public static TextComponent getNextButton(int next)
    {
        TextComponent nextBtn = new TextComponent(ChatColor.GREEN + "(" +
                ChatColor.AQUA + "=>" +
                ChatColor.GREEN + ")");
        nextBtn.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/psr view " + next));
        ComponentBuilder nextHover = new ComponentBuilder(MessageEngihe.get("book.words.next"));

        nextBtn.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, nextHover.create()));
        return nextBtn;
    }

    public static TextComponent getPrevButton(int previous)
    {

        TextComponent prevBtn = new TextComponent(ChatColor.GREEN + "(" +
                ChatColor.AQUA + "<=" +
                ChatColor.GREEN + ")");
        prevBtn.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/psr view " + previous));
        ComponentBuilder prevHover = new ComponentBuilder(MessageEngihe.get("book.words.back"));

        prevBtn.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, prevHover.create()));

        return prevBtn;
    }

    public static void showText(String id, String uuid, String issueById, String issueByUuid, BigDecimal dateInt, ArrayList<EnumCheatType> types, CommandSender sender)
    {
        Date date = new Date(dateInt.longValue());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");


        ComponentBuilder hover = new ComponentBuilder(MessageEngihe.get("book.clickable"));

        StringBuilder reasonText = new StringBuilder();

        for (EnumCheatType type: types)
            reasonText.append("        ").append(type.getText()).append("\n");

        ComponentBuilder b1 = new ComponentBuilder("    " + MessageEngihe.get("book.text.issueBy", MessageEngihe.hsh("id", issueById)));
        b1.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover.create()));
        b1.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, issueByUuid));
        sender.spigot().sendMessage(b1.create());

        ComponentBuilder b2 = new ComponentBuilder("    " + MessageEngihe.get("book.text.issueTo", MessageEngihe.hsh("id", id)));
        b2.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover.create()));
        b2.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, uuid));
        sender.spigot().sendMessage(b2.create());

        sender.sendMessage("    " + MessageEngihe.get("book.text.dateTime", MessageEngihe.hsh("time", formatter.format(date))));

        sender.sendMessage("    " + MessageEngihe.get("book.text.reason", MessageEngihe.hsh("reason", reasonText.toString())));

        HashMap<String, Object> serv = new HashMap<>();
        serv.put("color", SeverityLevelUtils.getSeverity(types).getColor());
        serv.put("level", SeverityLevelUtils.getSeverity(types).getText());
        sender.sendMessage(MessageEngihe.get("book.text.severity", serv));
    }


    public static ComponentBuilder getLine(String id, String issueById, ArrayList<EnumCheatType> types, String mngid, CommandSender sender)
    {
        ComponentBuilder hover = new ComponentBuilder(MessageEngihe.get("book.click.openAbout"));

        ComponentBuilder dropHover = new ComponentBuilder( MessageEngihe.get("book.click.deleteReport"));

        EnumSeverity severity = SeverityLevelUtils.getSeverity(types);

        ComponentBuilder b = new ComponentBuilder("");

        b.append(id)
                .color(net.md_5.bungee.api.ChatColor.AQUA)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/psr show " + mngid))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover.create()));
        b.append("   ");

        b.append(issueById)
                .color(net.md_5.bungee.api.ChatColor.GREEN)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/psr show " + mngid))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover.create()));
        b.append("   ");

        b.append(severity.getText())
                .color(severity.getColor())
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/psr show " + mngid))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover.create()));
        b.append("   ");
        if (sender instanceof Player)
        {
            b.append(MessageEngihe.get("book.click.delete"))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/psr drop " + mngid))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, dropHover.create()));

        }
        else
            b.append(ChatColor.YELLOW + mngid);

        return b;
    }

    public static String getSeverityLevel(EnumSeverity severity)
    {
        String prefix = ChatColor.YELLOW + "Level " + severity.getColor();
        switch (severity)
        {
            case FINE:
                return prefix + "1";
            case FINER:
                return prefix + "2";
            case FINEST:
                return prefix + "3";
            case NORMAL:
                return prefix + "4";
            case PRIORITY:
                return prefix + ChatColor.BOLD + "5";
            case REQUIRE_FAST:
                return prefix + ChatColor.BOLD + "6";
            case SEVERE:
                return prefix + ChatColor.BOLD + "7";
            default:
                return prefix + ChatColor.GRAY + "Unknown";
        }
    }

    public static ComponentBuilder getNextPrevButtonText(TextComponent prev, TextComponent next, boolean prevFlag, boolean nextFlag)
    {
        TextComponent uBar = new TextComponent("----");
        uBar.setColor(net.md_5.bungee.api.ChatColor.AQUA);
        ComponentBuilder builder = new ComponentBuilder(prevFlag ? prev: uBar);
        builder.append("------------------------")
                .color(net.md_5.bungee.api.ChatColor.AQUA);
        builder.append(nextFlag ? next :  uBar);
        return builder;
    }

    public static ComponentBuilder getBroadCastWdDetectionText()
    {
        return new ComponentBuilder(MessageEngihe.get("kick.broadcastWd"));
    }

    public static ComponentBuilder getBroadCastWdDetectionText(Player player)
    {
        ComponentBuilder component = getBroadCastWdDetectionText();

        HashMap<String, Object> map = new HashMap<>();
        map.put("name", player.getName());
        map.put("uuid", player.getUniqueId().toString());

        ComponentBuilder hover = new ComponentBuilder(MessageEngihe.get("kick.broadcastAdmin", map));
        HoverEvent event = new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover.create());
        component.event(event);
        return component;
    }

    public static String getLine(String prefix, String value)
    {
        return ChatColor.AQUA + prefix + ChatColor.WHITE + "：" + ChatColor.GREEN + value;
    }


    public static ComponentBuilder textTestRep(String name, int VL, int kickVL)
    {
        ComponentBuilder builder = new ComponentBuilder(MessageEngihe.get("base.prefix") + "\n");

        builder.append(MessageEngihe.get("message.auraCheck.result.prefix", MessageEngihe.hsh("name", name)));
        builder.append("\n");
        builder.append(MessageEngihe.get("message.auraCheck.result.vl", MessageEngihe.hsh("vl", String.valueOf(VL))));
        builder.append("\n");
        builder.append(MessageEngihe.get("message.auraCheck.result.vlGraph"));
        builder.append("\n");
        builder.append(OptGraphGenerator.genGraph(VL, kickVL));
        builder.append("\n");

        String result = VL >= kickVL ? MessageEngihe.get("message.auraCheck.result.words.kick"): MessageEngihe.get("message.auraCheck.result.words.ok");

        builder.append(MessageEngihe.get("message.auraCheck.result.result", MessageEngihe.hsh("result", result)));

        return builder;
    }

    public static ComponentBuilder textPanicRep(String name, int vl)
    {
        ComponentBuilder builder = new ComponentBuilder(MessageEngihe.get("base.prefix"));

        builder.append(MessageEngihe.get("message.auraCheck.result.prefix", MessageEngihe.hsh("name", name)));
        builder.append("\n");
        builder.append(MessageEngihe.get("message.auraCheck.result.vl", MessageEngihe.hsh("vl", String.valueOf(vl))));
        return builder;
    }


    public static ComponentBuilder getTextBan(BanAnalyzer.Bans ban, BanAnalyzer.Type type)
    {
        Date date = new Date(ban.getDate());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        ComponentBuilder builder = new ComponentBuilder(ChatColor.YELLOW + (type == BanAnalyzer.Type.KICK ? "Kick": "Ban"));
        builder.append(" - " + formatter.format(date));
        StringBuilder reasonSet = new StringBuilder();
        for (String reason: ban.getReason().split(", "))
        {
            EnumCheatType tp = CheatTypeUtils.getCheatTypeFromString(reason);
            if (tp == null)
                reasonSet.append(reason).append(", ");
            else
                reasonSet.append(tp.getText());
        }

        if (reasonSet.toString().endsWith(", "))
            reasonSet.setLength(reasonSet.length() - 2);

        builder.append(ChatColor.WHITE + " " + ChatColor.ITALIC + reasonSet.toString());

        return builder;
    }

}
