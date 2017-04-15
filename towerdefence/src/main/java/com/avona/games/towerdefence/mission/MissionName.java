package com.avona.games.towerdefence.mission;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MissionName {
	String value();
}
