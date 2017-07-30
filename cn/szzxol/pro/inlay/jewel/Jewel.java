package cn.szzxol.pro.inlay.jewel;

import cn.szzxol.pro.inlay.Inlay;
import org.bukkit.Material;

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

    public static int MaxLevel = 10;

    public JewelType type;
    public int Level;
    public double value;

    public Jewel(JewelType type, int level) {
        this.type = type;
        this.Level = level;
        this.value = Inlay.instance.getConfig().getDouble("Settings.DIAMOND.Level." + this.Level);
    }

    public String getLevelSign() {
        String Sign = "";
        for (int i = 1; i <= MaxLevel; i++) {
            Sign += i <= this.Level ? "★" : "☆";
        }
        return Sign;
    }

    public static String getLevelSign(int level) {
        String Sign = "";
        for (int i = 1; i <= MaxLevel; i++) {
            Sign += i <= level ? "★" : "☆";
        }
        return Sign;
    }

}
