package ml.peya.plugins.Gui;

import ml.peya.plugins.Enum.*;
import net.md_5.bungee.api.chat.*;
import org.bukkit.*;

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
        ComponentBuilder nextHover = new ComponentBuilder(ChatColor.AQUA + "次へ行く");

        nextBtn.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, nextHover.create()));
        return nextBtn;
    }

    public static TextComponent getPrevButton(int previous)
    {

        TextComponent prevBtn = new TextComponent(ChatColor.GREEN + "(" +
                ChatColor.AQUA + "<=" +
                ChatColor.GREEN + ")");
        prevBtn.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/psr view " + previous));
        ComponentBuilder prevHover = new ComponentBuilder(ChatColor.AQUA + "前へ戻る");

        prevBtn.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, prevHover.create()));

        return prevBtn;
    }

    public static ComponentBuilder getTable(String id, String uuid, String issueById, String issueByUuid, int dateInt, ArrayList<EnumCheatType> types)
    {
        Date date = new Date(dateInt);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");


        ComponentBuilder hover = new ComponentBuilder( "クリックして\nチャットに貼り付け");
        hover.color(net.md_5.bungee.api.ChatColor.AQUA);

        StringBuilder reasonText = new StringBuilder();

        for (EnumCheatType type: types)
            reasonText.append("        ").append(type.getText()).append("\n");

        ComponentBuilder b = new ComponentBuilder("");

        b.append("    ");
        b.append(getColor("報告者", issueById))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover.create()))
                .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, issueByUuid));
        b.append("\n    ");
        b.append(getColor("対象者", id))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover.create()))
                .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, uuid));
        b.append("\n    ");

        b.append(getColor("報告日時", formatter.format(date)));

        b.append(getColor("報告理由", reasonText.toString()));

        return b;
    }

    private static String getColor(String prefix, String value)
    {
        return ChatColor.AQUA +  prefix + ": " + ChatColor.BLUE + value;
    }

    public static ComponentBuilder getLine(String id, String issueById, ArrayList<EnumCheatType> types, String mngid)
    {
        ComponentBuilder hover = new ComponentBuilder( "クリックして\n");
        hover.color(net.md_5.bungee.api.ChatColor.GREEN);
        hover.append("詳細を表示")
                .color(net.md_5.bungee.api.ChatColor.GREEN);

        EnumSeverity severity = getSeverity(types);

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
        return b;
    }

    public static EnumSeverity getSeverity(ArrayList<EnumCheatType> types)
    {
        switch(types.size())
        {
            case 2:
                return EnumSeverity.NORMAL;
            case 3:
            case 4:
                return EnumSeverity.PRIORITY;
            case 5:
            case 6:
            case 7:
                return EnumSeverity.SEVERE;
            default:
                return EnumSeverity.LOW;
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
}
