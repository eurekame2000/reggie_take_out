package com.example.reggie.controller;

import com.example.reggie.common.R;
import com.example.reggie.entity.User;
import com.example.reggie.service.UserService;
import com.example.reggie.utils.MailUtils;
import com.example.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${reggie.email-from}")
    private String from;

    @Value("${reggie.email-to}")
    private String to;

    /**
     * 发送邮箱验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) throws GeneralSecurityException {
        //获取邮箱号
        String email = user.getPhone();

        if(StringUtils.isNotEmpty(email)){
            //生成随机的4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code:{}",code);


            System.out.println("from:"+from);
            System.out.println("to:"+to);

            //发送邮件
            new MailUtils().sendMail(code,from,to);

            //生成的验证码保存到session
            session.setAttribute(email,code);

            return R.success("邮箱验证码发送成功");
        }

        return R.error("邮件发送失败");
    }

    /**
     * 移动端用户登录
     * @param user
     * @return
     */
    @PostMapping("/login")
    public R<String> login(@RequestBody User user, HttpSession session) throws GeneralSecurityException {


        return R.error("邮件发送失败");
    }
}
