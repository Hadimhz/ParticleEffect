package dev.hadimhz.particles;

import dev.hadimhz.particles.exception.ConfigurationException;
import dev.hadimhz.particles.registries.ParticleRegistry;
import dev.hadimhz.particles.wrapper.ParticleWrapper;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

public class Config {

    private final Plugin plugin;
    private final ParticleRegistry registry;

    public String DATABASE_HOST, DATABASE_DATABASE, DATABASE_PORT, DATABASE_USER, DATABASE_PASSWORD;

    public String MESSAGES_SELECTED, MESSAGES_STOP, MESSAGES_MISSING_PERMISSION;


    public Config(Plugin plugin, ParticleRegistry registry) {
        this.plugin = plugin;
        this.registry = registry;

        plugin.saveDefaultConfig();

        setup();
    }

    public void setup() {

        final ConfigurationSection config = plugin.getConfig();

        DATABASE_HOST = config.getString("database.host");
        DATABASE_DATABASE = config.getString("database.database");
        DATABASE_PORT = config.getString("database.port");
        DATABASE_USER = config.getString("database.user");
        DATABASE_PASSWORD = config.getString("database.password");

        MESSAGES_SELECTED = config.getString("messages.selected");
        MESSAGES_STOP = config.getString("messages.stop");
        MESSAGES_MISSING_PERMISSION = config.getString("messages.missing_permission");

        setupParticles();

    }

    public void setupParticles() {

        registry.getMap().clear();

        ConfigurationSection config = plugin.getConfig().getConfigurationSection("particles");

        if (config == null)
            throw new ConfigurationException("Couldn't find the configuration section with the name 'particles'");

        for (String tempId : config.getKeys(false)) {

            try {

                final ConfigurationSection section = config.getConfigurationSection(tempId);
                assert section != null;

                final int id = Integer.parseInt(tempId);

                registry.set(new ParticleWrapper(
                        id,
                        section.getString("name"),
                        Material.valueOf(section.getString("item")),
                        Particle.valueOf(section.getString("particle")),
                        section.getString("permission")
                ));

            } catch (NumberFormatException numberFormatException) {
                throw new ConfigurationException("The id: " + tempId + " is not an integer");
            }
        }
    }

    public void reload() {
        plugin.reloadConfig();
        setup();
    }


}
