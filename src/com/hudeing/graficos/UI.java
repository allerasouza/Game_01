package com.hudeing.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.hudeing.entities.Player;
//import com.hudeing.main.Game;
//import com.hudeing.world.Camera;

public class UI {
	
	public void render(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(8, 4, 70, 8); //g.fillRect(Game.player.getX()- Camera.x, Game.player.getY() - Camera.y -7, 16, 3);
		g.setColor(Color.GREEN);
		g.fillRect(8, 4, (int)((Player.life/Player.maxLife)*70), 8); //g.fillRect(Game.player.getX()- Camera.x, Game.player.getY() - Camera.y -7, (int) ((double)(Player.life/(double)Player.maxLife)*16), 3);
		g.setColor(Color.WHITE);
		g.setFont(new Font("arial", Font.BOLD, 8));
		if((int)Player.life < 0)
			g.drawString("0/" + (int)Player.maxLife, 8, 11);
		else
			g.drawString((int)Player.life + "/" + (int)Player.maxLife, 30, 11);
	}

}
