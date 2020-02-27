package com.hudeing.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.hudeing.main.Game;

public class NPC extends Entity{
	
	public String[] frases = new String[5];
	public boolean showMessage = false;
	public boolean show = false;

	public NPC(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		frases[0] = "Olá!";
		frases[1] = "X1";
		frases[2] = "X2";
		frases[3] = "X3";
		frases[4] = "X4";
		
				
	}
	
	public void tick() {
		int xPlayer = Game.player.getX();
		int yPlayer = Game.player.getY();
		int xNpc = (int)x;
		int yNpc = (int)y;
		
		//if( Math.sqrt( (Math.abs(xPlayer - xNpc) * Math.abs(xPlayer - xNpc)) + (Math.abs(yPlayer - yNpc) * Math.abs(yPlayer - yNpc)))  < 30) {
		if(	Math.abs(xPlayer - xNpc) < 20 &&
				Math.abs(yPlayer - yNpc) < 20 ) {
			if(!show) {
				showMessage = true;
				show = true;
			}
		} else {
			//showMessage = false;
			show = false;
		}
	}
	
	public void render(Graphics g) {
		depth = Game.player.depth + 100;
		super.render(g);
		if(showMessage) {
			g.setColor(Color.WHITE);
			g.fillRect(9, 9, Game.WIDTH - 18, Game.HEIGHT - 18);
			g.setColor(Color.BLUE);
			g.fillRect(10, 10, Game.WIDTH - 20, Game.HEIGHT - 20);
			g.setFont(new Font("Arial", Font.BOLD, 9));
			g.setColor(Color.WHITE);
			g.drawString(frases[0], (int)x, (int)y);
			
			g.drawString(">Pressione Enter para Fechar<", (int)x + 10, (int)y + 13);
		}
	}

}
