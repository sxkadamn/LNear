package net.lnear.bukkit;

import net.lnear.Plugin;
import org.bukkit.ChatColor;

import java.util.List;

public class BukkitDirection {

    private final Plugin main;

    public BukkitDirection(Plugin plugin) {
        this.main = plugin;
    }

    public String getDirection(int yaw) {
        List<String> directions = main.getConfig().getStringList("directions");

        if (yaw >= 0 && yaw < directions.size()) {
            return directions.get(yaw);
        } else {
            return ChatColor.translateAlternateColorCodes('&', "&6Что-то не так... чини чушпан");
        }
    }

}
