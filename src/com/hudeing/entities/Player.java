package com.hudeing.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.hudeing.graficos.Spritesheet;
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
	private BufferedImage playerDamage;
	private boolean hasGun = false;
	public int ammo = 0;
	public boolean isDamaged = false;
	private int damageFrames = 0;
	public boolean shoot = false;
	public boolean mouseShoot = false;
	public double life = 100, maxLife = 100;
	public int mX, mY;
	public boolean jump = false;
	public boolean isJumping = false;
	public int z = 0;
	public int jumpFrames = 12, jumpCur = 0;
	public boolean jumpUp = false, jumpDown = false;
	public int jumpSpeed = 1;

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		playerDamage = Game.spritesheet.getSprite(0, 16, 16, 16);
		for(int i = 0; i < rightPlayer.length; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32 +(16*i), 0, 16, 16);
		}
		for(int i = 0; i < leftPlayer.length; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(32 +(16*i), 16, 16, 16);
		}
		
		
	}
	
	public void tick() {
		if(jump) {
			if(!isJumping) {
				jump = false;
				isJumping = true;
				jumpUp = true;
			}
		}
		
		if(isJumping) {
			if(jumpUp) {
				jumpCur+=jumpSpeed;
			} else if(jumpDown) {
				jumpCur-=jumpSpeed;
				if(jumpCur <= 0 ) {
					isJumping = false;
					jumpDown = false;
					jumpUp = false;
				}
			}
			z = jumpCur;
			if(jumpCur >= jumpFrames) {
				jumpUp = false;
				jumpDown = true;
			}
		}
		
		moved = false;
		if(right && World.isFree((int)(x+speed), this.getY(), this)) {
			moved = true;
			x += speed; //this.setX(getX() + speed);
			dir = right_dir;
		} else if(left && World.isFree((int)(x-speed), this.getY(), this)) {
			moved = true;
			x -= speed; //this.setX(getX() - speed);
			dir = left_dir;
		}
		
		if(up && World.isFree(this.getX(), (int)(y - speed), this)) {
			moved = true;
			y-=speed; //this.setY(getY() + speed);
		} else if(down && World.isFree(this.getX(), (int)(y+speed), this)) {
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
		
		checkCollisionLifePack();
		checkCollisionAmmo();
		checkCollisionGun();
		
		if(isDamaged) {
			this.damageFrames++;
			if(this.damageFrames == 8) {
				this.damageFrames = 0;
				isDamaged = false;
			}
		}
		
		if(shoot) {
			shoot = false;
			if(hasGun & ammo > 0) {
				ammo--;
				//System.out.println("Atirando!");
				int dX = 0;
				int pX = 0;
				int pY = this.getHeight()/2;
				if(dir == right_dir) {
					pX = 19;
					dX = 1;				
				} else if(dir == left_dir) {
					pX = -7;
					dX = -1;
				}
				
				BulletShoot bullet = new BulletShoot(this.getX() + pX, this.getY() + pY, 3, 3, null, dX, 0);
				Game.bullets.add(bullet);
			}
		}
		
		if(mouseShoot) {
			mouseShoot = false;
			
			
			if(hasGun & ammo > 0) {
				ammo--;
				//System.out.println("Atirando!");				
				
				int pX = 0;
				int pY = this.getWidth()/2;
				double angle = 0; // System.out.println("Angulo: " + Math.toDegrees(angle));
				if(dir == right_dir) {
					pX = 19;
									
				} else if(dir == left_dir) {
					pX = -7;
				}
				angle = Math.atan2(mY - (this.getY() + pY - Camera.y), mX - (this.getX() + pX - Camera.x));
				
				double dX = Math.cos(angle);
				double dY = Math.sin(angle);
				
				BulletShoot bullet = new BulletShoot(this.getX() + pX, this.getY() + pY, 3, 3, null, dX, dY);
				Game.bullets.add(bullet);
			}
		}
				
		if(life <= 0) {
			// Game Over
			life = 0;
			Game.gameState = "GAME_OVER";
			
		}
		updateCamera();
	}
	
	public void updateCamera() {
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT*16 - Game.HEIGHT);
	}
	
	public void checkCollisionGun() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Weapon) {
				if(Entity.isColliding(this, atual)) {
					hasGun = true;
					//System.out.println("Pegou arma");
					Game.entities.remove(atual);
					return;
				}
			}
		}
	}
	
	public void checkCollisionAmmo() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Bullet) {
				if(Entity.isColliding(this, atual)) {
					ammo+=30;
					//System.out.println("Munição atual: " + ammo);
					Game.entities.remove(atual);
					return;
				}
			}
		}
	}
	
	public void checkCollisionLifePack() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Lifepack) {
				if(Entity.isColliding(this, atual)) {
					life += 10;
					if(life >= 100)
						life = 100;
					Game.entities.remove(i);
					return;
				}
			}
		}
	}

	public void render(Graphics g) {
		if(!isDamaged) {
			if(dir == right_dir) {
				g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
				if(hasGun) {
					// Desenhar arma para direita
					g.drawImage(Entity.GUN_RIGHT, this.getX() - Camera.x + (this.width/2), this.getY() - Camera.y - z, null);
				}
			} else if(dir == left_dir) {
				g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y - z, null);
				if(hasGun) {
					// Desenhar arma para esquerda
					g.drawImage(Entity.GUN_LEFT, this.getX() - Camera.x - (this.width/2), this.getY() - Camera.y - z, null);
				}
			}
		} else {
			g.drawImage(playerDamage, this.getX() - Camera.x, this.getY() - Camera.y - z, null);
			if(hasGun) {
				if(dir == left_dir) {
					g.drawImage(Entity.GUN_DAMAGE_LEFT, this.getX() - Camera.x - (this.width/2), this.getY() - Camera.y - z, null);
				} else {
					g.drawImage(Entity.GUN_DAMAGE_RIGHT, this.getX() - Camera.x + (this.width/2), this.getY() - Camera.y - z, null);
				}
					
			}
		}
		
		if(isJumping) {
			g.setColor(Color.BLACK);
			g.fillOval(this.getX() - Camera.x + 2 , this.getY() - Camera.y + this.height - 5, 10, 5);
		}
	}
}
