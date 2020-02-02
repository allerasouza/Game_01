package com.hudeing.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.hudeing.main.Game;
import com.hudeing.world.Camera;
import com.hudeing.world.World;

public class Player extends Entity{
	
	public boolean right, up, left, down;
	public int right_dir = 0, left_dir = 1;
	public int dir = right_dir;
	public double speed = 1.4;
	
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		
		for(int i = 0; i < rightPlayer.length; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32 +(16*i), 0, 16, 16);
		}
		for(int i = 0; i < leftPlayer.length; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(32 +(16*i), 16, 16, 16);
		}
		
		
	}
	
	public void tick() {
		moved = false;
		if(right) {
			moved = true;
			x += speed; //this.setX(getX() + speed);
			dir = right_dir;
		} else if(left) {
			moved = true;
			x -= speed; //this.setX(getX() - speed);
			dir = left_dir;
		}
		
		if(up) {
			moved = true;
			y-=speed; //this.setY(getY() + speed);
		} else if(down) {
			moved = true;
			y+=speed; //this.setY(getY() - speed);
		}
		
		if(moved) {
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex) {
					index = 0;
				}
			}
		}
		
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT*16 - Game.HEIGHT);
	}

	public void render(Graphics g) {
		if(dir == right_dir) {
			g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else if(dir == left_dir) {
			g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
	}
}
