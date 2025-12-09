package com.example.demo.authentication;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.constant.AppConst;
import com.example.demo.entity.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
	
	private final User user;
	
	@Override
    public String getUsername() {
        return user.getMailAddress(); // DBに保存されたメールアドレスを返す
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // DBに保存されたハッシュ化済みパスワードを返す
    }
    
    @Override
    //複数の権限を持つことを想定しているため、「GrantedAuthorityを継承したクラスなら何でも入るリスト」を型とする
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // roleフィールドの値によって権限を切り替える
        // 0:一般、1:管理者
        String roleName = user.getRole() == AppConst.UserRole.ADMIN ? "ROLE_ADMIN" : "ROLE_GENERAL";
        
        // Spring Securityが理解できる形（List<GrantedAuthority>）にして返す
        return List.of(new SimpleGrantedAuthority(roleName));
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true; // 期限切れなし
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // ロックなし
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // パスワード期限切れなし
    }

    @Override
    public boolean isEnabled() {
        return true; // 有効
    }
    
    // 必要であれば、画面表示用に元のUserエンティティを取り出すメソッドも作っておくと便利
    public User getUser() {
        return user;
    }

}
