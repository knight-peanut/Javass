package cm.usc.fivechess;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameMouse extends MouseAdapter implements Config, ActionListener {
	private JPanel j;
	private Graphics g;
	private int x1, y1;
	private int xx, yy; // 当前棋子的交点位置
	private int chessColor = 0; // 控制棋子的颜色
	private int[][] chessArray = new int[LINE + 2][LINE + 2]; // 保存棋子的二维数组
	AI startAI ; // 设置AI下棋
	public int chooseModule; // 对设置的按钮进行功能选择

	/**
	 * 定义构造方法，用来初始化对象
	 * @param g 画笔
	 * @param chessArray 棋子数组
	 * @param j 画板
	 */
	public GameMouse(Graphics g, int[][] chessArray, JPanel j) {
		this.chessArray = chessArray;
		this.j = j;
		this.g = g;
	}
	
	// 获胜后的界面
	public void winFrame(int winFlag) {
		JDialog frame = new JDialog();//构造一个新的JFrame，作为新窗口。
        frame.setBounds(// 设置对话边框
                new Rectangle(400,350,500,200)
            );
        JLabel jl = new JLabel(); // 注意类名别写错了。
        frame.getContentPane().add(jl);
        
        String text = ""; //初始化
        
        if(winFlag==1) {
        	//设置对话窗口文字内容
             text = "<html>" + 
            		"<body>" + 
            		"<p align=\"center\">" + 
            		"白棋获胜" + 
            		"<br>" + 
            		"关闭对话即可继续操作！" + 
            		"</p>" + 
            		"</body>" + 
            		"</html>";
        }else if(winFlag==-1){
        	text = "<html>" + 
            		"<body>" + 
            		"<p align=\"center\">" + 
            		"黑棋获胜" + 
            		"<br>" + 
            		"关闭对话即可继续操作！" + 
            		"</p>" + 
            		"</body>" + 
            		"</html>";
		}else {
			text = "<html>" + 
            		"<body>" + 
            		"<p align=\"center\">" + 
            		"人人对战不可悔棋" + 
            		"<br>" + 
            		"关闭对话即可继续操作！" + 
            		"</p>" + 
            		"</body>" + 
            		"</html>";
		}
        
        jl.setText(text);
        //设置字体样式：字体类型，字体加粗，字体大小
        jl.setFont(new java.awt.Font("Dialog", 1, 20));
        jl.setVerticalAlignment(JLabel.CENTER);
        jl.setHorizontalAlignment(JLabel.CENTER);
        //设置模式类型
        frame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        //参数 APPLICATION_MODAL：阻塞同一 Java 应用程序中的所有顶层窗口，包括它自己的子层次
        frame.setVisible(true);
	}

	// 判断是否赢棋
	public boolean CheckWin(int xIndex, int yIndex) {
        int max = 0;
        int tempXIndex = xIndex;
        int tempYIndex = yIndex;
        
        int countConect;
        boolean flag;
        // 三维数组记录横向，纵向，左斜，右斜的移动
        int[][][] dir = new int[][][] {
                // 横向
                { { -1, 0 }, { 1, 0 } },
                // 竖着
                { { 0, -1 }, { 0, 1 } },
                // 左斜
                { { -1, -1 }, { 1, 1 } },
                // 右斜
                { { 1, -1 }, { -1, 1 } } };
 
        // 两层循环遍历了落子点以2为半径的周围棋子
        for (int i = 0; i < 4; i++) {
            countConect = 1;
            for (int j = 0; j < 2; j++) {
                flag = true;
                // while循环会一直往一个方向遍历
                while (flag) {
                    tempXIndex = tempXIndex + dir[i][j][0];
                    tempYIndex = tempYIndex + dir[i][j][1];
 
                    // 判断一个方向上的三颗棋子颜色
                    if (tempXIndex >= 0 && tempXIndex <= 15 && tempYIndex >= 0 && tempYIndex <= 15) {
                        if ((chessArray[tempXIndex][tempYIndex] == chessArray[xIndex][yIndex])) 
                            countConect++;
                        else
                            flag = false;
                    }else{
                        flag = false;
                    }
                    
                }
                tempXIndex = xIndex;
                tempYIndex = yIndex;
            }
            // 当满足五子相连条件
            if (countConect >= 5) {
                max = 1;
                break;
            } else
                max = 0;
        }
        if (max == 1)
            return true;
        else
            return false;
    }
	
	public void caculate_p2p() {
		// 计算棋子交点
		if ((x1 - X) % SIZE > SIZE / 2) {
			xx = (x1 - X) / SIZE + 1;
		} else {
			xx = (x1 - X) / SIZE;
		}
		if ((y1 - Y) % SIZE > SIZE / 2) {
			yy = (y1 - Y) / SIZE + 1;
		} else {
			yy = (y1 - Y) / SIZE;
		}

		// 控制棋子的黑白色交替出现
		if (chessArray[xx][yy] == 0) {
			if (chessColor == 1) {
				g.setColor(Color.WHITE);
				chessArray[xx][yy] = 1;
				chessColor--;
			} else {
				g.setColor(Color.BLACK);
				chessArray[xx][yy] = -1;
				chessColor++;
			}
			
			g.fillOval(xx * SIZE + X - CHESS / 2, yy * SIZE + Y - CHESS / 2,CHESS, CHESS);
			//判断胜负
			if(CheckWin(xx, yy)){
				winFrame(chessArray[xx][yy]);
			}
		}
	}

	public void caculate_p2c() {
		// 计算棋子交点
		if ((x1 - X) % SIZE > SIZE / 2) {
			xx = (x1 - X) / SIZE + 1;
		} else {
			xx = (x1 - X) / SIZE;
		}
		if ((y1 - Y) % SIZE > SIZE / 2) {
			yy = (y1 - Y) / SIZE + 1;
		} else {
			yy = (y1 - Y) / SIZE;
		}
		
		if (chessArray[xx][yy] == 0) {
			g.setColor(Color.BLACK);
			chessArray[xx][yy] = -1;
			g.fillOval(xx * SIZE + X - CHESS / 2, yy * SIZE + Y - CHESS / 2,
					CHESS, CHESS);
		}

	}

	// 定制按钮的事件功能模块
	public void actionPerformed(ActionEvent e) {
		System.out.println("选择模式：" + e.getActionCommand());
		
		if (e.getActionCommand().equals("重新开始")) {
			// 调用repaint清空所有的棋子并使每个下过的点可以再下
			for (int i = 0; i < LINE; i++) {
				for (int j = 0; j < LINE; j++) {
					chessArray[i][j] = 0;
					chessColor = 0;
				}
			}
			j.repaint();
		}
		
		if (e.getActionCommand().equals("人人对战")) {
				j.addMouseListener(this); 
				chooseModule = 1;
		}
			
		if (e.getActionCommand().equals("人机对战")) {
				j.addMouseListener(this); 
				startAI = new AI(g, chessArray, j);
				chooseModule = 2;
		}
		
		
		if (e.getActionCommand().equals("悔棋")) {
			
			if (chooseModule==2) {
				chessArray[xx][yy] = 0;
				chessColor = 0;
				// 重绘画面
				j.update(g);
			}else {
				// 弹出人人对战不可悔棋
				winFrame(0);
			}
			
		}
		
	}
	
	// 对选择的游戏模块进行反应
	public void mouseClicked(MouseEvent e) {
		x1 = e.getX();
		y1 = e.getY();
		if (chooseModule == 1) {
			caculate_p2p();
			System.out.println("模式为1");
		}
		if (chooseModule == 2) {
			caculate_p2c();
			System.out.println("模式为2");
			if(CheckWin(xx, yy)){
				winFrame(chessArray[xx][yy]);
			}
		    try {
			    Thread.sleep(350);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		      
			startAI.chessAI();
			if(CheckWin(startAI.getMaxi(), startAI.getMaxi())){
				winFrame(-chessArray[xx][yy]);
			}
		}

	}
	
}