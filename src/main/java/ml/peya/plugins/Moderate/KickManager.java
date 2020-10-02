package ml.peya.plugins.Moderate;

import ml.peya.plugins.Objects.Decorations;
import ml.peya.plugins.PeyangSuperbAntiCheat;
import ml.peya.plugins.Utils.SQL;
import ml.peya.plugins.Utils.Utils;
import ml.peya.plugins.Variables;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;

import static ml.peya.plugins.Utils.MessageEngine.get;

/**
 * プレイヤーのキックと共にいろいろやってくれるやつ。
 */
public class KickManager
{
    /**
     * Bukkit的キックをかます。
     *
     * @param player 対象プレイヤー。
     * @param reason 判定タイプ。
     * @param wdFlag 報告してるか...どうか？
     * @param isTest テストで捕まったか...どうか？
     */
    public static void kickPlayer(Player player, String reason, boolean wdFlag, boolean isTest) throws ArithmeticException
    {
        player.setMetadata("psac-kick", new FixedMetadataValue(PeyangSuperbAntiCheat.getPlugin(), true));

        BroadcastMessenger.broadCast(wdFlag, player);
        Decorations.decoration(player);
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                kick(player, reason, isTest, !wdFlag);
                this.cancel();
            }
        }.runTaskLater(PeyangSuperbAntiCheat.getPlugin(), Math.multiplyExact(Variables.config.getInt("kick.delay"), 20));
    }

    /**
     * 色々やってから結局蹴るやつ。
     *
     * @param player 対象プレイヤー。
     * @param reason 判定タイプ。
     * @param isTest テストで捕まったか...どうか？
     * @param opFlag OPが入ってたか......どうか？
     */
    private static void kick(Player player, String reason, boolean isTest, boolean opFlag)
    {
        if (Variables.config.getBoolean("decoration.lightning"))
            Decorations.lighting(player);

        HashMap<String, Object> map = new HashMap<>();

        String id = Abuse.genRandomId(8);

        map.put("reason", reason);
        map.put("ggid", Abuse.genRandomId(7));
        map.put("id", id);

        String message = get("kick.reason", map);

        if (isTest)
        {
            player.kickPlayer(message);
            return;
        }
        try (Connection kickC = Variables.banKick.getConnection();
             Connection eyeC = Variables.eye.getConnection();
             PreparedStatement statement = eyeC.prepareStatement("SELECT MNGID FROM watcheye WHERE UUID=?"))
        {

            SQL.insert(kickC, "kick",
                    player.getName(),
                    player.getUniqueId().toString().replace("-", ""),
                    id,
                    new Date().getTime(),
                    reason,
                    opFlag ? 1: 0
            );

            statement.setString(1, player.getUniqueId().toString().replace("-", ""));
            ResultSet eyeList = statement.executeQuery();

            while (eyeList.next())
            {
                final String MNGID = eyeList.getString("MNGID");
                SQL.delete(eyeC, "watchreason", new HashMap<String, String>()
                {{
                    put("MNGID", MNGID);
                }});
                SQL.delete(eyeC, "watcheye", new HashMap<String, String>()
                {{
                    put("MNGID", MNGID);
                }});
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Utils.errorNotification(Utils.getStackTrace(e));
        }
        player.kickPlayer(message);
    }
}
