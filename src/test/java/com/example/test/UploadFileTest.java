package com.example.test;

import com.baomidou.mybatisplus.annotation.TableField;
import org.junit.jupiter.api.Test;

public class UploadFileTest {

    @Test
    public void test1(){
        String fileName="ajhdja.jpg";
        String substring = fileName.substring(fileName.lastIndexOf('.'));
        System.out.println(substring);
    }

}
