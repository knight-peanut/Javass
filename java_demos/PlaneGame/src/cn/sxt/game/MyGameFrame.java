package cn.sxt.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

public class MyGameFrame extends Frame{
	//主窗口类
	
	//调用工具类中的方法来得到图像
	Image planeImg = GameUtil.getImage("images/plane.png");
	Image bg = GameUtil.getImage("images/bg.jpg");
	
	Plane plane = new Plane(planeImg,Constant.START_X,Constant.START_Y);
	
	//建立炮弹数组
	Shell[] shells = new Shell[Constant.SHELL_NUM];
	Explode bao;
	
	Date startTime = new Date();
	Date endTime;
	int peroid; //计算游戏时间
	
	//自动被调用,g相当于一支画笔
	public void paint(Graphics g) {
		Color c = g.getColor();
		
		g.drawImage(bg, 0, 0, null);
		plane.drawSelf(g);  //画出飞机的图形
		
		
		//画出数组中的每一个炮弹
		for(int i=0;i<shells.length;i++) {
			shells[i].draw(g);
			
			//检测碰撞
			boolean peng = shells[i].getRect().intersects(plane.getRect());
			
			if(peng){
				plane.live = false;
				//生成爆炸类使其导出爆炸效果
				if(bao == null) {
					bao = new Explode(plane.x, plane.y);
					
					endTime = new Date();
					peroid = (int)((endTime.getTime()-startTime.getTime())/1000);
				}
				
			    bao.draw(g);
			}
			
			if(!plane.live) {
				g.setColor(Color.RED);
				Font f = new Font("宋体", Font.BOLD, 40);
				g.setFont(f);
				g.drawString("存活时间：" + peroid + "秒" , 150, 250);
			}
			
		}
		
			g.setColor(c);
	}
	
	//不停地画飞机来达到动画效果
	class PaintThread extends Thread{
	
		public void run() {
			while(true) {
				repaint();
				
				try {
					Thread.sleep(Constant.TIME_SLEEP); //线程休眠时间，适应人眼
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	//建立一个键盘控制类
	class KeyMonito extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
			plane.addDirection(e);
		}
	
		public void keyReleased(KeyEvent e) {
			plane.minusDirection(e);
		}
	}
	
	/**
	 *初始化窗口
	 */
	public void launchFrame() {
		this.setTitle("飞机小游戏");
		this.setVisible(true);  //使主窗口可见
		this.setSize(Constant.GAME_WIDTH, Constant.GAME_HEIGTH);
		this.setLocationRelativeTo(null); //使主窗口居中
		
		//创立窗口监听使游戏的进程随着窗口的关闭而结束
		this.addWindowListener(new WindowAdapter() {
			//匿名内部类
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		new PaintThread().start();  //画飞机的线程
		addKeyListener(new KeyMonito());  //给窗口增加键盘监听
		
		//在窗口初始化炮弹数组
		for(int i=0;i<shells.length;i++) {
			shells[i] = new Shell();
		}
	}
	
	
	public static void main(String[] args) {
		MyGameFrame f = new MyGameFrame();
		f.launchFrame();
	}
	
	
	//双缓冲解决图片重画闪烁问题
	private Image offScreenImage = null;
	
	public void update(Graphics g) {
		if(offScreenImage == null)
			offScreenImage = this.createImage(Constant.GAME_WIDTH,Constant.GAME_HEIGTH);
		Graphics gOff = offScreenImage.getGraphics();
		paint(gOff);
		g.drawImage(offScreenImage, 0, 0, null);
	}
		


}
