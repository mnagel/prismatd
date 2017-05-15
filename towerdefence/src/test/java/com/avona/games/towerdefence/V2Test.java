package com.avona.games.towerdefence;

import com.avona.games.towerdefence.core.V2;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class V2Test {
	@Test
	public void rotate() throws Exception {
		V2 o = new V2();
		V2 v = new V2(1, 1);
		V2 r = v.clone2().rotate(o, 90);
		V2 r2 = v.clone2().rotate(v, 90);

		// enforce mathematical comparison by going to string-land
		assertThat(r.toString(), is(new V2(-1, 1).toString()));
		assertThat(r.clone2().rotate(o, -90).toString(), is(v.toString()));
		assertThat(r.clone2().rotate(o, 270).toString(), is(v.toString()));

		// TODO: unclear if correct
		assertThat(r2.toString(), is(new V2(1, 1).toString()));
	}

	@Test
	public void setLength() throws Exception {
		V2 v11 = new V2(3, 4);
		v11.setLength(10);
		assertThat(v11.x, is(6.0f));
		assertThat(v11.y, is(8.0f));
	}

	@Test
	public void setLengthIdem() throws Exception {
		V2 v11 = new V2(3, 4);
		v11.setLength(5);
		assertThat(v11.x, is(3.0f));
		assertThat(v11.y, is(4.0f));

		// again
		v11.setLength(5);
		assertThat(v11.x, is(3.0f));
		assertThat(v11.y, is(4.0f));
	}

	@Test
	public void setLengthIdem0() throws Exception {
		V2 v11 = new V2(3, 4);
		v11.setLength(0);
		assertThat(v11.x, is(0.0f));
		assertThat(v11.y, is(0.0f));

		// again
		v11.setLength(0);
		assertThat(v11.x, is(0.0f));
		assertThat(v11.y, is(0.0f));
	}

	@Test
	public void setLengthNeg() throws Exception {
		V2 v11 = new V2(3, 4);
		v11.setLength(-5);
		assertThat(v11.x, is(-3.0f));
		assertThat(v11.y, is(-4.0f));
	}
}
