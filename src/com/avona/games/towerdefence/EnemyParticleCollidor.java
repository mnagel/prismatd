package com.avona.games.towerdefence;

import java.util.List;

public interface EnemyParticleCollidor {
	public void collideParticlesWithEnemies(List<Enemy> enemies, float dt);
	
	public void registerEnemyVsParticle(Enemy e, Particle p);
}
