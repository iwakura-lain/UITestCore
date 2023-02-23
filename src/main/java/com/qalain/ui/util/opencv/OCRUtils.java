package com.qalain.ui.util.opencv;

import io.restassured.response.Response;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.testng.internal.collections.Pair;

import java.util.*;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.oauth;
import static org.opencv.core.Core.NATIVE_LIBRARY_NAME;
import static org.opencv.imgcodecs.Imgcodecs.imencode;
import static org.opencv.imgcodecs.Imgcodecs.imread;

/**
 * @author lain
 * @date 2023/02/16 7:33 PM
 */
public class OCRUtils {

    static {
        System.loadLibrary(NATIVE_LIBRARY_NAME);
    }

    public static Map<String, Point> getTargetTextCoordinate (String filePath, String text) {
        Mat src = imread(filePath);

        MatOfByte matOfByte = new MatOfByte();
        boolean imencode = imencode(".jpg", src, matOfByte);
        byte[] array = matOfByte.toArray();
        String encodeToString = Base64.getEncoder().encodeToString(array);

        Response response = given()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .formParam("img", encodeToString)
                .formParam("text", text)
                .when()
                .post("用的公司里的基础服务，感兴趣的可以自己训练模型，或者网上找模型直接用")
                .then()
                .extract().response();

        ArrayList<ArrayList<Integer>> pos = response.path("data.target_list.center_position");
        ArrayList<String> texts = response.path("data.target_list.text");

        LinkedHashMap<String, Point> result = new LinkedHashMap<>();

        for (int i = 0; i < texts.size(); i++) {
            Point point = new Point(pos.get(i).get(0), pos.get(i).get(1));
            result.put(texts.get(i), point);
        }

        return result;
    }
}
