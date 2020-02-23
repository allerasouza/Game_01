package com.hudeing.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;

import com.hudeing.main.Game;
import com.hudeing.world.Camera;
import com.hudeing.world.Node;
import com.hudeing.world.Vector2i;

public class Entity {
	
	public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(6*16, 0, 16, 16);
	public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(7*16, 0, 16, 16);
	public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(6*16, 16, 16, 16);
	public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(7*16, 16, 16, 16);
	public static BufferedImage ENEMY_EN2 = Game.spritesheet.getSprite(8*16, 16, 16, 16);
	public static BufferedImage ENEMY_FEEDBACK = Game.spritesheet.getSprite(144, 16, 16, 16);
	public static BufferedImage GUN_RIGHT = Game.spritesheet.getSprite(128, 0, 16, 16);
	public static BufferedImage GUN_LEFT = Game.spritesheet.getSprite(144, 0, 16, 16);
	public static BufferedImage GUN_DAMAGE_RIGHT = Game.spritesheet.getSprite(0, 32, 16, 16);
	public static BufferedImage GUN_DAMAGE_LEFT = Game.spritesheet.getSprite(16, 32, 16, 16);
	public static BufferedImage FLOWER_EN = Game.spritesheet.getSprite(144, 32, 16, 16);
	protected double x;
	protected double y;
	protected int z;
	protected int width;
	protected int height;
	private BufferedImage sprite;
	public int maskX, maskY, mWidth, mHeight;
	protected List<Node> path;
	public int depth;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		this.maskX = 0;
		this.maskY = 0;
		this.mWidth = width;
		this.mHeight = height;
	}
	
	public static Comparator<Entity> entitySorter = new Comparator<Entity>() {
		
		@Override
		public int compare(Entity e0, Entity e1) {
			if(e1.depth < e0.depth) {
				return +1;
			}
			if(e1.depth > e0.depth) {
				return -1;
			}
			return 0;
		}
	};
	
	public void setMask(int maskX, int maskY, int mWidht, int mHeight) {
		this.maskX = maskX;
		this.maskY = maskY;
		this.mWidth = mWidht;
		this.mHeight = mHeight;
	}

	public int getX() {
		return (int)x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return (int)y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public void tick() {
		
	}
	
	public double calculateDistance(int x1, int y1, int x2, int y2) {
		return Math.sqrt(((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2)));
	}
	
	public boolean isColliding(int xNext, int yNext) {
		//Rectangle currentEnemy = new Rectangle(xNext, yNext, World.TILE_SIZE, World.TILE_SIZE);
		Rectangle currentEnemy = new Rectangle(xNext + maskX, yNext + maskY, mWidth, mHeight);
		for(int i = 0; i < Game.enemies.size(); i++) {
			Enemy en = Game.enemies.get(i);
			if(en == this)
				continue;
			
			//Rectangle targetEnemy = new Rectangle(en.getX(), en.getY(), World.TILE_SIZE, World.TILE_SIZE);
			Rectangle targetEnemy = new Rectangle(en.getX() + maskX, en.getY() + maskY, mWidth, mHeight);
			if(currentEnemy.intersects(targetEnemy))
				return true;
		}
		return false;
	}
	
	public void followPath(List<Node> path) {
		if(path != null) {
			if(path.size() > 0) {
				Vector2i target = path.get(path.size() -1).tile;
				//double xprev = x;
				//double yprev = y;
				if(x < target.x * 16) {
					x++;
				} else if(x > target.x * 16) {
					x--;
				}
				if(y < target.y * 16) {
					y++;
				} else if(y > target.y * 16) {
					y--;
				}
				
				if(x == target.x * 16 && y == target.y * 16) {
					path.remove(path.size() -1);
				}
			}
		}
	}
	
	public static boolean isColliding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskX, e1.getY() + e1.maskY, e1.mWidth, e1.mHeight);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskX, e2.getY() + e2.maskY, e2.mWidth, e2.mHeight);
		if(e1Mask.intersects(e2Mask) && e1.z == e2.z) {
			return true;
		}
		return false;
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
		// g.setColor(Color.RED);
		// g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y, mWidth, mHeight);
	}
	
}
