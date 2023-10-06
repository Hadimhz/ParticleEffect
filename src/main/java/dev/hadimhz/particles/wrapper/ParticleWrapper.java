package dev.hadimhz.particles.wrapper;

import org.bukkit.Material;
import org.bukkit.Particle;

public record ParticleWrapper(int id, String name, Material icon, Particle particle, String permission) { }
