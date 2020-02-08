package com.hudeing.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.hudeing.main.Game;
import com.hudeing.world.Camera;

public class Entity {
	
	public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(6*16, 0, 16, 16);
	public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(7*16, 0, 16, 16);
	public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(6*16, 16, 16, 16);
	public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(7*16, 16, 16, 16);
	//public static BufferedImage ENEMY_EN2 = Game.spritesheet.getSprite(8*16, 16, 16, 16);
	public static BufferedImage GUN_RIGHT = Game.spritesheet.getSprite(128, 0, 16, 16);
	public static BufferedImage GUN_LEFT = Game.spritesheet.getSprite(144, 0, 16, 16);
	protected double x;
	protected double y;
	protected int width;
	protected int height;
	private BufferedImage sprite;
	private int maskX, maskY, mWidth, mHeight;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		this.maskX = 0;
		this.maskY = 0;
		this.mWidth = width;
		this.mHeight = height;
	}
	
	public void setMask(int maskX, int maskY, int mWidht, int mHeight) {
		this.maskX = maskX;
		this.maskY = maskY;
		this.mWidth = mWidht;
		this.mHeight = mHeight;
	}

	public int getX() {
		return (int)x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return (int)y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public void tick() {
		
	}
	
	public static boolean isColliding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskX, e1.getY() + e1.maskY, e1.mWidth, e1.mHeight);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskX, e2.getY() + e2.maskY, e2.mWidth, e2.mHeight);
		return e1Mask.intersects(e2Mask);
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
		// g.setColor(Color.RED);
		// g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y, mWidth, mHeight);
	}
	
}
