package com.niuml.niu_study_rocketmq;

import net.coobird.thumbnailator.Thumbnails;

import java.io.IOException;

/***
 * @author niumengliang
 * Date:2024/12/5
 * Time:10:17
 */
public class TTTest {

    static String sourcePath = "/Users/niumengliang/Downloads/WechatIMG210.jpg";
    static String toPath = "/Users/niumengliang/Downloads/WechatIMG210666.png";
    public static void main(String[] args) throws IOException {
        Thumbnails.of(sourcePath).size(200,200).toFile(toPath);
    }
}
