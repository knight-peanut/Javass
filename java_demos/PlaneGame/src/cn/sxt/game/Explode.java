package cn.sxt.game;

import java.awt.Graphics;
import java.awt.Image;

public class Explode {

	//爆炸类，主要是实现爆炸时的图片连载
	double x,y;
	
	//静态定义了16张图片，不用每张图片都再次new()，减少资源的消耗
	static Image[] imgs = new Image[16];
	
	static {
		for(int i=0;i<16;i++) {
			imgs[i] = GameUtil.getImage("images/explode/e" +(i + 1)+ ".gif");
			imgs[i].getWidth(null);
		}
	}
	
	//通过画笔g来画出每张图像，依次画出实现连播效果
	int count=0;
	public void draw(Graphics g) {
		if(count<=15) {
			g.drawImage(imgs[count], (int)x, (int)y, null);
			count ++;
		}
	}
	
	//爆炸位置坐标
	public Explode(double x,double y) {
		this.x = x;
		this.y = y;
	}
	
}
