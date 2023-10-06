package dev.hadimhz.particles.command;

import dev.hadimhz.particles.Config;
import dev.hadimhz.particles.database.ParticleDao;
import dev.hadimhz.particles.lib.Chat;
import dev.hadimhz.particles.registries.PlayerRegistry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ParticleStopCommand implements CommandExecutor {

    private final Config config;
    private final PlayerRegistry playerRegistry;
    private final ParticleDao dao;

    public ParticleStopCommand(Config config, PlayerRegistry playerRegistry, ParticleDao dao) {
        this.config = config;
        this.playerRegistry = playerRegistry;
        this.dao = dao;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Chat.translate("&cOnly players can run this command"));
            return false;
        }

        dao.deleteUser(player.getUniqueId()).thenAccept(aBoolean -> {

            if (!aBoolean) {
                player.sendMessage(Chat.translate("&cAn error occurred while setting your particle."));
                return;
            }

            sender.sendMessage(Chat.translate(config.MESSAGES_STOP));

            playerRegistry.remove(player.getUniqueId());
        });

        return true;
    }
}
