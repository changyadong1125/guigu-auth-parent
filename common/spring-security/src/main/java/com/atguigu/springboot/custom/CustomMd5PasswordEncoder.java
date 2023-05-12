package com.atguigu.springboot.custom;

import com.atguigu.springboot.common.util.MD5;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * project:PermissionManagement
 * package:com.atguigu.springboot.custom
 * class:CustomMd5PasswordEncoder
 *
 * @author: smile
 * @create: 2023/5/8-16:37
 * @Version: v1.0
 * @Description:
 */
@Component
public class CustomMd5PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return MD5.encrypt(charSequence.toString());
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return MD5.encrypt(charSequence.toString()).equals(s);

    }
}
