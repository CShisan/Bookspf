package cn.bookspf.utils;

import cn.bookspf.model.RO.CaptchaResponse;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.concurrent.ThreadLocalRandom;



public class Generator {
	private static char[] chars = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J','K',
			'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W','X',
			'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
			'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
			'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	/**
	 * 生成accesstoken
	 * @return accesstoken
	 */
	public static String genAccesstoken(){
		return genCodeByNum(32);
	}

	/**
	 * 生成用户ID
	 * @return	Uid
	 */
	public static Integer generateUid() {
		String uid =String.valueOf(System.currentTimeMillis()).substring(1, 8);
		return Integer.parseInt(uid);
	}

	/**
	 * 生成订单ID
	 * @return 订单ID
	 */
	public static Long generateId(){
		String uid =String.valueOf(System.currentTimeMillis());
		return  Long.parseLong(uid);
	}

	/**
	 * 生成当前时间串
	 * @return 时间串
	 */
	public static String generateTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(System.currentTimeMillis());
	}

	/**
	 * 生成验证码图片JSON
	 * @return 验证码图片JSON
	 * @throws IOException
	 */
	public static CaptchaResponse generateCaptchaImg() throws IOException {
		String captcha = genCodeByNum(4);
		BufferedImage image = genCaptchaImg(captcha);
		ByteArrayOutputStream out= new ByteArrayOutputStream();
		ImageIO.write(image,"jpg",out);
		Base64 base64 = new Base64();
		String imgJSON = base64.encodeToString(out.toByteArray());
		return new CaptchaResponse(captcha,imgJSON);
	}

	/**
	 * 生成n位字符串
	 * @return n位随机字符串
	 */
	public static String genCodeByNum(Integer num) {
		StringBuilder code = new StringBuilder();
		for(int i=0 ; i<num ; i++){
			//随机选取一个字母或数字
			char c = chars[ThreadLocalRandom.current().nextInt(chars.length)];
			code.append(c);
		}
		return code.toString();
	}


	/**
	 * 生成验证码图片
	 * @param captcha 验证码
	 * @return 验证码图片
	 */
	public static BufferedImage genCaptchaImg(String captcha){
		ThreadLocalRandom r = ThreadLocalRandom.current();
		int count=captcha.length();
		//字体大小
		int fontSize = 80;
		//字体间隔
		int fontMargin=fontSize/4;
		//图片宽度
		int width=(fontSize+fontMargin)*count+fontMargin;
		//图片高度
		int height= (int) (fontSize*1.2);
		int avgWidth=width/count;
		int maxDegree=26;

		//背景颜色
		Color bkColor = Color.WHITE;
		//验证码颜色
		Color[] captchaColor={Color.MAGENTA, Color.BLACK, Color.BLUE, Color.CYAN, Color.GREEN, Color.ORANGE, Color.PINK};

		BufferedImage image=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		Graphics2D g=image.createGraphics();

		//填充底色灰白
		g.setColor(bkColor);
		g.fillRect(0,0,width,height);

		//画边框
		g.setColor(Color.BLACK);
		g.drawRect(0,0,width-1,height-1);

		//画干扰字母,数字
		int dSize=fontSize/3;//调整分母大小以调整干扰字符大小
		Font font = new Font("Fixedsys",Font.PLAIN,dSize);
		g.setFont(font);
		int dNumber=width*height/dSize/dSize;//根据面积计算干扰字母的个数
		for (int i=0;i<dNumber;i++){
			char d_code = chars[r.nextInt(chars.length)];
			g.setColor(new Color(r.nextInt(255),r.nextInt(255),r.nextInt(255)));
			g.drawString(String.valueOf(d_code),r.nextInt(width),r.nextInt(height));
		}


		//画验证码

		//创建字体
		font = new Font(Font.MONOSPACED,Font.ITALIC|Font.BOLD, fontSize);
		//设置字体
		g.setFont(font);

		for(int i=0;i<count;i++){
			char c=captcha.charAt(i);

			//随机转角度
			int degree=r.nextInt(-maxDegree,maxDegree);

			//偏移系数,和旋转角度成反比,以避免字符在图片中越出边框
			double offsetFactor=1-(Math.abs(degree)/(maxDegree+1.0));//加1避免0

			g.rotate(degree*Math.PI/180);//旋转角度
			int x=(int) (fontMargin+r.nextInt(avgWidth-fontSize)*offsetFactor);//横向偏移的距离
			int y = (int) (fontSize + r.nextInt(height-fontSize)*offsetFactor); //上下偏移的距离

			g.drawString(String.valueOf(c),x,y);//x,y是字符的左下角，偏离原点的距离

			g.rotate(-degree*Math.PI/180);//画完一个字符之后，旋转回原来的角度
			g.translate(avgWidth,0);//移动到下一个画画的原点

		}
		g.dispose();
		return image;
	}
}
