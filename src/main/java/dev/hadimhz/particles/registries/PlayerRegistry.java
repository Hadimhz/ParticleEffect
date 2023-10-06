package dev.hadimhz.particles.registries;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerRegistry {

    private final Map<UUID, Integer> map = new HashMap<>();


    public Map<UUID, Integer> getMap() {
        return map;
    }

    public void set(UUID uuid, int id) {
        this.map.put(uuid, id);
    }

    public void remove(UUID uuid) {
        this.map.remove(uuid);
    }

}
