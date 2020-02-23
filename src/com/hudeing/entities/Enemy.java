package com.hudeing.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.hudeing.main.Game;
import com.hudeing.main.Sound;
import com.hudeing.world.AStar;
import com.hudeing.world.Camera;
import com.hudeing.world.Vector2i;
import com.hudeing.world.World;

public class Enemy extends Entity{
	
	private double speed = 0.4;
	private int frames = 0, maxFrames = 20, index = 0, maxIndex = 1;
	private BufferedImage[] sprites;
	private int life = 10;
	private boolean isDamaged = false;
	private int damageFrames = 10, damageCurrent = 0;
	private boolean hasAgroo = false;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		sprites = new BufferedImage[2];
		sprites[0] = Game.spritesheet.getSprite(112, 16, 16, 16);
		sprites[1] = Game.spritesheet.getSprite(128, 16, 16, 16);
	}
	
	public void tick() {
		/*
		maskX = 8;
		maskY = 8;
		maskW = 10;
		maskH = 10;
		*/
		//if(Game.rand.nextInt(100) < 80) {
		
		// Normal enemy intelligence
		/*if(this.calculateDistance(this.getX(), this.getY(), Game.player.getX(), Game.player.getY()) < 50 || hasAgroo) {
			hasAgroo = true;
			if(isCollidingWithPlayer() == false) {
			if((int)x < Game.player.getX() && World.isFree((int)(x + speed), this.getY(), this) && !isColliding((int)(x + speed), this.getY())) {
				x += speed;
			} else if((int)x > Game.player.getX() && World.isFree((int)(x - speed), this.getY(), this) && !isColliding((int)(x - speed), this.getY())) {
				x -= speed;
			}
			
			if((int)y < Game.player.getY() && World.isFree(this.getX(), (int)(y + speed), this) && !isColliding(this.getX(), (int)(y + speed))) {
				y += speed;
			} else if((int)y > Game.player.getY() && World.isFree(this.getX(), (int)(y - speed), this) && !isColliding(this.getX(), (int)(y - speed))) {
				y -= speed;
			}
			//}
			} else {
				// Estamos colidindo
				if(Game.rand.nextInt(100) < 10) {
					Sound.hurtEffect.play();
					Game.player.life -= Game.rand.nextInt(3);
					Game.player.isDamaged = true;
					//System.out.println("Vida: " + Game.player.life);
				
				}
			}
		}*/
		
		//AStar intelligence
		maskX = 5;
		maskY = 8;
		mWidth = 8;
		mHeight = 8;
		if(!isCollidingWithPlayer()) {
			if(path == null  || path.size() == 0) {
				Vector2i start = new Vector2i((int)(x / 16), (int)(y / 16));
				Vector2i end = new Vector2i((int)((double)Game.player.getX() / 16), (int)((double)Game.player.getY() / 16));
				path = AStar.findPath(Game.world, start, end);
			}
		} else {
			// Estamos colidindo
			if(Game.rand.nextInt(100) < 5) {
				Sound.hurtEffect.play();
				Game.player.life -= Game.rand.nextInt(3);
				Game.player.isDamaged = true;
				//System.out.println("Vida: " + Game.player.life);
			
			}
		}
		
		if(Game.rand.nextInt(100) < 90) {
			followPath(path);
		}
			
		if(Game.rand.nextInt(100) < 5) {
			Vector2i start = new Vector2i((int)(x / 16), (int)(y / 16));
			Vector2i end = new Vector2i((int)((double)Game.player.getX() / 16), (int)((double)Game.player.getY() / 16));
			path = AStar.findPath(Game.world, start, end);
		}
		
		frames++;
		if(frames == maxFrames) {
			frames = 0;
			index++;
			if(index > maxIndex)
				index = 0;
		}
		
		collidingBullet();
		
		if(life <= 0) {
			destroySelf();
			return;
		}
		
		if(isDamaged) {
			this.damageCurrent++;
			if(this.damageCurrent == this.damageFrames) {
				this.damageCurrent = 0;
				isDamaged = false;
			}
		}
	}
	
	public void destroySelf() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}
	
	public void collidingBullet() {
		for(int i = 0; i < Game.bullets.size(); i++) {
			BulletShoot bullet = Game.bullets.get(i);
			if(Entity.isColliding(this, bullet)) {
				isDamaged = true;
				life--;
				Game.bullets.remove(bullet);
				return;
			}
			
		}
	}
	
	public boolean isCollidingWithPlayer() {
		Rectangle currentEnemy = new Rectangle(this.getX() + maskX, this.getY() + maskY, mWidth, mHeight);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);
		
		//return currentEnemy.intersects(player);
		if(currentEnemy.intersects(player) && this.z == Game.player.z) {
			return true;
		}
		return false;
	}
	
	public void render(Graphics g) {
		if(!isDamaged)
			g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		else
			g.drawImage(Entity.ENEMY_FEEDBACK, this.getX() - Camera.x, this.getY() - Camera.y, null);
		//g.setColor(Color.BLUE);
		////g.fillRect(this.getX() - Camera.x, this.getY() - Camera.y, 16, 16);
		//g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y, mWidth, mHeight);
	}

}
