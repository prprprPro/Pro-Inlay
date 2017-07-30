package cn.szzxol.pro.inlay.utils;

import cn.szzxol.pro.inlay.Inlay;
import java.io.File;
import java.io.IOException;
import static org.bukkit.Bukkit.getLogger;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author I_promise
 */
public class Configuration {

    public static YamlConfiguration getConfiguration(String FileName) {
        File Config = new File(Inlay.instance.getDataFolder(), FileName + ".yml");
        return YamlConfiguration.loadConfiguration(Config);
    }

    public static void saveConfiguration(YamlConfiguration YamlConfig, String FileName) {
        File Config = new File(Inlay.instance.getDataFolder(), FileName + ".yml");
        try {
            YamlConfig.save(Config);
        } catch (IOException ex) {
        }
    }

    public static void saveDefaultYaml(String FileName) {
        File file = new File(Inlay.instance.getDataFolder(), FileName + ".yml");
        if (file.exists()) {
            getLogger().info("[pro-Essential] 检测到文件：" + FileName + ".yml");
        } else {
            Inlay.instance.saveResource(FileName + ".yml", false);
        }
    }
}
