package application;

import java.util.Random;

public class Asteroid extends Character{
	
	private double movements;
	
	public Asteroid(int x, int y) {
		super(new PolygonShape().createPolygon(), x, y);
		
		Random rand = new Random();
		
		super.getCharacter().setRotate(rand.nextInt(360));
		
		int acceleration = rand.nextInt(40) + 5;
		
		for (int i = 0; i < acceleration; i++) {
			accelerate();
		}
		
		movements = rand.nextDouble() - 0.5;
	}
	
	public void move() {
		super.move();
		
		super.getCharacter().setRotate(super.getCharacter().getRotate() + movements);
	}
}
