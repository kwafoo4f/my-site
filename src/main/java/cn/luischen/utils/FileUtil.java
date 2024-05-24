package cn.luischen.utils;

import org.apache.commons.compress.compressors.FileNameUtil;
import org.apache.commons.compress.utils.FileNameUtils;

import java.util.Date;

/**
 * @description:
 * @author: zk
 * @date: 2024-05-24 16:43
 */
public class FileUtil {

    public static String getExtension(String fileName) {
        final int extensionIndex = fileName.lastIndexOf('.');
        return extensionIndex < 0 ? "" : fileName.substring(extensionIndex + 1);
    }

    public static String genFileName(String originalFilename) {
        String extension = getExtension(originalFilename);
        String prefix = DateKit.dateFormat(new Date(), "yyyy/MM");
        return prefix+"/"+UUID.UU32()+"."+extension;
    }

    public static void main(String[] args) {
        String fileName = genFileName("IMG_5507 (3) (1).jpeg");
        System.out.println(fileName);
    }
}
