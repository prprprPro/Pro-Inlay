package cn.szzxol.pro.inlay;

import cn.szzxol.pro.inlay.jewel.Utils.EffectType;
import static cn.szzxol.pro.inlay.jewel.Utils.getIS;
import cn.szzxol.pro.inlay.listener.Listeners;
import java.io.File;
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

    public static String version = "1.0.3";
    public static String sign_Full = "★";
    public static String sign_Null = "☆";
    public static int MaxLevel = 10;

    @Override
    public void onEnable() {
        getLogger().info("插件正在加载...");
        this.saveDefaultConfig();
        if (!this.getConfig().getString("Version").equals(version)) {
            File Config = new File(Inlay.instance.getDataFolder(), "config.yml");
            Config.delete();
            this.saveDefaultConfig();
        }
        getServer().addRecipe(new FurnaceRecipe(new ItemStack(Material.DIAMOND_SWORD), Material.DIAMOND_SWORD));
        getServer().getPluginManager().registerEvents(new Listeners(), this);
        sign_Full = this.getConfig().getString("Settings.DIAMOND.Sign.Full");
        sign_Null = this.getConfig().getString("Settings.DIAMOND.Sign.Null");
        MaxLevel = this.getConfig().getInt("Settings.DIAMOND.Level.MaxLevel");
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
                    player.sendMessage((new StringBuilder()).append(ChatColor.GOLD).append("无效指令...").toString());
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
                        if (Integer.valueOf(args[1]) < 1 || Integer.valueOf(args[1]) > MaxLevel) {
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
                    player.getWorld().dropItem(player.getLocation(), getIS(EffectType.Damage, level, amount));
                    player.sendMessage((new StringBuilder()).append(ChatColor.GOLD).append("物品已给予...").toString());
                }
                if (args[0].equalsIgnoreCase("punch")) {
                    ItemStack ItemStack = player.getItemInHand();
                    if (ItemStack.getType() == Material.DIAMOND_SWORD) {
                        ItemMeta ItemMeta = ItemStack.getItemMeta();
                        List<String> lores = ItemMeta.getLore() == null ? new ArrayList<>() : ItemMeta.getLore();
                        lores.add(ChatColor.translateAlternateColorCodes('&', "&f&l○ 空镶嵌孔"));
                        ItemMeta.setLore(lores);
                        ItemStack.setItemMeta(ItemMeta);
                        player.setItemInHand(ItemStack);
                        player.sendMessage((new StringBuilder()).append(ChatColor.GOLD).append("打孔成功...").toString());
                    } else {
                        player.sendMessage((new StringBuilder()).append(ChatColor.GOLD).append("当前物品无法打孔...").toString());
                    }
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    sign_Full = this.getConfig().getString("Settings.DIAMOND.Sign.Full");
                    sign_Null = this.getConfig().getString("Settings.DIAMOND.Sign.Null");
                    MaxLevel = this.getConfig().getInt("Settings.DIAMOND.Level.MaxLevel");
                    player.sendMessage((new StringBuilder()).append(ChatColor.GOLD).append("插件重载完成...").toString());
                    player.sendMessage((new StringBuilder()).append(ChatColor.GOLD).append("当前宝石掉落率： ").append(this.getConfig().getInt("Settings.Drop.Chance")).append("%").toString());
                    player.sendMessage((new StringBuilder()).append(ChatColor.GOLD).append("当前打孔成功率： ").append(this.getConfig().getInt("Settings.Punch.Chance")).append("%").toString());
                }
            }
        }
        return true;
    }

    public static Inlay instance;

    public Inlay() {
        instance = this;
    }

}
