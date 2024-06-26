package com.example.lostandfoundbackground.config.security.authManager;

import com.example.lostandfoundbackground.utils.JsonUtils;
import com.example.lostandfoundbackground.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author archi
 */
@Slf4j
public abstract class AbstractCustomAuthManager implements AuthenticationManager {

    public AbstractCustomAuthManager(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String phone = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        //从redis中尝试查询user，不存在再去MySql查询
        Object user = null;
        String key = getLoginKey() + phone;
        if(RedisUtils.hasKey(key)){
            String jsonUser = RedisUtils.get(key);
            if(jsonUser!=null){
                //将json反序列化为administrator类型
                user = JsonUtils.jsonStrToJavaBean(jsonUser, getUserClass());
            }
        }

        //redis中查询不到,从mysql中查询
        if(user == null){
            user = findUserByPhone(phone);
            if(user == null){
                log.info("从数据库查询后发现用户不存在");
                throw new BadCredentialsException("用户不存在");
            }
        }
        //注意这里要使用BCryptEncoder的match方法，不然每次加密结构都是不同的，密码都是错误!
        if(!passwordEncoder.matches(password,getPassword(user))){
            log.info("用户输入的pwd: "+password+"加密后的用户输入的pwd: "+passwordEncoder.encode(password));
            log.info("数据库中用户的pwd: "+ getPassword(user));
            throw new BadCredentialsException("用户密码错误");
        }
        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        grantedAuths.add(new SimpleGrantedAuthority(getRole()));
        return new UsernamePasswordAuthenticationToken(phone, password, grantedAuths);
    }
    protected abstract Object findUserByPhone(String phone);

    protected abstract Class <?> getUserClass();
    protected abstract String getLoginKey();

    protected abstract String getPassword(Object user);

    protected abstract String getRole();
}
