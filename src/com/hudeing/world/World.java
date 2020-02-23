package com.hudeing.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.hudeing.entities.*;
import com.hudeing.graficos.Spritesheet;
import com.hudeing.main.Game;

public class World {
	
	public static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static final int TILE_SIZE = 16;
	
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
						tiles[xx + (yy*WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL);
					} else if(pixelAtual == 0xFF0026FF) {
						// Player
						Game.player.setX(xx*16);
						Game.player.setY(yy*16);
					} else if(pixelAtual == 0xFFFF0000) {
						// Enemy
						Enemy en = new Enemy(xx*16, yy*16, 16, 16, Enemy.ENEMY_EN);
						Game.entities.add(en);
						Game.enemies.add(en);
					} else if(pixelAtual == 0xFFFF6A00) {
						// WEAPON
						Game.entities.add(new Weapon(xx*16, yy*16, 16, 16, Weapon.WEAPON_EN));
					} else if(pixelAtual == 0xFFFF7F7F) {
						// LIFEPACK
						Lifepack pack = new Lifepack(xx*16, yy*16, 16, 16, Lifepack.LIFEPACK_EN);
						pack.setMask(8, 8, 8, 8);
						Game.entities.add(pack);
					} else if(pixelAtual == 0xFFFFD800) {
						// BULLET
						Game.entities.add(new Bullet(xx*16, yy*16, 16, 16, Bullet.BULLET_EN));
					} else if(pixelAtual == 0xFF4CFF00) {
						// Flower
						/*Flower flower = new Flower(xx*16, yy*16, 16, 16, Entity.FLOWER_EN);
						Game.entities.add(flower);*/
						tiles[xx + (yy*WIDTH)] = new FloorTile(xx*16, yy*16, Entity.FLOWER_EN);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isFree(int xNext, int yNext, Entity e) {
		// Ponto superior esquerdo
		int x1 = (xNext + 1) / TILE_SIZE;
		int y1 = (yNext + 1) / TILE_SIZE;

		// Ponto superior direito
		int x2 = (xNext + TILE_SIZE -1) / TILE_SIZE;
		int y2 = (yNext + 1) / TILE_SIZE;
		
		// Ponto inferior esquerdo
		int x3 = (xNext + 1) / TILE_SIZE;
		int y3 = (yNext + TILE_SIZE -1) / TILE_SIZE;
		
		// Ponto inferior direito
		int x4 = (xNext + TILE_SIZE -1) / TILE_SIZE;
		int y4 = (yNext + TILE_SIZE -1) / TILE_SIZE;
		
		if(!(	(tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile) ||
					(tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile) ||
					(tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile) ||
					(tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile))) {
			return true;
		}
		
		if((Game.player.z > 0) && e instanceof Player) {
			return true;
		}
		return false;
		
	}
	
	public static void restartGame(String level) {
		Game.entities.clear();
		Game.enemies.clear();
		Game.bullets.clear();
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(32, 0, 16, 16));
		Game.entities.add(Game.player);
		Game.world = new World("/" + level);
		Camera.x = 0;
		Camera.y = 0;
		return;
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
