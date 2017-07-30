package cn.szzxol.pro.inlay.listener;

import cn.szzxol.pro.inlay.Inlay;
import static cn.szzxol.pro.inlay.Inlay.getIS;
import cn.szzxol.pro.inlay.jewel.Jewel;
import static cn.szzxol.pro.inlay.jewel.Utils.canInlay;
import static cn.szzxol.pro.inlay.jewel.Utils.getJewelLevel;
import static cn.szzxol.pro.inlay.jewel.Utils.getJewelType;
import static cn.szzxol.pro.inlay.jewel.Utils.isActive;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import static org.bukkit.Bukkit.getLogger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
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
import org.json.simple.JSONObject;

/**
 *
 * @author I_promise
 */
public class Listeners implements Listener {

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        AnvilInventory inv = event.getInventory();
        ItemStack is = event.getResult();
        ItemStack jw = inv.getItem(1);
        if (jw != null && isActive(jw)) {
            if (canInlay(is)) {
                Jewel j = new Jewel(getJewelType(jw), getJewelLevel(jw));
                ItemMeta ItemMeta = is.getItemMeta();
                List<String> lores = ItemMeta.getLore();
                ItemMeta.setLore(insertLore(lores, j));
                is.setItemMeta(ItemMeta);
                is.setDurability((short) 1);
                event.setResult(is);
            }
        }
    }

    @EventHandler
    public void onFurnaceSmelt(FurnaceSmeltEvent event) {
        ItemStack is = event.getSource();
        if (is.getType() == Material.DIAMOND_SWORD) {
            Random r = new java.util.Random();
            int index = r.nextInt(100);
            int chance = Inlay.instance.getConfig().getInt("Settings.Punch.Chance");
            if (index < chance) {
                ItemMeta ItemMeta = is.getItemMeta();
                List<String> lores = ItemMeta.getLore() == null ? new LinkedList<>() : ItemMeta.getLore();
                lores.add(ChatColor.translateAlternateColorCodes('&', "&f&l○ 空镶嵌孔"));
                ItemMeta.setLore(lores);
                is.setItemMeta(ItemMeta);
                is.setDurability((short) 1);
                event.setResult(is);
            } else {
                event.setResult(null);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (event.getBlock().getType() == Material.DIAMOND_ORE && !player.getItemInHand.containsEnchantment(Enchantment.SILK_TOUCH)) {
            Random r = new java.util.Random();
            int index = r.nextInt(100);
            int chance = Inlay.instance.getConfig().getInt("Settings.Drop.Chance");
            if (index < chance) {
                int level = (int) Math.floor(11 - (Math.log(r.nextInt(1023) + 2) / Math.log(2)));
                Location l = event.getBlock().getLocation();
                ItemStack is = getIS(level, 1);
                l.getWorld().dropItem(l, is);

               
sendmessage1(player, is);
            }
        }
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
        l.add(ChatColor.translateAlternateColorCodes('&', "&b&l● " + j.type.getName()));
        l.add(ChatColor.translateAlternateColorCodes('&', "&f&l     " + j.getLevelSign()));
        l.add(ChatColor.translateAlternateColorCodes('&', "&f&l     伤害+" + Inlay.instance.getConfig().getDouble("Settings.DIAMOND.Level." + j.Level)));
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
