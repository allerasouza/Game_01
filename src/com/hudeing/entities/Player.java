package com.hudeing.entities;

import java.awt.image.BufferedImage;

public class Player extends Entity{
	
	public boolean right, up, left, down;
	public double speed = 0.7;

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}
	
	public void tick() {
		if(right) {
			x+=speed; //this.setX(getX() + speed);
		} else if(left) {
			x-=speed; //this.setX(getX() - speed);
		}
		
		if(up) {
			y-=speed; //this.setY(getY() + speed);
		} else if(down) {
			y+=speed; //this.setY(getY() - speed);
		}
	}

}
