package com.avona.games.towerdefence.tower;

import com.avona.games.towerdefence.RGB;
import com.avona.games.towerdefence.TimedCodeManager;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.enemySelection.EnemySelectionPolicy;
import com.avona.games.towerdefence.enemySelection.NearestEnemyPolicy;
import com.avona.games.towerdefence.enemySelection.NearestEnemyWithColourPolicy;
import com.avona.games.towerdefence.particle.PaintballParticle;
import com.avona.games.towerdefence.particle.Particle;
import com.avona.games.towerdefence.particleCollidors.NearestEnemyCollidorPolicy;

/**
 * Created by mna on 14.04.17.
 */

public class PaintballTower extends Tower {
    public PaintballTower(final TimedCodeManager timedCodeManager,
                              final int level) {
        super(timedCodeManager, getPolicyForLevel(level),
                new NearestEnemyCollidorPolicy(), level);
        strength = new RGB(0, level * 10 + 10, 0);
    }

    public PaintballTower(final PaintballTower other) {
        super(other);
    }

    private static EnemySelectionPolicy getPolicyForLevel(int level) {
        if (level > 1) {
            return new NearestEnemyWithColourPolicy(false, true, false);
        }
        return new NearestEnemyPolicy();
    }

    @Override
    public Tower clone() {
        return new PaintballTower(this);
    }

    @Override
    public Particle makeParticle(final Enemy e) {
        return new PaintballParticle(location, e, enemyParticleCollidorPolicy,
                150 + 2 * (level - 1), new RGB(0, 500 * (level + 1), 0));
    }
}
