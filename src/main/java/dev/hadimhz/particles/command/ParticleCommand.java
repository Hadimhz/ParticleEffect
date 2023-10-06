package dev.hadimhz.particles.command;

import dev.hadimhz.particles.Config;
import dev.hadimhz.particles.database.ParticleDao;
import dev.hadimhz.particles.lib.Chat;
import dev.hadimhz.particles.registries.ParticleRegistry;
import dev.hadimhz.particles.registries.PlayerRegistry;
import dev.hadimhz.particles.wrapper.ParticleWrapper;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class ParticleCommand implements CommandExecutor {


    private final Config config;
    private final ParticleRegistry particleRegistry;
    private final PlayerRegistry playerRegistry;
    private final ParticleDao dao;

    public ParticleCommand(Config config, ParticleRegistry particleRegistry, PlayerRegistry playerRegistry, ParticleDao dao) {
        this.config = config;
        this.particleRegistry = particleRegistry;
        this.playerRegistry = playerRegistry;
        this.dao = dao;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Chat.translate("&cOnly players can run this command"));
            return false;
        }

        final Gui gui = Gui.gui()
                .title(Chat.translate("&aParticle Selector"))
                .rows(6)
                .disableAllInteractions()
                .create();


        for (ParticleWrapper wrapper : particleRegistry.getMap().values()) {

            gui.setItem(wrapper.id(), ItemBuilder.from(wrapper.icon()).name(Chat.translate(wrapper.name())).asGuiItem(event -> {

                final Player player = (Player) event.getWhoClicked();

                if (!player.hasPermission(wrapper.permission())) {
                    player.sendMessage(Chat.translate(config.MESSAGES_MISSING_PERMISSION));
                    return;
                }

                dao.setUser(player.getUniqueId(), wrapper.id()).thenAccept(aBoolean -> {

                    if (!aBoolean) {
                        player.sendMessage(Chat.translate("&cAn error occurred while setting your particle."));
                        return;
                    }

                    sender.sendMessage(Chat.translate(
                            config.MESSAGES_SELECTED
                                    .replaceAll("%particle_name%", wrapper.name())
                    ));

                    playerRegistry.set(player.getUniqueId(), wrapper.id());
                });

            }));

        }

        gui.open((Player) sender);

        return true;
    }
}
