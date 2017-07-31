package cn.szzxol.pro.inlay;

import static cn.szzxol.pro.inlay.jewel.Jewel.getLevelSign;
import cn.szzxol.pro.inlay.listener.Listeners;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author I_promise
 */
public class Inlay extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("插件正在加载...");
        this.saveDefaultConfig();
        FurnaceRecipe recipe = new FurnaceRecipe(new ItemStack(Material.DIAMOND_SWORD), Material.DIAMOND_SWORD);
        getServer().addRecipe(recipe);
        getServer().getPluginManager().registerEvents(new Listeners(), this);
        getLogger().info("插件加载完成...");
    }

    @Override
    public void onDisable() {
        getLogger().info("插件卸载完成...");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("inlay")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!player.isOp()) {
                    return true;
                }
                if (args.length == 0) {
                    return true;
                }
                if (args[0].equalsIgnoreCase("get")) {
                    int level = 1, amount = 1;
                    if (args.length == 3) {
                        try {
                            float number = Integer.valueOf(args[1]);
                        } catch (Exception e) {
                            level = 1;
                        }
                        if (Integer.valueOf(args[1]) < 1) {
                            level = 1;
                        }
                        level = Integer.valueOf(args[1]);
                        try {
                            float number = Integer.valueOf(args[2]);
                        } catch (Exception e) {
                            amount = 1;
                        }
                        if (Integer.valueOf(args[2]) < 1) {
                            amount = 1;
                        }
                        amount = Integer.valueOf(args[2]);
                    }
                    player.getWorld().dropItem(player.getLocation(), getIS(level, amount));
                    ItemStack ItemStack2 = new ItemStack(Material.DIAMOND_SWORD, 1);
                    ItemMeta ItemMeta2 = ItemStack2.getItemMeta();
                    List<String> lores2 = new ArrayList<>();
                    lores2.add(ChatColor.translateAlternateColorCodes('&', "&f&l○ 空镶嵌孔"));
                    ItemMeta2.setLore(lores2);
                    ItemStack2.setItemMeta(ItemMeta2);
                    ItemStack2.setDurability((short) 1);
                    player.getWorld().dropItem(player.getLocation(), ItemStack2);
                }
                if (args[0].equalsIgnoreCase("punch")) {
                    ItemStack ItemStack = player.getItemInHand();
                    ItemMeta ItemMeta = ItemStack.getItemMeta();
                    List<String> lores = ItemMeta.getLore() == null ? new ArrayList<>() : ItemMeta.getLore();
                    lores.add(ChatColor.translateAlternateColorCodes('&', "&f&l○ 空镶嵌孔"));
                    ItemMeta.setLore(lores);
                    ItemStack.setItemMeta(ItemMeta);
                    player.setItemInHand(ItemStack);
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    player.sendMessage((new StringBuilder()).append(ChatColor.GOLD).append("插件重载完成...").toString());
                    player.sendMessage((new StringBuilder()).append(ChatColor.GOLD).append("当前宝石掉落率： ").append(this.getConfig().getInt("Settings.Drop.Chance")).append("%").toString());
                    player.sendMessage((new StringBuilder()).append(ChatColor.GOLD).append("当前打孔成功率： ").append(this.getConfig().getInt("Settings.Punch.Chance")).append("%").toString());
                }
            }
        }
        return true;
    }

    public static ItemStack getIS(int Level, int amount) {
        ItemStack ItemStack = new ItemStack(Material.DIAMOND, amount);
        ItemMeta ItemMeta = ItemStack.getItemMeta();
        ItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&f&l钻石&d&l【可镶嵌】"));
        List<String> lores = new ArrayList<>();
        lores.add(ChatColor.translateAlternateColorCodes('&', "&f&l【镶嵌宝石】"));
        lores.add(ChatColor.translateAlternateColorCodes('&', "&f&l" + getLevelSign(Level)));
        lores.add(ChatColor.translateAlternateColorCodes('&', "&f&l伤害+" + Inlay.instance.getConfig().getDouble("Settings.DIAMOND.Level." + Level)));
        ItemMeta.setLore(lores);
        ItemStack.setItemMeta(ItemMeta);
        return ItemStack;
    }

    public static Inlay instance;

    public Inlay() {
        instance = this;
    }

}
