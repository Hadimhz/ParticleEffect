package dev.hadimhz.particles.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ParticleDao {

    private final Database database;

    public ParticleDao(Database database) {
        this.database = database;

        try {
            CompletableFuture.supplyAsync(() -> {

                this.database.createTable("particles", "`uuid` VARCHAR(36) NOT NULL COLLATE 'latin1_swedish_ci', `selected` TINYINT(4) NULL DEFAULT NULL");

                return null;

            }).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    public CompletableFuture<Map<UUID, Integer>> getEntries() {

        return CompletableFuture.supplyAsync(() -> {

            final Map<UUID, Integer> toReturn = new HashMap<>();

            try (Connection connection = database.getConnection()) {
                try (PreparedStatement stm = connection.prepareStatement("SELECT * FROM `user-data`;")) {

                    try (ResultSet resultSet = stm.executeQuery()) {
                        while (resultSet.next()) {
                            toReturn.put(UUID.fromString(resultSet.getString(1)), resultSet.getInt(2));
                        }
                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return toReturn;

        });
    }


    public CompletableFuture<Integer> getUser(UUID uuid) {

        return CompletableFuture.supplyAsync(() -> {

            try (Connection connection = database.getConnection()) {
                try (PreparedStatement stm = connection.prepareStatement("SELECT `id` FROM `user-data` WHERE `uuid` = ?;")) {

                    stm.setString(1, uuid.toString());

                    try (ResultSet resultSet = stm.executeQuery()) {
                        while (resultSet.next()) {
                            return resultSet.getInt(1);
                        }
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        });
    }

    public CompletableFuture<Boolean> setUser(UUID uuid, int selected) {

        return CompletableFuture.supplyAsync(() -> {

            try (Connection connection = database.getConnection()) {
                try (PreparedStatement stm = connection.prepareStatement("INSERT INTO `user-data` (uuid, selected) VALUES (?, ?) ON DUPLICATE KEY UPDATE selected = ?;")) {

                    stm.setString(1, uuid.toString());
                    stm.setInt(2, selected);
                    stm.setInt(3, selected);

                    return stm.executeUpdate() > 0;

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return false;
        });
    }

    public CompletableFuture<Boolean> deleteUser(UUID uuid) {

        return CompletableFuture.supplyAsync(() -> {

            try (Connection connection = database.getConnection()) {
                try (PreparedStatement stm = connection.prepareStatement("DELETE FROM `user-data` WHERE `uuid` = ?;")) {
                    stm.setString(1, uuid.toString());

                    return stm.executeUpdate() > 0;

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;

        });
    }

}
