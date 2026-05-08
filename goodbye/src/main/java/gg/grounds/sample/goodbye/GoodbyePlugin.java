package gg.grounds.sample.goodbye;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class GoodbyePlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("GoodbyePlugin enabled.");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.quitMessage(
            Component.text("Bye, ", NamedTextColor.AQUA)
                .append(event.getPlayer().displayName())
                .append(Component.text("!", NamedTextColor.AQUA))
        );
    }
}
