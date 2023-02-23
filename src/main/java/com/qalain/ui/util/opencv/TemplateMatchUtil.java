package com.qalain.ui.util.opencv;

import lombok.extern.slf4j.Slf4j;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.HashMap;
import java.util.Map;

import static org.opencv.core.Core.NATIVE_LIBRARY_NAME;
import static org.opencv.imgcodecs.Imgcodecs.imread;

/**
 * @author lain
 * @date 2023/01/07 6:11 PM
 */

@Slf4j
public class TemplateMatchUtil {

    static {
        System.loadLibrary(NATIVE_LIBRARY_NAME);
    }

    public static Point templateMatchWithCCORRNORMED (String srcImgName, String templateImgName) {
        //待匹配图片
        Mat src = imread(srcImgName, Imgcodecs.IMREAD_GRAYSCALE);
        Mat mInput = src.clone();

        // 获取匹配模板
        Mat mTemplate = imread(templateImgName,Imgcodecs.IMREAD_GRAYSCALE);
        System.out.println(mTemplate.toString());
        System.out.println(mInput.toString());
        /**
         * TM_SQDIFF = 0, 平方差匹配法，最好的匹配为0，值越大匹配越差
         * TM_SQDIFF_NORMED = 1,归一化平方差匹配法
         * TM_CCORR = 2,相关匹配法，采用乘法操作，数值越大表明匹配越好
         * TM_CCORR_NORMED = 3,归一化相关匹配法
         * TM_CCOEFF = 4,相关系数匹配法，最好的匹配为1，-1表示最差的匹配
         * TM_CCOEFF_NORMED = 5;归一化相关系数匹配法
         */
        int resultRows = mInput.rows() - mTemplate.rows() + 1;
        int resultCols = mInput.cols() - mTemplate.cols() + 1;
        Mat gResult = new Mat(resultRows, resultCols, CvType.CV_32FC1);

        Imgproc.matchTemplate(mInput, mTemplate, gResult, Imgproc.TM_CCORR_NORMED);

        Core.normalize(gResult, gResult, 0, 1, Core.NORM_MINMAX, -1, new Mat());
        Core.MinMaxLocResult mmlr = Core.minMaxLoc(gResult);
        Point matchLocation = mmlr.maxLoc;
        Double x = matchLocation.x + (mTemplate.cols() / 2);
        Double y = matchLocation.y + (mTemplate.rows() / 2);
        int resX = (int)Math.floor(x);
        int resY = (int)Math.floor(y);

        if (mInput.rows() < y || mInput.cols() < x) {
            log.error("匹配定位超出图片范围，请检查是否存在匹配图片");
        }

        return new Point(resX, resY);
    }
}
