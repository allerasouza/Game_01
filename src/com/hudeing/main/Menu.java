package com.hudeing.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.hudeing.world.World;

public class Menu {
	
	public String[] options = {"Novo jogo","Carregar jogo", "Sair"};
	public int currentOption = 0;
	public int maxOption = options.length - 1;
	public boolean up, down, enter;
	public static boolean pause = false;
	public static boolean saveExists = false;
	public static boolean saveGame = false;
	
	public void tick() {
		File file = new File("save.txt");
		if(file.exists()) {
			saveExists = true;
		} else {
			saveExists = false;
		}
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
				file = new File("save.txt");
				file.delete();
			}
			if(options[currentOption] == "Carregar jogo") {
				System.out.println("Carregar jogo");
				file = new File("save.txt");
				if(file.exists()) {
					String saver = loadGame(10);
					applySave(saver);
				}
			}
			if(options[currentOption] == "Sair") {
				System.exit(1);		
			}
		}
	}
	
	public static void applySave(String str) {
		String[] spl = str.split("/");
		for(int i = 0; i < spl.length; i++) {
			String[] spl2 = spl[i].split(":");
			switch (spl2[0]) {
			case "level": {				
				World.restartGame("level"+ spl2[1] + ".png");
				Game.gameState = "NORMAL";
				pause = false;
				break;
			}
			case "vida":
				Game.player.life = Integer.parseInt(spl2[1]);
				break;
				
			default:
				throw new IllegalArgumentException("Unexpected value: " + spl2);
			}
		}
	}
	
	public static String loadGame(int encode) {
		String line = "";
		File file = new File("save.txt");
		if(file.exists()) {
			try {
				String singleLine = null;
				BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
				try {
					while((singleLine = reader.readLine()) != null) {
						String[] trans = singleLine.split(":");
						char[] val = trans[1].toCharArray(); 
						trans[1] = "";
						for(int i = 0; i < val.length; i++) {
							val[i] -= encode;
							trans[1] += val[i];
						}
						line += trans[0];
						line += ":";
						line += trans[1];
						line += "/";
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		return line;
	}
	
	public static void saveGame(String[] val1, int[] val2, int encode) {
		BufferedWriter write = null;
		try {
			write = new BufferedWriter(new FileWriter("save.txt"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < val1.length; i++) {
			String current = val1[i];
			current += ":";
			char[] value = Integer.toString(val2[i]).toCharArray();
			for(int n = 0; n < value.length; n++) {
				value[n] += encode;
				current += value[n];
			}
			try {
				write.write(current);
				if(i < val1.length - 1) {
					write.newLine();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		try {
			write.flush();
			write.close();
		} catch (Exception e) {
			// TODO: handle exception
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
