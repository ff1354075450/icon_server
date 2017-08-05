package com.dianying.service;

import com.dianying.Config;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ff135 on 2017/8/3.
 */
public class IconUtil {
    public final static int HeadSize=10;
    private final static String savePath= Config.UPLOAD_DIR;

    public static boolean saveCharacter(int width,int height,int startX,int startY,String key,long[] arr){
        try {
            File file = new File(savePath+key);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(HeadSize);
            fos.write(width);
            fos.write(height);
            fos.write(int2Bytes(startX));
            fos.write(int2Bytes(startY));
            for(long l:arr){
                fos.write(long2Bytes(l));
            }
            fos.flush();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static byte[] int2Bytes(int num) {
        byte[] byteNum = new byte[4];
        for (int ix = 0; ix < 4; ++ix) {
            int offset = 32 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;
    }
    public static byte[] long2Bytes(long num) {
        byte[] byteNum = new byte[8];
        for (int ix = 0; ix < 8; ++ix) {
            int offset = 64 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;
    }


    public static long getCharacter(File srcImg,int startX,int startY,int desWidth,int desHeight) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(srcImg);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int[] pixels = bufferedImage.getRGB(0, 0, width, height, null, 0, width);
        return getCharacter(pixels, width, startX,startY, desWidth, desHeight);
    }
    /**
     * 从图片数组中取出特征值
     * @param pixles
     * @param srcwidth
     * @param desx
     * @param desy
     * @param deswidth
     * @param desheight
     * @return
     */
    private static long getCharacter(int[] pixles,int srcwidth,int desx,int desy,int deswidth,int desheight){
        int line = 0;
        int[] rs = new int[desheight * deswidth];
        int[] gs = new int[desheight * deswidth];
        int[] bs = new int[desheight * deswidth];
        int i=0;
        for (int y = desy; y < desheight + desy; y++) {
            line = srcwidth * y;
            for (int x = desx; x < deswidth + desx; x++) {
                int index = line + x;
                int r = (pixles[index] & 0xff0000) >> 16;//2个字节
                int g = (pixles[index] & 0xff00) >> 8;//1个字节
                int b = (pixles[index] & 0xff) >> 0;

                rs[i] = r;
                gs[i] = g;
                bs[i] = b;
                i++;
            }
        }
        int rAverage = getAverage(rs);
        int gAverage = getAverage(gs);
        int bAverage = getAverage(bs);

        int rVariance = getVariance(rs,rAverage);
        int gVariance = getVariance(gs,gAverage);
        int bVariance = getVariance(bs,bAverage);
        long result = makeKey(rAverage,gAverage,bAverage,rVariance,gVariance,bVariance);
        return result;
    }
    /**
     * 计算数组的平均值
     * @param values
     * @return
     */
    private static int getAverage(int[] values){
        int sum = 0;
        for (int value:
                values) {
            sum += value;
        }
        return sum/values.length;
    }

    /**
     * 计算方差
     * @param values
     * @param average
     * @return
     */
    private static int getVariance(int[] values,int average){
        int sum = 0;
        for (int value:
                values) {
            sum += Math.pow((value - average),2);
        }
        return sum/values.length;
    }
    public static long makeKey(int aveR,int aveG,int aveB,int varR,int varG,int varB){
        return (aveR&0xff)|(aveG&0xff)<<8|(aveB&0xff)<<16|(varR&0x1fffL)<<24|(varG&0x1fffL)<<37|(varB&0x1fffL)<<50;
    }

}
