package com.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.reggie.common.R;
import com.example.reggie.entity.User;
import com.example.reggie.service.UserService;
import com.example.reggie.utils.MailUtils;
import com.example.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

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
            //session.setAttribute(email,code);

            //将生成的验证码缓存到redis中，并设置有效期为5分钟
            redisTemplate.opsForValue().set(email,code,5, TimeUnit.MINUTES);

            return R.success("邮箱验证码发送成功");
        }

        return R.error("邮件发送失败");
    }

    /**
     * 移动端用户登录
     * @param map
     * @param session
     * @return
     * @throws GeneralSecurityException
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) throws GeneralSecurityException {

        log.info(map.toString());

        //获取邮箱
        String email = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();
        //从Session中获取保存的验证码
        //Object codeInSession = session.getAttribute(email);

        //从redis中获取缓存的验证码
        Object codeInSession = redisTemplate.opsForValue().get(email);

        //进行验证码的比对（页面提交的验证码和Session中保存的验证码比对）
        if(codeInSession!=null&&codeInSession.equals(code)){
            //如果比对成功，说明登录成功
            LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,email);

            User user = userService.getOne(queryWrapper);

            if(user==null){
                //判断当前邮箱对应的用户是否是新用户，如果是新用户就自动完成注册
                user=new User();
                user.setPhone(email);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            //如果用户登录成功，删除redis中缓存的验证码
            redisTemplate.delete(email);

            return R.success(user);
        }
        return R.error("登录失败");
    }
}
