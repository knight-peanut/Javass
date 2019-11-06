package cm.usc.fivechess;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameUI implements Config {
	// 保存棋子的二维数组
	private int[][] chessArray = new int[LINE+2][LINE+2]; 
	
	public void showUI() {
		
		JFrame jf = new JFrame();
		jf.setTitle("五子棋_简易版");
		jf.setSize(800, 700);
		// 设置退出进程的方法
		jf.setDefaultCloseOperation(3);
		// 设置居中显示
		jf.setLocationRelativeTo(null);
		GamePanel gp = new GamePanel(chessArray);
		//设置棋盘背景色
		gp.setBackground(new Color(139,139,122));
		jf.add(gp,BorderLayout.CENTER);
		//设置有边框的属性
		JPanel jp=new JPanel();
		jp.setBackground(new Color(245,245,245));
		jf.add(jp,BorderLayout.EAST);
		//设置选项边框流式布局的属性
		jp.setLayout(new java.awt.FlowLayout(4,4,70));
		jp.setPreferredSize(new Dimension(120,0));
		 
		//设置按钮及其属性
		//Object gameButton[];
		ArrayList<Object> gameButton = new ArrayList<Object>();
		String strButton[] = new String[4];
		strButton[0] = "人人对战";
		strButton[1] = "人机对战";
		strButton[2] = "悔棋";
		strButton[3] = "重新开始";
		
		for(int i=0;i<4;i++) {
			gameButton.add(new javax.swing.JButton(strButton[i]));
			((JComponent) gameButton.get(i)).setPreferredSize(new Dimension(100,60));
			jp.add((JComponent) gameButton.get(i));
		}
		
		jf.setVisible(true);
		//设置棋盘的画笔
		Graphics g = gp.getGraphics();
		
		//去除棋子图像边缘锯齿化
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

		GameMouse mouse = new GameMouse(g,chessArray,gp);
		
		//设置时间监听器
		for(int i=0;i<4;i++) {
			((JButton) gameButton.get(i)).addActionListener(mouse);
		}		
	}
	
	public static void main(String[] args) {
		GameUI ui = new GameUI();
		ui.showUI();
	}

}