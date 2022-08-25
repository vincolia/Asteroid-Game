package application;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public abstract class Character {
	
	private Point2D movement;
	private Polygon character;
	private Boolean isAlive;
	
	public Character (Polygon poly, int x, int y) {
		character = poly;
		character.setTranslateX(x);
		character.setTranslateY(y);
		
		movement = new Point2D(0, 0);
		isAlive = true;
		
	}
	
	public Polygon getCharacter() {
		return character;
	}
	
	public void turnLeft() {
		character.setRotate(character.getRotate() - 5);
	
	}
	
	public void turnRight() {
		character.setRotate(character.getRotate() + 5);
	}
	
	
	public void move() {
		character.setTranslateX(character.getTranslateX() + movement.getX());
		character.setTranslateY(character.getTranslateY() + movement.getY());
		
		if (character.getTranslateX() < 0) {
			character.setTranslateX(character.getTranslateX() + AsteroidApplication.WIDTH);
		}
		
		if (character.getTranslateX() > AsteroidApplication.WIDTH) {
			character.setTranslateX(character.getTranslateX() % AsteroidApplication.WIDTH);
		}
		
		if (character.getTranslateY() < 0) {
			character.setTranslateY(character.getTranslateY() + AsteroidApplication.HEIGHT);
		}
		
		if (character.getTranslateY() > AsteroidApplication.HEIGHT) {
			character.setTranslateY(character.getTranslateY() % AsteroidApplication.HEIGHT);
		}
		
		
	}
	
	public void accelerate() {
		double changeX = Math.cos(Math.toRadians(character.getRotate()));
		double changeY = Math.sin(Math.toRadians(character.getRotate()));
		
		changeX *= 0.05;
		changeY *= 0.05;
		
		movement = movement.add(changeX, changeY);
	}
	
	public void decelerate() {
		double changeX = Math.cos(Math.toRadians(character.getRotate()));
		double changeY = Math.cos(Math.toRadians(character.getRotate()));
		
		changeX *= 0.05;
		changeY *= 0.05;
		
		movement = movement.add(changeX * -1, changeY * -1);
	}
	
	public boolean collide(Character other) {
		Shape collisionArea = Shape.intersect(character, other.getCharacter());
		
		return collisionArea.getBoundsInLocal().getWidth() != -1;
	}
	
	public void setMovement(Point2D movement) {
		this.movement = movement;
	}
	
	public Point2D getMovement() {
		return movement;
	}
	
	public void setAlive(boolean set) {
		isAlive = set;
	}
	
	public boolean isAlive() {
		return isAlive;
	}
	
}
