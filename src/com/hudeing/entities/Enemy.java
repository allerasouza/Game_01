package com.hudeing.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.hudeing.main.Game;
import com.hudeing.world.Camera;
import com.hudeing.world.World;

public class Enemy extends Entity{
	
	private double speed = 1;
	
	private int maskX = 8, maskY = 8, maskW = 10, maskH = 10;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}
	
	public void tick() {
		/*
		maskX = 8;
		maskY = 8;
		maskW = 10;
		maskH = 10;
		*/
		//if(Game.rand.nextInt(100) < 80) {
		if((int)x < Game.player.getX() && World.isFree((int)(x + speed), this.getY()) && !isColliding((int)(x + speed), this.getY())) {
			x += speed;
		} else if((int)x > Game.player.getX() && World.isFree((int)(x - speed), this.getY()) && !isColliding((int)(x - speed), this.getY())) {
			x -= speed;
		}
		
		if((int)y < Game.player.getY() && World.isFree(this.getX(), (int)(y + speed)) && !isColliding(this.getX(), (int)(y + speed))) {
			y += speed;
		} else if((int)y > Game.player.getY() && World.isFree(this.getX(), (int)(y - speed)) && !isColliding(this.getX(), (int)(y - speed))) {
			y -= speed;
		}
		//}
	}
	
	public boolean isColliding(int xNext, int yNext) {
		//Rectangle currentEnemy = new Rectangle(xNext, yNext, World.TILE_SIZE, World.TILE_SIZE);
		Rectangle currentEnemy = new Rectangle(xNext + maskX, yNext + maskY, maskW, maskH);
		for(int i = 0; i < Game.enemies.size(); i++) {
			Enemy en = Game.enemies.get(i);
			if(en == this)
				continue;
			
			//Rectangle targetEnemy = new Rectangle(en.getX(), en.getY(), World.TILE_SIZE, World.TILE_SIZE);
			Rectangle targetEnemy = new Rectangle(en.getX() + maskX, en.getY() + maskY, maskW, maskH);
			if(currentEnemy.intersects(targetEnemy))
				return true;
		}
		return false;
	}
	
	public void render(Graphics g) {
		super.render(g);
		//g.setColor(Color.BLUE);
		//g.fillRect(this.getX() - Camera.x, this.getY() - Camera.y, 16, 16);
		//g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y, maskW, maskH);
	}

}
