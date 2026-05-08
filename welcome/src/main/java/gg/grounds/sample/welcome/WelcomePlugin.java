package gg.grounds.sample.welcome;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class WelcomePlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("WelcomePlugin enabled.");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.joinMessage(
            Component.text("Welcome, ", NamedTextColor.YELLOW)
                .append(event.getPlayer().displayName())
                .append(Component.text("!", NamedTextColor.YELLOW))
        );
    }
}
