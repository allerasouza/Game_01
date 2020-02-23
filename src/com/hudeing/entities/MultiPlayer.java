package com.hudeing.entities;

import java.awt.image.BufferedImage;

import com.hudeing.main.Game;

public class MultiPlayer extends Player{
	
	public MultiPlayer(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		if(this.getY() > Game.player.getY())
			this.depth = Game.player.depth + 1;
		else
			this.depth = Game.player.depth - 1;
	}

}
