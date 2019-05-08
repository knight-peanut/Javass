package cn.sxt.game;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class GameUtil {

    //工具类一般将构造器私有化
	private GameUtil() {
	}
	//返回图片指定对象，定义得到图像的方法
	public static Image getImage(String path) {
		BufferedImage bi = null;
		try {
			URL u = GameUtil.class.getClassLoader().getResource(path);
			bi = ImageIO.read(u);   //ImageIO中读取图像的方法
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bi;
	}
}
