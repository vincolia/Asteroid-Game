package application;

import javafx.application.Application;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyCode;
import javafx.animation.AnimationTimer;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.concurrent.atomic.AtomicInteger;

public class AsteroidApplication extends Application{
	
	public static int WIDTH = 600;
	public static int HEIGHT = 400;
	
	public static Ship ships;
	
	
	@Override
	public void start(Stage stage) throws Exception{
		// Create main pane
		Pane pane = new Pane();
		pane.setPrefSize(WIDTH, HEIGHT);
		
		Text text = new Text(10, 20, "Points: 0");
		text.setFill(Color.GRAY);
		
		AtomicInteger points = new AtomicInteger();
		
		// Creating player ship
		Ship ship = new Ship(WIDTH / 2, HEIGHT / 2);
		
		// Creating asteroids list then randomize their location
		List<Asteroid> asteroids = new ArrayList<>();
		
		for (int i = 0; i < 15; i++) {
			Random rand = new Random();
			
			if (rand.nextInt(2) == 0) {
				Asteroid asteroid = new Asteroid (rand.nextInt(WIDTH / 3), rand.nextInt(HEIGHT));
				asteroids.add(asteroid);
			} else {
				Asteroid asteroid = new Asteroid (rand.nextInt(WIDTH / 3) + WIDTH / 2 + WIDTH / 6, rand.nextInt(HEIGHT));
				asteroids.add(asteroid);
			} 
		}
		
		// Creating bullet list to shoot and interval each shot
		List<Bullet> bullets = new ArrayList<>();
		BulletInterval interval = new BulletInterval();
		
		// Adding player ship, asteroids, text in the pane
		pane.getChildren().add(ship.getCharacter());
		asteroids.forEach(asteroid -> pane.getChildren().add(asteroid.getCharacter()));
		pane.getChildren().add(text);
		
		Scene scene = new Scene(pane);
			
		// Setting pressedKeys Map to move ship LEFT/RIGHT
		Map<KeyCode, Boolean> pressedKeys = new HashMap<>();

		scene.setOnKeyPressed((event) -> {
		    pressedKeys.put(event.getCode(), Boolean.TRUE);
		});

		scene.setOnKeyReleased((event) -> {
		    pressedKeys.put(event.getCode(), Boolean.FALSE);
		});
	    
		new AnimationTimer() {

		    @Override
		    public void handle(long now) {
		    	
		    	// pressedKeys W,A,S,D or UP,LEFT,DOWN,RIGHT arrow keys to move ship
		        if(pressedKeys.getOrDefault(KeyCode.LEFT, false) || pressedKeys.getOrDefault(KeyCode.A, false)) {
		            ship.turnLeft();
		        }

		        if(pressedKeys.getOrDefault(KeyCode.RIGHT, false) || pressedKeys.getOrDefault(KeyCode.D, false)) {
		            ship.turnRight();
		        }
		        
		        if(pressedKeys.getOrDefault(KeyCode.UP, false) || pressedKeys.getOrDefault(KeyCode.W, false)) {
		        	ship.accelerate();
		        }
		        
		        if(pressedKeys.getOrDefault(KeyCode.DOWN, false)) {
		        	ship.decelerate();
		        }
		        
		        // pressedKey space bar to shoot
		        // Set millis then divide it by 500 to get 0.5s which will then use as interval per shot
		        Long timeMillis = System.currentTimeMillis() / 500;
		        
		        // Shoot First bullet then stop
		        if (pressedKeys.getOrDefault(KeyCode.SPACE, false) && !interval.getFirstShot() == true) {
		            // we shoot
		            Bullet bullet = new Bullet((int) ship.getCharacter().getTranslateX(), (int) ship.getCharacter().getTranslateY());
		            bullet.getCharacter().setRotate(ship.getCharacter().getRotate());
		            bullets.add(bullet);
		            
		            bullet.accelerate();
		            bullet.setMovement(bullet.getMovement().normalize().multiply(3));
		        
		            pane.getChildren().add(bullet.getCharacter());
		            
		            interval.setFirstShot();	// Set to true
		            interval.setLastShot(timeMillis);		// save timeMillis to lastShot
		        
		        }
		        // Subsequent bullet with 0.5s interval and 4 bullets at most
		        if (pressedKeys.getOrDefault(KeyCode.SPACE, false) && timeMillis - interval.getLastShot() >= 1 
		        													&& bullets.size() < 4){			
		            // we shoot
		            Bullet bullet = new Bullet((int) ship.getCharacter().getTranslateX(), (int) ship.getCharacter().getTranslateY());
		            bullet.getCharacter().setRotate(ship.getCharacter().getRotate());
		            
		            bullets.add(bullet);
		            
		            bullet.accelerate();
		            bullet.setMovement(bullet.getMovement().normalize().multiply(3));	        	       
		        	
		            pane.getChildren().add(bullet.getCharacter());
		            
		            interval.setLastShot(timeMillis);		// save timeMillis to lastShot
		        
		        }
		        
		        // remove asteroids and bullet when they collide to each other        
		        bullets.forEach(bullet -> {
		        	asteroids.forEach(asteroid -> {
		        		if (bullet.collide(asteroid)) {
		        			bullet.setAlive(false);
		        			asteroid.setAlive(false);
		        		}
		        	});
		        	
		        	if (!bullet.isAlive()) {
		        		text.setText("Points: " + points.addAndGet(500));
		        	}
		        });
		        
		        bullets.stream()
		        		.filter(bullet -> !bullet.isAlive())
		        		.forEach(bullet -> pane.getChildren().remove(bullet.getCharacter()));
		        
		        bullets.removeAll(bullets.stream()
		        						.filter(bullet -> !bullet.isAlive())
		        						.collect(Collectors.toList()));
		        
		        asteroids.stream()
		        		.filter(asteroid -> !asteroid.isAlive())
		        		.forEach(asteroid -> pane.getChildren().remove(asteroid.getCharacter()));
		        
		        asteroids.removeAll(asteroids.stream()
		        							.filter(asteroid -> !asteroid.isAlive())
		        							.collect(Collectors.toList()));
		        
		        // 0.5% chance to add more asteroids   
		        if(Math.random() < 0.005) {
		        	Asteroid asteroid = new Asteroid(WIDTH, HEIGHT);
		     
		        	if(!asteroid.collide(ship	)) {
		        		asteroids.add(asteroid);
		        		pane.getChildren().add(asteroid.getCharacter());
		        	}
		        }
		        
		        // to move
		        ship.move();
		        asteroids.forEach(asteroid -> asteroid.move());
		        bullets.forEach(bullet -> bullet.move());
		        
		        // End the game when ship collides with the asteroids
		        asteroids.forEach(asteroid -> {
		        	if (ship.collide(asteroid)) {
			        	stop();
			        }
		        });	        
		    }		       
		}.start();
		
		
		stage.setTitle("Asteroid");
		stage.setScene(scene);
		stage.show();
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(AsteroidApplication.class);
	}

}
