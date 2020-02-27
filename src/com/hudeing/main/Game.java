package com.hudeing.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.hudeing.entities.BulletShoot;
import com.hudeing.entities.Enemy;
import com.hudeing.entities.Entity;
import com.hudeing.entities.Player;
import com.hudeing.graficos.Spritesheet;
import com.hudeing.graficos.UI;
import com.hudeing.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = false;
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;
	private int CUR_LEVEL = 1, MAX_LEVEL = 2;
	private BufferedImage image;
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<BulletShoot> bullets;
	public static Spritesheet spritesheet;
	public static World world;
	public static Player player;
	public static Random rand;
	public UI ui;
	//public int xx, yy;
	//public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("pixelfont.ttf");
	//public Font newFont;
	public static String gameState = "MENU"; // MENU, NORMAL, GAME_OVER
	private boolean showMessageGameOver = true;
	private int framesGameOver = 0;
	private boolean restartGame = false;
	public Menu menu;
	public static BufferedImage lightMap;
	public static BufferedImage miniMap;
	public static int[] pixels;	
	public static int[] lightMapPixels;
	public static int[] miniMapPixels;
	public boolean saveGame = false;
	public int mX, mY;
	// Cutscene system
	public static int entrada = 1;
	public static int comecar = 2;
	public static int jogando = 3;
	public static int estadoCena = entrada;
	public int timeCena = 0, maxTimeCena = 60 * 3;
		
	public Game() {
		Sound.musicBackground.loop();
		rand = new Random();
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		this.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE)); // Fixed window size
		//this.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize())); // Fullscreen
		initFrame();
		//Inicializando objetos.
		ui = new UI();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		try {
			lightMap = ImageIO.read(getClass().getResource("/lightmap.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		lightMapPixels = new int[lightMap.getWidth() * lightMap.getHeight()];
		lightMap.getRGB(0, 0, lightMap.getWidth(), lightMap.getHeight(), lightMapPixels, 0, lightMap.getWidth());
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		bullets = new ArrayList<BulletShoot>();
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
		world = new World("/level1.png");
		miniMap = new BufferedImage(World.WIDTH, World.HEIGHT, BufferedImage.TYPE_INT_RGB);
		miniMapPixels = ((DataBufferInt)miniMap.getRaster().getDataBuffer()).getData();
		/*try {
			newFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(70f);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		menu = new Menu();
	}
	
	public void initFrame() {
		frame = new JFrame("Game #1");
		frame.add(this);
		//frame.setUndecorated(false); //Remove barra de cima, com icone de minimizar, maximizar e fechar
		frame.setResizable(false);
		frame.pack();
		
		// Icone da janela e do cursor
		Image imagem = null;
		try {
			imagem = ImageIO.read(getClass().getResource("/icon.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = toolkit.getImage(getClass().getResource("/icon_32.png"));
		Cursor c = toolkit.createCustomCursor(image, new Point(0, 0), "img");
		frame.setCursor(c);
		frame.setIconImage(image);
		frame.setAlwaysOnTop(true);
		
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		//thread.stop();
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.start();

	}
	
	public void tick() {
		if(gameState == "NORMAL") {
			/*xx++;
			yy++;*/
			if(this.saveGame) {
				this.saveGame = false;
				String[] opt1 = {"level", "vida"};
				int[] opt2 = {this.CUR_LEVEL, (int) player.life};
				Menu.saveGame(opt1, opt2, 10);
				System.out.println("Jogo salvo");
			}
			this.restartGame = false;
			if(Game.estadoCena == Game.jogando) {
				for(int i = 0; i < entities.size(); i++) {
					Entity e = entities.get(i);
					//if(e instanceof Player) { System.out.println("Estou dando tick no player!");}
					e.tick();
				}
				
				for(int i = 0; i < bullets.size(); i++) {
					bullets.get(i).tick();
				}
			} else {
				if(Game.estadoCena == Game.entrada) {
					//player.dir = player.right_dir;
					if(player.getX() < 400) {
						player.setX(player.getX() + 1);
						player.updateCamera();
					} else {
						//player.dir = player.left_dir;
						System.out.println("Game entrada concluido!");
						Game.estadoCena = Game.comecar;
					}
				} else if(Game.estadoCena == Game.comecar) {
					timeCena++;
					if(timeCena == maxTimeCena) {
						Game.estadoCena = Game.jogando;
					}
				}
			}
			
			if(enemies.size() == 0) {
				//Avançar para o próximo level
				CUR_LEVEL++;
				if(CUR_LEVEL > MAX_LEVEL) {
					CUR_LEVEL = 1;
				}
				String newWorld = "level" + CUR_LEVEL + ".png";
				World.restartGame(newWorld);
			}
		} else if(gameState == "GAME_OVER") {
			this.framesGameOver++;
			if(this.framesGameOver == 30) {
				this.framesGameOver = 0;
				if(this.showMessageGameOver)
					this.showMessageGameOver = false;
				else
					this.showMessageGameOver = true;
			}
			
			if(restartGame) {
				this.restartGame = false;
				gameState = "NORMAL";
				CUR_LEVEL = 1;
				String newWorld = "level" + CUR_LEVEL + ".png";
				World.restartGame(newWorld);
			}
		} else if(gameState == "MENU") {
			player.updateCamera();
			menu.tick();
		}
	}
	
	/*public void drawRectangleExample(int xxOff, int yyOff) {
		for(int xx = 0; xx < 32; xx++) {
			for(int yy = 0; yy < 32; yy++) {
				int xOff = xx + xxOff;
				int yOff = yy + yyOff;
				if(xOff < 0 || yOff < 0 || xOff >= WIDTH || yOff >= HEIGHT) {
					continue;
				}
				pixels[xOff + (yOff * WIDTH)] = 0xff0000;
			}
		}
	}*/
	
	public void applyLight() {
		for(int xx = 0; xx < Game.WIDTH; xx++) {
			for(int yy = 0; yy < Game.HEIGHT; yy++) {
				if(lightMapPixels[xx + (yy * Game.WIDTH)] == 0xffffffff) {
					int pixel = Pixel.getLightBlend(pixels[xx + (yy * Game.WIDTH)], 0x808080, 0);
					pixels[xx + (yy * Game.WIDTH)] = pixel;
				}
			}
		}
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, WIDTH, HEIGHT);		
		
		/*Renderização do jogo*/
		//Graphics2D g2 = (Graphics2D) g;
		world.render(g);
		Collections.sort(entities, Entity.entitySorter);
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(g);
		}
		applyLight();
		ui.render(g);
		/***/
		g.dispose();
		g = bs.getDrawGraphics();
		//drawRectangleExample(xx, yy);
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null); // Fixed window size
		//g.drawImage(image, 0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height, null); // Fullscreen
		g.setFont(new Font("arial", Font.BOLD, 15));
		g.setColor(Color.WHITE);
		g.drawString("Munição: " + player.ammo, 600, 30);
		if(gameState == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0, 0, 0, 100));
			g2.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
			g.setFont(new Font("arial", Font.BOLD, 36));
			g.setColor(Color.WHITE);
			g.drawString("Game Over!",WIDTH * SCALE / 2 - 80, HEIGHT * SCALE / 2 -20);
			g.setFont(new Font("arial", Font.BOLD, 30));
			if(showMessageGameOver)
				g.drawString(">Pressione ENTER para reiniciar<",WIDTH * SCALE / 2 - 250, HEIGHT * SCALE / 2 + 40);
		} else if(gameState == "MENU") {
			menu.render(g);
		}
		
		// Cutscene system
		if(Game.estadoCena == Game.comecar) {
			g.drawString("Se prepare... O jogo vai começar!",player.getX() - 50 , player.getY());
		}
		
		// Rotacionando objetos
		/*Graphics2D g2 = (Graphics2D) g;
		double angleMouse = Math.atan2(200 + 25 - mY, 200 + 25 - mX);
		g2.rotate(angleMouse, 200 + 25, 200 + 25);
		System.out.println(Math.toDegrees(angleMouse));
		g.setColor(Color.RED);
		g.fillRect(200, 200, 50, 50);*/
		
		// Setando fonte customizada
		/*g.setFont(newFont);
		g.setColor(Color.RED);
		g.drawString("Teste com a nova fonte", 90, 90);*/
		
		// Minimap
		World.renderMiniMap();
		g.drawImage(miniMap, 560, 40, World.WIDTH * 5, World.HEIGHT * 5, null);
		
		bs.show();
	}

	@Override
	public void run() {
		requestFocus(); //Focar automaticamente na janela
		long lastTime = System.nanoTime();
		double amountofTicks = 60.0;
		double ns = 1000000000 / amountofTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				tick();
				render();
				frames++;
				//delta = 0;
				delta--;
			}
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames);				
				frames = 0;
				timer+=1000;
			}
			//System.out.println("Meu jogo está rodando!");
		}
		stop();
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
				
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			// System.out.println("Direita");
			player.right = true;
		} else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			//System.out.println("Esquerda");
			player.left = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			//System.out.println("Cima");
			player.up = true;
			if(gameState == "MENU") {
				menu.up = true;
			}
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			//System.out.println("Baixo");
			player.down = true;
			if(gameState == "MENU") {
				menu.down = true;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			//System.out.println("Espaço");
			player.shoot = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			//System.out.println("Enter");
			if(gameState == "GAME_OVER") {
				this.restartGame = true;
			}
			if(gameState == "MENU") {
				menu.enter = true;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			//System.out.println("Escape");
			gameState = "MENU";
			menu.pause = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_Z) {
			//System.out.println("Z);
			player.jump = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_M) {
			if(gameState == "NORMAL") {
				this.saveGame = true;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			// System.out.println("Direita");
			player.right = false;
		} else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			//System.out.println("Esquerda");
			player.left = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			//System.out.println("Cima");
			player.up = false;
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			//System.out.println("Baixo");
			player.down = false;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		player.mouseShoot = true;
		player.mX = (e.getX() / this.SCALE);
		player.mY = (e.getY() / this.SCALE);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		this.mX = e.getX();
		this.mY = e.getY();
		
	}
}
