package com.avona.games.towerdefence.particle;

import com.avona.games.towerdefence.RGB;
import com.avona.games.towerdefence.V2;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.particleCollidors.ParticleCollidorPolicy;

/**
 * Created by mna on 14.04.17.
 */

public class PaintballParticle extends Particle {

    public PaintballParticle(V2 location, Enemy target, ParticleCollidorPolicy collidorPolicy, int velocity, RGB strength) {
        super(location, target, collidorPolicy, velocity, strength);
    }

    @Override
    public void attack(Enemy e) {
        if (isDead()) {
            return;
        }

        final RGB oldlife = e.life.clone();
        e.inflictDamage(strength);
        e.life.R += oldlife.G - e.life.G;

        dead = true;
    }
}
