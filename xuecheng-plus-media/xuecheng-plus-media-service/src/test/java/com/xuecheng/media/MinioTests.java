package com.xuecheng.media;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import io.minio.*;
import io.minio.errors.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 苏航
 * @description
 * @date 2023/4/9 10:53
 **/
@SpringBootTest
public class MinioTests {

    MinioClient minioClient=new MinioClient.Builder().endpoint("http://192.168.207.128:9000")
            .credentials("minioadmin","minioadmin").build();
    @Test
    void  testUpLoad() throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        ContentInfo extensionMatch=ContentInfoUtil.findExtensionMatch(".mp4");
        String mimeType =MediaType.APPLICATION_OCTET_STREAM_VALUE;
        if(extensionMatch!=null){
            mimeType = extensionMatch.getMimeType();

        }

        minioClient.uploadObject(UploadObjectArgs.builder().
                //桶、文件路径、对象名
                bucket("testbucket")
                .filename("E:\\图片\\95024783_p0.jpg")
                .object("1.jpg")
                        .contentType(String.valueOf(MediaType.IMAGE_JPEG))
                .build());

    }
    @Test
    void delete() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        RemoveObjectArgs  removeObjectArgs= RemoveObjectArgs
                .builder().bucket("testbucket").object("1.jpg").build();

        minioClient.removeObject(removeObjectArgs);
    }

    @Test
    void findAndDownLoad() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        GetObjectArgs getObjectArgs= GetObjectArgs.
                builder().object("1.jpg").bucket("testbucket").build();

        //得到远程输入流
        GetObjectResponse response = minioClient.getObject(getObjectArgs);
        //指定输出流
        FileOutputStream fileOutputStream=new FileOutputStream("E:\\图片\\upload\\minioTest1.jpg");
        IOUtils.copy(response,fileOutputStream);
        //校验文件完整性，对文件进行md5,查看前后是否有区别，应该与原始文件的md5相比而不是远程minio里面的--将文件md5保存到服务器
       String minio = DigestUtils.md5Hex(response);
        String local = DigestUtils.md5Hex(new
                FileInputStream("E:\\图片\\upload\\minioTest1.jpg"));
        if(minio.equals(local)){
            System.out.println("下载成功");
        }



    }

    //将分块文件上传导minio
    @Test
    void testUpChunk() throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        for (int i = 0; i < 2; i++) {
            minioClient.uploadObject(UploadObjectArgs.builder().
                    //桶、文件路径、对象名
                            bucket("testbucket")
                    .filename("D:\\video\\chunk\\"+i)
                    .object("chunk/"+i)
                    .build());
            System.out.println("上传分块"+i+"成功");
        }

    }
    //minio合并分块
    @Test
    void testMerge() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        List<ComposeSource> list=new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ComposeSource testbucket = ComposeSource.builder().
                    bucket("testbucket").object("chunk/" + i).build();
            list.add(testbucket);
        }

        //指定合并后的文件name
        ComposeObjectArgs testbucket = ComposeObjectArgs.builder()
                .bucket("testbucket").object("merge01.mp4").sources(list)
                .build();
        //minio默认文件分块大小为5M
        minioClient.composeObject(testbucket);
    }

    //清理分块文件

}
