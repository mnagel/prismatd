package com.avona.games.towerdefence.particle;

import com.avona.games.towerdefence.RGB;
import com.avona.games.towerdefence.V2;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.particleCollidors.ParticleColliderPolicy;

public class SlowDownParticle extends Particle {
    private int speedPenalty;

    public SlowDownParticle(V2 location, Enemy target, ParticleColliderPolicy colliderPolicy, int velocity, RGB strength, int speedPenalty) {
        super(location, target, colliderPolicy, velocity, strength);
        this.speedPenalty = speedPenalty;
    }

    @Override
    public void attack(Enemy e) {
        if (isDead()) {
            return;
        }

        int speed = (int)e.getVelocity().length() - speedPenalty;
        // XXX: Enemies seem to die with a speed <= 0. TBD why.
        if (speed < 1) {
            speed = 1;
        }
        e.getVelocity().setLength(speed);

        dead = true;
    }
}
