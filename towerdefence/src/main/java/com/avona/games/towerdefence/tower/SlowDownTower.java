package com.avona.games.towerdefence.tower;

import com.avona.games.towerdefence.RGB;
import com.avona.games.towerdefence.TimedCodeManager;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.enemySelection.EnemySelectionPolicy;
import com.avona.games.towerdefence.enemySelection.NearestEnemyPolicy;
import com.avona.games.towerdefence.particle.Particle;
import com.avona.games.towerdefence.particle.SlowDownParticle;
import com.avona.games.towerdefence.particleCollidors.NearestEnemyColliderPolicy;

public class SlowDownTower extends Tower {
    public SlowDownTower(final TimedCodeManager timedCodeManager, final int level) {
        super(timedCodeManager, new NearestEnemyColliderPolicy(), level);
        color = new RGB(1.0f, 1.0f, 1.0f);
    }

    public SlowDownTower(final SlowDownTower other) {
        super(other);
    }

    @Override
    public String getName() {
        return "Slow-down tower";
    }

    @Override
    public Tower clone() {
        return new SlowDownTower(this);
    }

    @Override
    public Particle makeParticle(Enemy e) {
        return new SlowDownParticle(
                location,
                e,
                enemyParticleColliderPolicy,
                50,
                new RGB(1.0f, 1.0f, 1.0f),
                10
        );
    }

    @Override
    protected EnemySelectionPolicy getPolicyForLevel(int level) {
        return new NearestEnemyPolicy();
    }
}
