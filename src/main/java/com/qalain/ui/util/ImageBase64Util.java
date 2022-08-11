package com.qalain.ui.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.io.*;

/**
 * @author lain
 * @Description
 * @create 2022-01-24
 */
@Slf4j
public class ImageBase64Util {
    /**
     * 将图片转换成Base64编码
     *
     * @param imgFilePath 待处理图片的路径
     * @return
     */
    public static String getImgBase64Code(String imgFilePath) {
        File imgFile = new File(imgFilePath);
        if (!imgFile.exists()) {
            return null;
        }
        return getImgBase64Code(imgFile);
    }

    /**
     * 将图片转换成Base64编码
     *
     * @param imgFile 待处理图片
     * @return
     */
    public static String getImgBase64Code(File imgFile) {
        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }

        }
        if (data == null) {
            return null;
        }
        return new String(Base64.encodeBase64(data));
    }

    /**
     * 对字节数组字符串进行Base64解码并生成图片
     *
     * @param imgStr      图片数据
     * @param imgFilePath 保存图片全路径地址
     * @return
     */
    public static boolean generateImage(String imgStr, String imgFilePath) {
        if (StringUtils.isNoneBlank(imgStr)) {
            return false;
        }
        OutputStream out = null;
        try {
            byte[] byteArray = Base64.decodeBase64(imgStr);
            for (int i = 0; i < byteArray.length; ++i) {
                if (byteArray[i] < 0) {
                    byteArray[i] += 256;
                }
            }
            out = new FileOutputStream(imgFilePath);
            out.write(byteArray);
            out.flush();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return true;
    }
}
