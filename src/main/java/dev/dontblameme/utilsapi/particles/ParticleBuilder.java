package dev.dontblameme.utilsapi.particles;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParticleBuilder {

    private final Particle type;
    private float x = 0;
    private float y = 0;
    private float z = 0;
    private float speed = 0;
    private float offsetX = 0;
    private float offsetY = 0;
    private float offsetZ = 0;
    private int amount = 0;
    private List<Player> players = new ArrayList<>();

    /**
     *
     * @param type Type of particle which should be used
     */
    public ParticleBuilder(Particle type) {
        this.type = type;
    }

    /**
     *
     * @param offset OffsetX which the particles should have to their location
     * @return Instance of the current state of the builder
     */
    public ParticleBuilder offsetX(float offset) {
        return offset(offset, offsetY, offsetZ);
    }

    /**
     *
     * @param offset OffsetY which the particles should have to their location
     * @return Instance of the current state of the builder
     */
    public ParticleBuilder offsetY(float offset) {
        return offset(offsetX, offset, offsetZ);
    }

    /**
     *
     * @param offset OffsetZ which the particles should have to their location
     * @return Instance of the current state of the builder
     */
    public ParticleBuilder offsetZ(float offset) {
        return offset(offsetX, offsetY, offset);
    }

    /**
     *
     * @param x OffsetX which the particles should have to their location
     * @param y OffsetY which the particles should have to their location
     * @param z OffsetZ which the particles should have to their location
     * @return Instance of the current state of the builder
     */
    public ParticleBuilder offset(float x, float y, float z) {
        this.offsetX = x;
        this.offsetY = y;
        this.offsetZ = z;
        return this;
    }

    /**
     *
     * @param location Location on which the particles should spawn
     * @return Instance of the current state of the builder
     */
    public ParticleBuilder location(Location location) {
        return location((float) location.getX(), (float) location.getY(), (float) location.getZ());
    }

    /**
     *
     * @param x PositionX on which the particles should spawn
     * @param y PositionY on which the particles should spawn
     * @param z PositionZ on which the particles should spawn
     * @return Instance of the current state of the builder
     */
    public ParticleBuilder location(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    /**
     *
     * @param speed Speed on which this particles should move
     * @return Instance of the current state of the builder
     */
    public ParticleBuilder speed(float speed) {
        this.speed = speed;
        return this;
    }

    /**
     *
     * @param amount Amount of particles which should be displayed
     * @return Instance of the current state of the builder
     */
    public ParticleBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    /**
     *
     * @param p Multiple players which should be added
     * @return Instance of the current state of the builder
     */
    public ParticleBuilder addPlayer(Player... p) {
        Arrays.stream(p).forEach(this::addPlayer);
        return this;
    }

    /**
     *
     * @param p Player which should be added
     * @return Instance of the current state of the builder
     */
    public ParticleBuilder addPlayer(Player p) {
        this.players.add(p);
        return this;
    }

    /**
     * @apiNote Sends the packet to every player provided
     */
    public void send() {
        players.forEach(p -> p.getWorld().spawnParticle(this.type, this.x, this.y ,this.z, this.amount, this.offsetX, this.offsetY, this.offsetZ, this.speed));
    }

}
