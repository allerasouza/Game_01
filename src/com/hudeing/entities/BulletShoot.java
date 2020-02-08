package com.hudeing.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.hudeing.main.Game;
import com.hudeing.world.Camera;

public class BulletShoot extends Entity{
	
	private double dX;
	private double dY;
	private double spd = 4;
	private int life = 30, curLife = 0;

	public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, double dX, double dY) {
		super(x, y, width, height, sprite);
		this.dX = dX;
		this.dY = dY;
	}
	
	public void tick() {
		x += dX*spd;
		y += dY*spd;
		curLife++;
		if(curLife == life) {
			Game.bullets.remove(this);
			return;
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, this.width, this.height);
	}

}
