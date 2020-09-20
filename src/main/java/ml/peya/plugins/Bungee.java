package ml.peya.plugins;

import com.google.common.io.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.messaging.*;

import java.io.*;

import static ml.peya.plugins.Variables.bungeeChannel;
import static ml.peya.plugins.Variables.bungeeCommand;

/**
 * バンジーフック系
 */
public class Bungee implements PluginMessageListener
{
    /**
     * バンジーに送信
     *
     * @param message メッセージ
     */
    public static void sendMessage(String message)
    {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF(message);
        Bukkit.getServer().sendPluginMessage(PeyangSuperbAntiCheat.getPlugin(), "PSACProxy", output.toByteArray());
    }

    /**
     * コマンド(メッセージ)受信
     *
     * @param channel チャンネル
     * @param player  プレイヤー
     * @param data    データ
     */
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] data)
    {
        if (!channel.equals(bungeeChannel))
            return;
        DataInputStream input = new DataInputStream(new ByteArrayInputStream(data));
        String message;
        try
        {
            if (!input.readUTF().equals(bungeeChannel))
                return;
            message = input.readUTF();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }

        bungeeCommand.runCommand(message);
    }
}