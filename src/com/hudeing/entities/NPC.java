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
	public int curIndexMsg = 0;
	public int fraseIndex = 0;
	public int time = 0;
	public int maxTime = 5;

	public NPC(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		frases[0] = "Olá! Seja muito bem-vindo ao jogo :)";
		frases[1] = "Destrua os inimigos!";
		frases[2] = "XXXXXXXXXXXXXX2";
		frases[3] = "XXXXXXXXXXXXXXXXXXXXXXXXX3";
		frases[4] = "XXXXXX4";
		
				
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
		
		if(showMessage) {
			this.time++;
			if(this.time >= this.maxTime) {
				this.time = 0;
				if(curIndexMsg < frases[fraseIndex].length()) {
					curIndexMsg++;
				} else {
					if(fraseIndex < frases.length -1) {
						fraseIndex++;
						curIndexMsg = 0;
					}
				}
			}
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
			g.setFont(new Font("Arial", Font.BOLD, 7));
			g.setColor(Color.WHITE);
			g.drawString(frases[fraseIndex].substring(0, curIndexMsg), (int)x, (int)y);
			
			g.drawString(">Pressione Enter para Fechar<", (int)x + 10, (int)y + 13);
		}
	}

}
