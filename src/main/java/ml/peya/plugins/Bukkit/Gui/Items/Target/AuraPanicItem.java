package ml.peya.plugins.Bukkit.Gui.Items.Target;

import ml.peya.plugins.Bukkit.Gui.Item;
import ml.peya.plugins.Bukkit.Gui.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

import static ml.peya.plugins.Bukkit.Utils.MessageEngine.get;
import static ml.peya.plugins.Bukkit.Utils.MessageEngine.pair;

/**
 * 背後に張り付くBOT
 */
public class AuraPanicItem implements IItems
{
    @Override
    public void run(Player player, String target)
    {
        player.performCommand("acpanic " + target);
    }

    @Override
    public ItemStack getItem(String target)
    {
        AuraBotItem auraBot = new AuraBotItem();
        ItemStack stack = auraBot.getItem(target);

        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(get("item.execute", pair("command", "AuraPanic")));
        meta.setLore(Item.getLore(this, target));
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public boolean canSpace()
    {
        return true;
    }

    @Override
    public String getExecName()
    {
        return "AURA_PANIC";
    }

    @Override
    public Type getType()
    {
        return Type.TARGET;
    }
}
