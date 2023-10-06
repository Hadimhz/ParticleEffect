package dev.hadimhz.particles;

import dev.hadimhz.particles.command.ParticleCommand;
import dev.hadimhz.particles.command.ParticleStopCommand;
import dev.hadimhz.particles.database.Database;
import dev.hadimhz.particles.database.HikariDatabase;
import dev.hadimhz.particles.database.ParticleDao;
import dev.hadimhz.particles.listener.PlayerListener;
import dev.hadimhz.particles.registries.ParticleRegistry;
import dev.hadimhz.particles.registries.PlayerRegistry;
import dev.hadimhz.particles.wrapper.ParticleWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ParticlePlugin extends JavaPlugin {

    private final ParticleRegistry particleRegistry;
    private final PlayerRegistry playerRegistry;
    private Database db;

    public ParticlePlugin() {

        this.particleRegistry = new ParticleRegistry();
        this.playerRegistry = new PlayerRegistry();
    }

    @Override
    public void onEnable() {

        final Config config = new Config(this, particleRegistry);

        db = new HikariDatabase(config.DATABASE_HOST, config.DATABASE_DATABASE, config.DATABASE_PORT, config.DATABASE_USER, config.DATABASE_PASSWORD);
        ParticleDao particleDao = new ParticleDao(db);

        new PlayerListener(this, playerRegistry, particleDao);

        getCommand("particles").setExecutor(new ParticleCommand(config, particleRegistry, playerRegistry, particleDao));
        getCommand("particlestop").setExecutor(new ParticleStopCommand(config, playerRegistry, particleDao));

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> playerRegistry.getMap().forEach((uuid, integer) -> {

            final Player player = Bukkit.getPlayer(uuid);

            if (player == null || !player.isOnline()) return;

            final ParticleWrapper wrapper = particleRegistry.getMap().get(integer);

            if (wrapper == null || !player.hasPermission(wrapper.permission())) return;

            player.getLocation().getWorld().spawnParticle(wrapper.particle(), player.getLocation().add(0, -.5, 0), 1, 0d, 0d, 0d, 0);

        }), 5, 5);
    }
}
