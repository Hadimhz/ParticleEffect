package dev.hadimhz.particles.listener;

import dev.hadimhz.particles.database.ParticleDao;
import dev.hadimhz.particles.registries.PlayerRegistry;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class PlayerListener implements Listener {

    private final PlayerRegistry registry;
    private final ParticleDao particleDao;


    public PlayerListener(Plugin plugin, PlayerRegistry registry, ParticleDao particleDao) {
        this.registry = registry;
        this.particleDao = particleDao;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void onJoin(PlayerJoinEvent event) {

        particleDao.getUser(event.getPlayer().getUniqueId()).thenAccept(integer -> {

            if (integer == null) return;

            registry.set(event.getPlayer().getUniqueId(), integer);
        });

    }

    public void onLeave(PlayerQuitEvent event) {

        registry.remove(event.getPlayer().getUniqueId());

    }
}
