package application;

public class BulletInterval {
	
	private boolean firstShot;
	private Long lastShot;
	
	public BulletInterval() {
		firstShot = false;
	}
	
	public void setFirstShot() {
		firstShot = true;
	}
	
	public boolean getFirstShot() {
		return firstShot;
	}
	
	public void setLastShot(Long time) {
		lastShot = time;
	}
	
	public Long getLastShot() {
		return lastShot;
	}	
}
