package com.xuecheng.media;

import com.xuecheng.base.model.PagePrams;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author 苏航
 * @description
 * @date 2023/4/10 19:51
 **/
@SpringBootTest
public class BigFileTests {
    @Test
    void testChuck() throws IOException {
        File sourseFile=new
                File("D:\\video\\[Airota][Tengoku Daimakyou][01][1080p AVC AAC][CHS].mp4");

        //分块
        String chunkFilePath="D:\\video\\chunk\\";
        //分快文件大小200M
        int chunkSize=1024*1024*200;
        //分快个数
        int chunkNum= (int) Math.ceil(sourseFile.length()*1.0/chunkSize);
        //使用流，从源文件中读数据，向chunk中写数据
        RandomAccessFile r = new RandomAccessFile(sourseFile, "r");
        //缓冲区
        byte[] bytes=new byte[1024];
        for (int i = 0; i < chunkNum; i++) {
            File file = new File(chunkFilePath + i);
            //写入流
            RandomAccessFile rw = new RandomAccessFile(file, "rw");
            int len=-1;
            while ((len=r.read(bytes))!=-1){
                rw.write(bytes,0,len);
                if(file.length()>=chunkSize){
                    break;
                }
            }
            rw.close();

        }
        r.close();

    }
    @Test
    void testMerge() throws IOException {
        //合并

        //块文件的目录
        File chunkFolder=new File("D:\\video\\chunk");
        //源文件
        File sourceFile=new File
                ("D:\\video\\[Airota][Tengoku Daimakyou][01][1080p AVC AAC][CHS].mp4");

        //合并文件
        File mergeFile=new File
                ("D:\\video\\douga.mp4");
        RandomAccessFile rw = new RandomAccessFile(mergeFile, "rw");
        byte[] bytes=new byte[1024];
        //取出所有的分块文件
        File[] files = chunkFolder.listFiles();
        List<File> list = Arrays.asList(files);
        //升序排序
        list.sort((o1, o2) -> Integer.parseInt(o1.getName()) - Integer.parseInt(o2.getName()));

        for (File file : list) {
            RandomAccessFile r = new RandomAccessFile(file, "r");
            int len=-1;
            while ((len=r.read(bytes))!=-1){
                rw.write(bytes,0,len);
            }
            r.close();

        }
        rw.close();
        //进行校验
        FileInputStream fileInputStream=new FileInputStream(mergeFile);
        FileInputStream fileInputStream2=new FileInputStream(sourceFile);
        String mergeMd5 = DigestUtils.md5Hex(fileInputStream);
        String sourceMd5 = DigestUtils.md5Hex(fileInputStream2);
        if(mergeMd5.equals(sourceMd5)){
            System.out.println("文件合并完成");


    }
}}
