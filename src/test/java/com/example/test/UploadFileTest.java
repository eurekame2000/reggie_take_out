package com.example.test;

import com.baomidou.mybatisplus.annotation.TableField;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UploadFileTest {

    @Test
    public void test1(){
        String regex ="^[a-z0-9A-Z]+[- | a-z0-9A-Z . _]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$";
        boolean isMatch = Pattern.matches(regex,"1810713410@qq.com");
        System.out.println(isMatch);
    }

}
