package com.example.user.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import org.apache.commons.codec.binary.Base64;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FilesUtils {
    public static Map<String, String> saveImgByBase64(String base64Code,String userName) throws Exception{
        System.out.println(base64Code);
        String uploadFileName = "headPhoto";
        Map map  = new HashMap<>();
        String message = "";
        if(null == base64Code || "".equals(base64Code))
        {
            map.put("code", "error");
            return map;
        }

        //相对保存路径
//        String relativePath="resources\\upload\\"+uploadFileName+"\\"+ DateUtil.format(new Date(), "yyyyMMdd")+"\\";
        ClassLoader classLoader = FilesUtils.class.getClassLoader();
        URL url = classLoader.getResource("static/images");
        String relativePath = url.getPath().toString();
        //base64 统一保存为jpeg文件格式，并用时间戳重命名文件
        String newFileName="/"+userName+System.currentTimeMillis()+".jpg";

        //判断要存储的文件夹是否存在，不存在则创建
        String holePath=relativePath;
        System.out.println("完整存储路径："+holePath);
        File f=new File(holePath);
        if(!f.exists()){
            f.mkdirs();
        }
        try{
            Base64 base64 = new Base64();
            byte[] bytes = base64.decodeBase64(new String(base64Code.replace("data:image/png;base64,","")).getBytes());
            //保存到新文件
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            byte[] buffer = new byte[1024];
            FileOutputStream out = new FileOutputStream(holePath+newFileName);
            int bytesum = 0;
            int byteread = 0;
            while ((byteread = in.read(buffer)) != -1) {
                bytesum += byteread;
                out.write(buffer, 0, byteread); // 文件写操作
                out.flush();
            }
            out.close();
            map.put("code", relativePath+newFileName);
        }catch (Exception e) {
            map.put("code", "error");
            return map;
        }
        return map;
    }
}
