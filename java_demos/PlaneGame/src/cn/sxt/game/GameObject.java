package cn.sxt.game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public class GameObject {
	//定义游戏物体类
	//类属性
	Image img;
	double x,y;
	int speed;
	int width,height;
	
	//用画笔画出物体
	public void drawSelf(Graphics g) {
		g.drawImage(img, (int)x, (int)y, null);
	}

	public GameObject(Image img, double x, double y, int speed, int width, int height) {
		super();
		this.img = img;
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.width = width;
		this.height = height;
	}

	public GameObject(Image img, double x, double y) {
		super();
		this.img = img;
		this.x = x;
		this.y = y;
	}

	//GameObject作为父类，保证其无参构造器的存在
	public GameObject() {
		
	}
	
	
    //返回物体所在的矩形方便碰撞检测
	public Rectangle getRect(){
		return new Rectangle((int)x,(int)y,width,height);
	}
}
