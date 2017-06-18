package com.avona.games.towerdefence.balancing;

import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.util.Util;

import java.util.List;
import java.util.Locale;

public class BalancingHelper {
	public String rateFleet(List<Tower> fleet) {
		/*
		float worth = (float)fleet.stream().mapToDouble(Tower::getPrice).sum();
		RGB dps = fleet.stream().map(t -> t.getDamage().scaled(1.0f / t.getReloadTime() * t.getRange() / new RedEnemy(20).getVelocity().length())).collect(
				() -> new RGB(0, 0, 0), // create new acc's
				(acc, next) -> acc.add(next), // consume next value into acc
				(acc1, acc2) -> acc1.add(acc2) // combine two acc's (parallelism, ...)
		);
		*/

		float worth = 0;
		for (Tower t : fleet) {
			worth += t.getPrice();
		}

		RGB dps = new RGB(0, 0, 0);
		for (Tower t : fleet) {
			RGB local = t.getDps();
			Util.log("debug: local: " + local);
			dps.add(local);
		}

		return String.format(
				Locale.US,
				"\t$%.1f\tDPS %s\tDPS/k$ %s\tFDPS %.1f\tFDPS/k$ %.1f",
				worth,
				dps,
				dps.scaled(1000.0f / worth),
				dps.length(),
				dps.scaled(1000.0f / worth).length()
		);
	}
}
