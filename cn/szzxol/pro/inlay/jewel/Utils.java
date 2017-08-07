package cn.szzxol.pro.inlay.jewel;

import cn.szzxol.pro.inlay.Inlay;
import static cn.szzxol.pro.inlay.jewel.Jewel.getLevelSign;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author I_promise
 */
public class Utils {

    public enum EffectType {
        Damage("伤害+");

        private final String Name;

        private EffectType(String Name) {
            this.Name = Name;
        }

        public String getName() {
            return this.Name;
        }
    }

    public static EffectType getRandomEffectType() {
        EffectType[] values = EffectType.values();
        int index = new Random().nextInt(values.length);
        return values[index];
    }

    public static ItemStack getIS(EffectType type, int Level, int amount) {
        ItemStack ItemStack = new ItemStack(Material.DIAMOND, amount);
        ItemMeta ItemMeta = ItemStack.getItemMeta();
        ItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&f&l钻石&d&l【可镶嵌】"));
        List<String> lores = new ArrayList<>();
        lores.add(ChatColor.translateAlternateColorCodes('&', "&f&l【镶嵌宝石】"));
        lores.add(ChatColor.translateAlternateColorCodes('&', "&b&l" + getLevelSign(Level)));
        lores.add(ChatColor.translateAlternateColorCodes('&', "&f&l" + type.getName() + Inlay.instance.getConfig().getDouble("Settings.DIAMOND.Level." + type.toString() + "." + Level)));
        ItemMeta.setLore(lores);
        ItemStack.setItemMeta(ItemMeta);
        return ItemStack;
    }

    public static boolean isActive(ItemStack is) {
        for (Jewel.JewelType t : Jewel.JewelType.values()) {
            if (is.getType() == t.getMaterial()) {
                if (is.getItemMeta().getLore() != null) {
                    if (!is.getItemMeta().getLore().isEmpty()) {
                        if (is.getItemMeta().getLore().get(0).contains("【镶嵌宝石】")) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean canInlay(ItemStack is) {
        if (is.getType() == Material.DIAMOND_SWORD) {
            List<String> list = is.getItemMeta().getLore();
            if (list != null) {
                if (!list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        String x = list.get(i);
                        if (is.getItemMeta().getLore().get(i).contains("○ 空镶嵌孔")) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static int getInlayAmount(ItemStack is) {
        int count = 0;
        if (is.getType() == Material.DIAMOND_SWORD) {
            List<String> list = is.getItemMeta().getLore();
            if (list != null) {
                if (!list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        String x = list.get(i);
                        if (x.contains("○") || x.contains("●")) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    public static Jewel.JewelType getJewelType(ItemStack is) {
        for (Jewel.JewelType t : Jewel.JewelType.values()) {
            if (is.getType() == t.getMaterial()) {
                return t;
            }
        }
        return null;
    }

    public static int getJewelLevel(ItemStack is) {
        String str = is.getItemMeta().getLore().get(1);
        String des = "★";
        int count = 0, offset = 0;
        while ((offset = str.indexOf(des, offset)) != -1) {
            offset = offset + des.length();
            count++;
        }
        return count;
    }

}
