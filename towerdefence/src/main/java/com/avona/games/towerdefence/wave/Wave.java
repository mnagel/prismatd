package com.avona.games.towerdefence.wave;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.TimedCode;
import com.avona.games.towerdefence.TimedCodeManager;
import com.avona.games.towerdefence.V2;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.enemyEventListeners.EnemyEventListener;
import com.avona.games.towerdefence.level.Level;

public class Wave extends TimedCode implements EnemyEventListener {
    private static final long serialVersionUID = 1L;

    public int waveNum;

    private boolean fullyDeployed = false;
    private Level level;
    private Game game;
    private TimedCodeManager timedCodeManager;
    private int curEnemy = 0;
    private WaveEnemyConfig[] enemies;
    private int activeEnemies = 0;

    public Wave(int waveNum, Game game, Level level,
                TimedCodeManager timedCodeManager, WaveEnemyConfig[] enemies) {
        this.waveNum = waveNum;
        this.game = game;
        this.level = level;
        this.timedCodeManager = timedCodeManager;
        this.enemies = enemies;

        spawnEnemy();
    }

    public boolean isFullyDeployed() {
        return fullyDeployed;
    }

    private void spawnEnemy() {
        final V2 location = level.waypoints[0].center.clone();
        WaveEnemyConfig we = enemies[curEnemy];
        Enemy e = we.enemy.clone();
        e.eventListeners.add(this);
        e.setInitialLocation(location);
        ++curEnemy;
        ++activeEnemies;
        game.onEnemySpawned(e);
        timedCodeManager.addCode(we.delay, this);
    }

    @Override
    public void execute() {
        if (curEnemy < enemies.length) {
            spawnEnemy();
        } else {
            fullyDeployed = true;
            level.waveTracker.onWaveFullyDeployed(this);
        }
    }

    private void checkEnemiesDone() {
        if (!fullyDeployed)
            return;
        if (activeEnemies > 0)
            return;

        level.waveTracker.onWaveCompleted(this);
    }

    @Override
    public void onDeathEvent(Enemy e) {
        --activeEnemies;
        checkEnemiesDone();
    }

    @Override
    public void onEscapeEvent(Enemy e) {
        --activeEnemies;
        checkEnemiesDone();
    }
}
