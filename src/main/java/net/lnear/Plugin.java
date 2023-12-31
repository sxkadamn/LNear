package net.lnear;

import net.lnear.bukkit.BukkitDirection;
import net.lnear.commands.CommandContext;
import net.lnear.commands.CommandProcessingAPI;
import net.lnear.commands.SimpleCommandProcessing;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.stream.Collectors;

public final class Plugin extends JavaPlugin {

    private static Plugin instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        CommandProcessingAPI commandProcessingAPI = new SimpleCommandProcessing();
        commandProcessingAPI.registerCommand("lnear", this::processNearCommand);
    }

    private void processNearCommand(CommandContext context) {
        Player player = (Player) context.getSender();
        if (player == null || context.getArgs().length != 0 || !player.hasPermission("near.usage")) {
            return;
        }

        int rad = getPermissionRadius(player);
        StringBuilder near = new StringBuilder();

        for (Player online : getPlayersInRadius(player, rad)) {
            if (isPlayerExcluded(player, online, rad)) {
                continue;
            }

            near.append(getPlayerMessage(online.getName(), (int) player.getLocation().distance(online.getLocation())));
            near.append(getPlayerDirectionMessage(player, online));
        }

        String resultMessage = near.length() == 0
                ? getNearMessage(rad) + ChatColor.translateAlternateColorCodes('&', "&6пусто")
                : getNearMessage(rad) + "\n" + near;

        player.sendMessage(resultMessage);
    }

    private boolean isPlayerExcluded(Player player, Player online, int rad) {
        return online.getLocation().distance(player.getLocation()) > rad ||
                online.equals(player) ||
                (online.getGameMode() == GameMode.SPECTATOR && !getGamemode()) ||
                (online.hasPotionEffect(PotionEffectType.INVISIBILITY) && !getInvisibility()) ||
                online.hasPermission("near.invisibility");
    }

    private int getPermissionRadius(Player player) {
        int rad = 0;
        for (String perms : getConfig().getConfigurationSection("perms").getKeys(false)) {
            if (player.hasPermission(perms)) {
                rad = getConfig().getInt("perms." + perms);
            }
        }
        return rad;
    }

    private List<Player> getPlayersInRadius(Player player, int radius) {
        return player.getWorld().getPlayers().stream()
                .filter(online -> isPlayerInRange(player, online, radius))
                .collect(Collectors.toList());
    }

    private boolean isPlayerInRange(Player player, Player target, int radius) {
        return player.getLocation().distance(target.getLocation()) <= radius &&
                player != target &&
                target.getGameMode() != GameMode.SPECTATOR;
    }

    private String getPlayerDirectionMessage(Player player, Player online) {
        Location origin = player.getLocation();
        Vector target = online.getLocation().toVector();
        origin.setDirection(target.subtract(origin.toVector()));
        int yaw = (int) ((player.getLocation().getYaw() - origin.getYaw()) / 45.0F);
        BukkitDirection direction = new BukkitDirection(instance);
        return direction.getDirection(yaw);
    }

    private String getPlayerMessage(String playerName, int distance) {
        return getConfig().getString("messages.players_near")
                .replace("&", "§")
                .replace("{player}", playerName)
                .replace("{distance}", String.valueOf(distance));
    }

    private String getNearMessage(int rad) {
        return getConfig().getString("messages.near_messages")
                .replace("&", "§")
                .replace("{radius}", String.valueOf(rad));
    }

    public boolean getGamemode() {
        return this.getConfig().getBoolean("settings.gamemode");
    }

    public boolean getInvisibility() {
        return this.getConfig().getBoolean("settings.invisibility");
    }
}
