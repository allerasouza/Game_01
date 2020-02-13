package com.hudeing.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Menu {
	
	public String[] options = {"Novo jogo","Carregar jogo", "Sair"};
	public int currentOption = 0;
	public int maxOption = options.length - 1;
	public boolean up, down, enter;
	public boolean pause = false;
	
	public void tick() {
		if(up) {
			currentOption--;
			if(currentOption < 0) {
				currentOption = maxOption;
			}
			up = false;
		}
		if(down) {
			currentOption++;
			if(currentOption > maxOption) {
				currentOption = 0;
			}
			down = false;
		}
		if(enter) {
			enter = false;
			if(options[currentOption] == "Novo jogo" || options[currentOption] == "Continuar") {
				Game.gameState = "NORMAL";
				pause = false;
			}
			if(options[currentOption] == "Carregar jogo") {
				System.out.println("Carregar jogo");
			}
			if(options[currentOption] == "Sair") {
				System.exit(1);		
			}
		}
	}
	
	public void render(Graphics g) {
		/*Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0, 0, 0, 100));
		g2.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);*/ 
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
		g.setColor(new Color(100, 0, 100));
		g.setFont(new Font("arial", Font.BOLD, 36));
		g.drawString("   AAS shooter 0", (Game.WIDTH * Game.SCALE)/2 - 200, (Game.HEIGHT * Game.SCALE)/2 - 160);
		
		// Opções de menu
		g.setColor(Color.WHITE);
		g.setFont(new Font("arial", Font.BOLD, 24));
		for(int i = 0; i < options.length; i++) {
			if(i == 0) {
				if(pause == false)
					g.drawString(options[i], 50, 300 + (30 * i));
				else
					g.drawString("Continuar", 50, 300 + (30 * i));
			} else
				g.drawString(options[i], 50, 300 + (30 * i));			
		}
		
		g.setColor(new Color(100, 0, 100));
		if(currentOption == 0) {
			g.drawString(">", 25, 300 + (30 * 0));
		} else if(currentOption == 1) {
			g.drawString(">", 25, 300 + (30 * 1));
		} else if(currentOption == 2) {
			g.drawString(">", 25, 300 + (30 * 2));
		}
	}
}
