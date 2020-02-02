package com.hudeing.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.hudeing.entities.*;
import com.hudeing.main.Game;

public class World {
	
	private Tile[] tiles;
	public static int WIDTH, HEIGHT;
	
	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			for(int xx = 0; xx < map.getWidth(); xx++) {
				for(int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy*map.getWidth())];
					tiles[xx + (yy*WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
					if(pixelAtual == 0xFF000000) {
						// Floor
						tiles[xx + (yy*WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
					} else if(pixelAtual == 0xFFFFFFFF) {
						// Parede
						tiles[xx + (yy*WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_WALL);
					} else if(pixelAtual == 0xFF0026FF) {
						// Player
						Game.player.setX(xx*16);
						Game.player.setY(yy*16);
					} else if(pixelAtual == 0xFFFF0000) {
						// Enemy
						Game.entities.add(new Enemy(xx*16, yy*16, 16, 16, Enemy.ENEMY_EN));
					} else if(pixelAtual == 0xFFFF6A00) {
						// WEAPON
						Game.entities.add(new Weapon(xx*16, yy*16, 16, 16, Weapon.WEAPON_EN));
					} else if(pixelAtual == 0xFFFF7F7F) {
						// LIFEPACK
						Game.entities.add(new Lifepack(xx*16, yy*16, 16, 16, Lifepack.LIFEPACK_EN));
					} else if(pixelAtual == 0xFFFFD800) {
						// BULLET
						Game.entities.add(new Bullet(xx*16, yy*16, 16, 16, Bullet.BULLET_EN));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void render(Graphics g) {
		int xStart = Camera.x >> 4; // Camera.x/16;
		int yStart = Camera.y >> 4; // Camera.y/16;
		
		int xFinal = xStart + (Game.WIDTH/16);
		int yFinal = yStart + (Game.HEIGHT/16);
		
		for(int xx = xStart; xx <= xFinal; xx++) {
			for(int yy = yStart; yy <= yFinal; yy++) {
				if(xx < 0 || yy <0 || xx >= WIDTH || yy >= HEIGHT)
					continue;
				Tile tile = tiles[xx + (yy*WIDTH)];
				tile.render(g);
			}
		}
	}
}
