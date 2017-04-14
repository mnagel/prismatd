package com.avona.games.towerdefence;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class V2Test {
	@Test
	public void rotate() throws Exception {
		V2 o = new V2();
		V2 v = new V2(1, 1);
		V2 r = v.clone().rotate(o, 90);
		V2 r2 = v.clone().rotate(v, 90);

		// enforce mathematical comparison by going to string-land
		assertThat(r.toString(), is(new V2(-1, 1).toString()));
		assertThat(r.clone().rotate(o, -90).toString(), is(v.toString()));
		assertThat(r.clone().rotate(o, 270).toString(), is(v.toString()));

		// TODO: unclear if correct
		assertThat(r2.toString(), is(new V2(1, 1).toString()));
	}

}