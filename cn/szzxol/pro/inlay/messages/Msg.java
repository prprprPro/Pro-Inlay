package cn.szzxol.pro.inlay.messages;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author I_promise
 */
public class Msg {

    public static void MsgNoPermission(Player player) {
        player.sendMessage((new StringBuilder()).append(ChatColor.RED).append(ChatColor.BOLD).append("你没有权限执行这个指令").toString());
    }

}
