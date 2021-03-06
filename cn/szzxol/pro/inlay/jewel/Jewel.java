package cn.szzxol.pro.inlay.jewel;

import static cn.szzxol.pro.inlay.Inlay.MaxLevel;
import static cn.szzxol.pro.inlay.Inlay.sign_Full;
import static cn.szzxol.pro.inlay.Inlay.sign_Null;
import static cn.szzxol.pro.inlay.jewel.Utils.getJewelLevel;
import static cn.szzxol.pro.inlay.jewel.Utils.getJewelType;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author I_promise
 */
public class Jewel {

    

    public enum JewelType {
        DIAMOND(Material.DIAMOND, "钻石");

        private final Material material;
        private final String Name;

        private JewelType(Material material, String Name) {
            this.material = material;
            this.Name = Name;
        }

        public Material getMaterial() {
            return this.material;
        }

        public String getName() {
            return this.Name;
        }
    }

    public JewelType type;
    public int Level;
    public List<String> lores;

    public Jewel(JewelType type, int level) {
        this.type = type;
        this.Level = level;
    }

    public Jewel(ItemStack is) {
        this.type = getJewelType(is);
        this.Level = getJewelLevel(is);
        List<String> lore = is.getItemMeta().getLore();
        lore.set(0, ChatColor.translateAlternateColorCodes('&', "&b&l● " + this.type.getName()));
        lore.set(1, "     " + lore.get(1));
        lore.set(2, "     " + lore.get(2));
        this.lores = lore;
    }

    public String getLevelSign() {
        String Sign = "";
        for (int i = 1; i <= MaxLevel; i++) {
            Sign += i <= this.Level ? sign_Full : sign_Null;
        }
        return Sign;
    }

    public static String getLevelSign(int level) {
        String Sign = "";
        for (int i = 1; i <= MaxLevel; i++) {
            Sign += i <= level ? sign_Full : sign_Null;
        }
        return Sign;
    }

}
