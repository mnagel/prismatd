package com.avona.games.towerdefence;

public class V2 {
	public float x = 0.0f;
	public float y = 0.0f;
	
	public V2() {
		
	}
	
	public V2(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public V2(V2 orig) {
		x = orig.x;
		y = orig.y;
	}
	
	public void add(V2 v) {
		x += v.x;
		y += v.y;
	}
	
	public void sub(V2 v) {
		x -= v.x;
		y -= v.y;
	}
	
	public void mult(float f) {
		x *= f;
		y *= f;
	}
	
    public float abs() {
    	return (float) Math.sqrt(x*x + y*y);
    }
    
    public float abs_sq() {
    	return (x*x + y*y);
    }
    

    public void normalize() {
    	float f = 1.0f / abs();
    	x *= f;
    	y *= f;
    }
    
    public static float dist(V2 from, V2 to) {
    	return (float) Math.sqrt((to.x - from.x) * (to.x - from.x) + (to.y - from.y) * (to.y - from.y));
    }
    
    public static float dist_sq(V2 from, V2 to) {
    	return (to.x - from.x) * (to.x - from.x) + (to.y - from.y) * (to.y - from.y);
    	}
    
    public float dist(V2 dest) {
    	return (float) Math.sqrt((dest.x - this.x) * (dest.x - this.x) + (dest.y - this.y) * (dest.y - this.y));
    }
    
    public float dist_sq(V2 dest) {
    	return (dest.x - this.x) * (dest.x - this.x) + (dest.y - this.y) * (dest.y - this.y);
    }
}
