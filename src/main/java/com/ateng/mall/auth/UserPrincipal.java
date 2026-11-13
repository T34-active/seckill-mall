package com.ateng.mall.auth;

import com.ateng.mall.user.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * 当前登录用户在 Spring Security 中的“身份凭证”。
 *
 * <p>业务表里的 {@link User} 不能直接给 Security 用，需要适配成 {@link UserDetails}。
 * 登录成功、JWT 校验通过后，这个对象会放进 SecurityContext；
 * Controller 里可用 {@code @AuthenticationPrincipal UserPrincipal} 拿到当前用户。</p>
 *
 * <p>常见链路：UserDetailsServiceImpl 查库 → new UserPrincipal(user) → JWT / 鉴权。</p>
 */
@Getter
public class UserPrincipal implements UserDetails {

    /** 业务用户主键，下单、查自己的订单时会用到 */
    private final Long id;

    /** 登录名，也是 JWT subject 一类标识 */
    private final String username;

    /** 加密后的密码；鉴权比对用，一般不要再返回给前端 */
    private final String password;

    /**
     * 权限/角色集合。
     * Spring Security 约定角色名以 {@code ROLE_} 开头，例如库里是 USER → 这里是 ROLE_USER。
     */
    private final Collection<? extends GrantedAuthority> authorities;

    /**
     * 用业务实体构造 Security 身份。
     * 把 User 的字段拷过来，并把 role 转成 GrantedAuthority。
     */
    public UserPrincipal(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole())
        );
    }

    /** 账号是否未过期。本项目不做过期策略，固定返回 true。 */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /** 账号是否未锁定。本项目不做锁定策略，固定返回 true。 */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /** 密码是否未过期。本项目不做密码过期，固定返回 true。 */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /** 账号是否启用。本项目暂无禁用账号逻辑，固定返回 true。 */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
