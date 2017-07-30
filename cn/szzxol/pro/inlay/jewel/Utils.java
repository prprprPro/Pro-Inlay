package cn.szzxol.pro.inlay.jewel;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author I_promise
 */
public class Utils {

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
            if (is.getItemMeta().getLore() != null) {
                if (!is.getItemMeta().getLore().isEmpty()) {
                    for (int i = 0; i < is.getItemMeta().getLore().size(); i++) {
                        if (is.getItemMeta().getLore().get(i).contains("○ 空镶嵌孔")) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
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
