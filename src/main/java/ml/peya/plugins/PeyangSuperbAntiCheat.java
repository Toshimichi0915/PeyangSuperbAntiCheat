package ml.peya.plugins;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import com.zaxxer.hikari.*;
import ml.peya.plugins.Commands.CmdTst.AuraBot;
import ml.peya.plugins.Commands.CmdTst.AuraPanic;
import ml.peya.plugins.Commands.CmdTst.*;
import ml.peya.plugins.Commands.*;
import ml.peya.plugins.DetectClasses.*;
import ml.peya.plugins.Gui.Events.*;
import ml.peya.plugins.Gui.*;
import ml.peya.plugins.Gui.Items.Main.*;
import ml.peya.plugins.Gui.Items.Target.*;
import ml.peya.plugins.Gui.Items.Target.Page2.*;
import ml.peya.plugins.Learn.*;
import ml.peya.plugins.Moderate.*;
import ml.peya.plugins.Task.*;
import ml.peya.plugins.Utils.*;
import org.bukkit.*;
import org.bukkit.configuration.file.*;
import org.bukkit.plugin.java.*;
import org.bukkit.scheduler.*;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

public class PeyangSuperbAntiCheat extends JavaPlugin
{
    private static final int __BSTATS_PLUGIN_ID = 8084;
    public static Logger logger = Logger.getLogger("PeyangSuperbAntiCheat");
    public static FileConfiguration config;
    public static String databasePath;
    public static String banKickPath;
    public static String learnPath;
    public static DetectingList cheatMeta;
    public static KillCounting counting;
    public static ProtocolManager protocolManager;
    public static Item item;
    public static Tracker tracker;
    public static HashMap<UUID, HashMap<String, String>> mods;
    public static long time = 0L;
    public static int banLeft;
    public static NeuralNetwork network;
    public static HikariDataSource eye;
    public static HikariDataSource banKick;
    public static HikariDataSource learn;
    public static boolean isAutoMessageEnabled;
    public static boolean isTrackEnabled;
    public static BukkitRunnable autoMessage;
    public static BukkitRunnable trackerTask;
    private static PeyangSuperbAntiCheat plugin;

    public static PeyangSuperbAntiCheat getPlugin()
    {
        return plugin;
    }

    @Override
    public void onEnable()
    {
        new Metrics(this, __BSTATS_PLUGIN_ID);

        if (getServer().getPluginManager().getPlugin("ProtocolLib") == null || !getServer().getPluginManager().getPlugin("ProtocolLib").isEnabled())
        {
            logger.log(Level.SEVERE, "This plugin requires ProtocolLib!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        saveDefaultConfig();

        plugin = this;
        config = getConfig();
        databasePath = config.getString("database.path");
        banKickPath = config.getString("database.logPath");
        learnPath = config.getString("database.learnPath");

        network = new NeuralNetwork();

        eye = new HikariDataSource(Init.initMngDatabase(getDataFolder().getAbsolutePath() + "/" + databasePath));
        banKick = new HikariDataSource(Init.initMngDatabase(getDataFolder().getAbsolutePath() + "/" + banKickPath));
        learn = new HikariDataSource(Init.initMngDatabase(getDataFolder().getAbsolutePath() + "/" + learnPath));

        cheatMeta = new DetectingList();
        counting = new KillCounting();
        tracker = new Tracker();

        protocolManager = ProtocolLibrary.getProtocolManager();

        item = new Item();

        item.register(new ml.peya.plugins.Gui.Items.Target.AuraBot());  //====Page1
        item.register(new ml.peya.plugins.Gui.Items.Target.AuraPanic());
        item.register(new TestKnockBack());
        item.register(new CompassTracker3000_tm());
        item.register(new BanBook());
        item.register(new ToPage2());                                   //
        item.register(new BackButton());

        item.register(new BackToPage1());                              //====Page2
        item.register(new Lead());
        item.register(new ModList());

        item.register(new TargetStick());                              //====Main

        protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY)
        {
            @Override
            public void onPacketReceiving(PacketEvent event)
            {
                new Packets().useEntity(event);
            }
        });

        protocolManager.addPacketListener(new PacketAdapter(this, PacketType.Play.Server.PLAYER_INFO)
        {
            @Override
            public void onPacketSending(PacketEvent event)
            {
                new Packets().playerInfo(event);
            }
        });


        if (!(Init.createDefaultTables() && Init.initBypass()))
            Bukkit.getPluginManager().disablePlugin(this);

        try (Connection connection = learn.getConnection();
             Statement statement = connection.createStatement())
        {
            ResultSet rs = statement.executeQuery("SeLeCt standard FrOm WdLeArN;");

            while (rs.next())
            {
                banLeft = rs.getInt("standard");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            ReportUtils.errorNotification(ReportUtils.getStackTrace(e));
        }

        getCommand("report").setExecutor(new CommandReport());
        getCommand("peyangsuperbanticheat").setExecutor(new CommandPeyangSuperbAntiCheat());
        getCommand("aurabot").setExecutor(new AuraBot());
        getCommand("acpanic").setExecutor(new AuraPanic());
        getCommand("testknockback").setExecutor(new TestKnockback());
        getCommand("bans").setExecutor(new CommandBans());
        getCommand("pull").setExecutor(new CommandPull());
        getCommand("target").setExecutor(new CommandTarget());
        getCommand("mods").setExecutor(new CommandMods());
        getCommand("tracking").setExecutor(new CommandTracking());
        getCommand("silentteleport").setExecutor(new CommandSilentTeleport());

        getServer().getPluginManager().registerEvents(new Events(), this);
        getServer().getPluginManager().registerEvents(new Run(), this);
        getServer().getPluginManager().registerEvents(new Drop(), this);

        isAutoMessageEnabled = config.getBoolean("autoMessage.enabled");
        time = config.getLong("autoMessage.time");

        if (time == 0L)
            time = 1L;

        autoMessage = new AutoMessageTask();
        trackerTask = new TrackerTask();

        isTrackEnabled = config.getBoolean("mod.tracking.enabled");

        if (isTrackEnabled)
        {
            logger.info("Starting Tracker Task...");
            trackerTask.runTaskTimer(this, 0, config.getInt("mod.tracking.trackTicks"));
        }

        if (isAutoMessageEnabled)
        {
            logger.info("Starting Auto-Message Task...");
            autoMessage.runTaskTimer(this, 0, 20 * (time * 60));
        }

        mods = new HashMap<>();

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "FML|HS");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "FML|HS", new PluginMessageListener());


        logger.info("PeyangSuperbAntiCheat has been activated!");
    }

    @Override
    public void onDisable()
    {
        if (eye != null)
            eye.close();
        if (banKick != null)
            banKick.close();
        eye = null;
        banKick = null;
        if (autoMessage != null && RunnableUtil.isStarted(autoMessage))
        {
            logger.info("Stopping Auto-Message Task...");
            autoMessage.cancel();
        }

        if (trackerTask != null && RunnableUtil.isStarted(trackerTask))
        {
            logger.info("Stopping Tracker Task...");
            trackerTask.cancel();
        }

        logger.info("PeyangSuperbAntiCheat has disabled!");
    }
}
