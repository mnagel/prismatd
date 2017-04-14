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

public class PaintRedTower extends Tower {
    private RGB particleMask;

    public PaintRedTower(final TimedCodeManager timedCodeManager,
                         final int level) {
        super(timedCodeManager, new NearestEnemyCollidorPolicy(), level);
        color = new RGB(0, level * 10 + 10, level * 10 + 10);
        particleMask = new RGB(1.0f, 0, 0);
    }

    public PaintRedTower(final PaintRedTower other) {
        super(other);
        this.particleMask = other.particleMask;
    }

    public String getName() { return "Paint Red Tower"; }

    protected EnemySelectionPolicy getPolicyForLevel(int level) {
        if (level > 1) {
            return new NearestEnemyWithColourPolicy(false, true, false);
        }
        return new NearestEnemyPolicy();
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
        this.enemySelectionPolicy = getPolicyForLevel(level);
    }

    @Override
    public Tower clone() {
        return new PaintRedTower(this);
    }

    @Override
    public Particle makeParticle(final Enemy e) {
        return new PaintballParticle(
                location,
                e,
                enemyParticleCollidorPolicy,
                150 + 2 * (level - 1),
                new RGB(0, 500 * (level + 1), 0),
                particleMask);
    }
}
