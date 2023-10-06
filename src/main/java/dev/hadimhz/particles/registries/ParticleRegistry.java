package dev.hadimhz.particles.registries;

import dev.hadimhz.particles.wrapper.ParticleWrapper;

import java.util.HashMap;
import java.util.Map;

public class ParticleRegistry {

    private final Map<Integer, ParticleWrapper> map = new HashMap<>();

    public Map<Integer, ParticleWrapper> getMap() {
        return map;
    }

    public void set(ParticleWrapper wrapper) {
        this.map.put(wrapper.id(), wrapper);
    }

}
