package cn.szzxol.pro.inlay.listener;

import cn.szzxol.pro.inlay.Inlay;
import cn.szzxol.pro.inlay.jewel.Jewel;
import static cn.szzxol.pro.inlay.jewel.Jewel.MaxLevel;
import static cn.szzxol.pro.inlay.jewel.Utils.canInlay;
import static cn.szzxol.pro.inlay.jewel.Utils.getIS;
import static cn.szzxol.pro.inlay.jewel.Utils.getInlayAmount;
import static cn.szzxol.pro.inlay.jewel.Utils.getJewelLevel;
import static cn.szzxol.pro.inlay.jewel.Utils.getRandomEffectType;
import static cn.szzxol.pro.inlay.jewel.Utils.isActive;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import static org.bukkit.Bukkit.getLogger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author I_promise
 */
public class Listeners implements Listener {

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        AnvilInventory inv = event.getInventory();
        ItemStack is = event.getResult();
        ItemStack isr = inv.getItem(0);
        if (!(isr == null) && isr.getDurability() == 0) {
            isr.setDurability((short) 1);
            inv.setItem(0, isr);
        }
        ItemStack jw = inv.getItem(1);
        if (jw != null && isActive(jw)) {
            if (canInlay(is)) {
                Jewel j = new Jewel(jw);
                ItemMeta ItemMeta = is.getItemMeta();
                List<String> lores = ItemMeta.getLore();
                ItemMeta.setLore(insertLore(lores, j));
                is.setItemMeta(ItemMeta);
                event.setResult(is);
            }
        }
    }

    @EventHandler
    public void onFurnaceSmelt(FurnaceSmeltEvent event) {
        ItemStack is = event.getSource();
        if (is.getType() == Material.DIAMOND_SWORD) {
            if (getInlayAmount(is) < Inlay.instance.getConfig().getInt("Settings.Punch.MaxAmount")) {
                Random r = new java.util.Random();
                int index = r.nextInt(100);
                int chance = Inlay.instance.getConfig().getInt("Settings.Punch.Chance");
                if (index < chance) {
                    ItemMeta ItemMeta = is.getItemMeta();
                    List<String> lores = ItemMeta.getLore() == null ? new LinkedList<>() : ItemMeta.getLore();
                    lores.add(ChatColor.translateAlternateColorCodes('&', "&f&l○ 空镶嵌孔"));
                    ItemMeta.setLore(lores);
                    is.setItemMeta(ItemMeta);
                    event.setResult(is);
                } else if (Inlay.instance.getConfig().getBoolean("Settings.Punch.DisapperOnFail")) {
                    event.setResult(null);
                }
            } else {
                event.setResult(is);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (event.getBlock().getType() == Material.DIAMOND_ORE && !player.getItemInHand().containsEnchantment(Enchantment.SILK_TOUCH)) {
            Random r = new java.util.Random();
            int index = r.nextInt(100);
            int chance = Inlay.instance.getConfig().getInt("Settings.Drop.Chance");
            if (index < chance) {
                int level = getRandom(r, Inlay.instance.getConfig().getInt("Settings.DIAMOND.Multiple"), 0);
                Location l = event.getBlock().getLocation();
                ItemStack is = getIS(getRandomEffectType(), level, 1);
                l.getWorld().dropItem(l, is);
                sendmessage1(player, is);
            }
        }
    }

    public static int getRandom(Random r, int Multiple, int adjust) {
        double d = r.nextDouble();
        long l = (long) (d * (Math.pow(Multiple, MaxLevel + adjust) - Multiple)) + Multiple;
        int n = (int) Math.floor(Math.log(l) / Math.log(Multiple));
        return MaxLevel + adjust - n;
    }

    public static void sendmessage1(Player player, ItemStack is) {
        getLogger().info(player.getName() + " 挖到了 " + getJewelLevel(is) + "星钻石");
        String str = "{\"text\":\"" + is.getItemMeta().getDisplayName() + "\\r\\n";
        List<String> lores = is.getItemMeta().getLore();
        for (int i = 0; i < lores.size(); i++) {
            str += lores.get(i);
            if (i != lores.size() - 1) {
                str += "\\r\\n";
            }
        }
        str += "\"}";
        List<Player> AllPlayers = new ArrayList<>();
        AllPlayers.addAll(Bukkit.getServer().getOnlinePlayers());
        for (Player target : AllPlayers) {
            Inlay.instance.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + target.getName() + " [\"【系统提示】" + player.getName() + "挖到了 " + "\",{\"text\":\"" + is.getItemMeta().getDisplayName().toString() + "\",\"bold\":true,\"underlined\":true,\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[" + str + "]}}}]");
        }
    }

    public static void sendmessage2(Player player, ItemStack is) {
        String str = "{\"text\":\"" + is.getItemMeta().getDisplayName() + "\\r\\n";
        List<String> lores = is.getItemMeta().getLore();
        for (int i = 0; i < lores.size(); i++) {
            str += lores.get(i);
            if (i != lores.size() - 1) {
                str += "\\r\\n";
            }
        }
        str += "\"}";
        List<Player> AllPlayers = new ArrayList<>();
        AllPlayers.addAll(Bukkit.getServer().getOnlinePlayers());
        for (Player target : AllPlayers) {
            Inlay.instance.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + target.getName() + " [\"【系统提示】" + player.getName() + "成功强化了 " + "\",{\"text\":\"" + is.getItemMeta().getDisplayName() + "\",\"bold\":true,\"underlined\":true,\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[" + str + "]}}}]");
        }
    }

    @EventHandler
    public void damagedByEntity(EntityDamageByEntityEvent evt) {
        if (evt.getDamager() instanceof Player) {
            Player player = (Player) evt.getDamager();
            ItemStack is = player.getItemInHand();
            if (is.getType() == Material.DIAMOND_SWORD) {
                if (is.getItemMeta().getLore() != null) {
                    if (!is.getItemMeta().getLore().isEmpty()) {
                        Double damage = evt.getDamage();
                        for (int i = 0; i < is.getItemMeta().getLore().size(); i++) {
                            if (is.getItemMeta().getLore().get(i).contains("伤害+")) {
                                String x = is.getItemMeta().getLore().get(i);
                                x = x.substring(x.indexOf("伤害+") + 3);
                                try {
                                    double number = Double.valueOf(x);
                                } catch (Exception e) {
                                    return;
                                }
                                damage += Double.valueOf(x);
                            }
                        }
                        evt.setDamage(damage);
                    }
                }
            }
        }
    }

    public static List<String> insertLore(List<String> lores, Jewel j) {
        int index = -1;
        for (int i = 0; i < lores.size(); i++) {
            if (lores.get(i).contains("○ 空镶嵌孔") && index == -1) {
                index = i;
            }
        }
        List<String> l = new LinkedList<>();
        List<String> l1 = index - 1 > 0 ? lores.subList(0, index) : new LinkedList<>();
        l.addAll(l1);
        List<String> l2 = lores.size() > index + 1 ? lores.subList(index + 1, lores.size()) : new LinkedList<>();
        l.addAll(j.lores);
        l.addAll(l2);
        return l;
    }

    public static void takeItem(Player player, ItemStack is) {
        int itemsToTake = is.getAmount();
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack current = contents[i];
            if (current != null && current.isSimilar(is)) {
                if (current.getAmount() >= itemsToTake) {
                    current.setAmount(current.getAmount() - itemsToTake);
                    return;
                } else {
                    itemsToTake -= current.getAmount();
                    player.getInventory().setItem(i, null);
                }
            }
        }
    }
}
