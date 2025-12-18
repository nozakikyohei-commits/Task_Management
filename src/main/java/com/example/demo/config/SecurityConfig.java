package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.constant.AppConst;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http.authorizeHttpRequests(authz -> authz
				.requestMatchers("/css/**", "/js/**", "/img/**").permitAll() //フロントエンド関連のファイルへのアクセス許可
				.requestMatchers(AppConst.Url.DEFAULT, AppConst.Url.LOGIN, AppConst.Url.CREATE_USER).permitAll() //ログイン認証前にも開ける画面を設定
				.requestMatchers(AppConst.Url.VIEW_ALL_TASKS, AppConst.Url.VIEW_ALL_USERS).hasRole("ADMIN") //デフォルトで「ROLE_」という接頭辞をつけてくれる
				.anyRequest().authenticated() //上記以外はログイン認証を必要とする
		).formLogin(login -> login
				.loginPage(AppConst.Url.LOGIN) //認証前のユーザーが保護されたURLにアクセスしようとした場合、/loginへGETリクエストを送信
				.defaultSuccessUrl(AppConst.Url.VIEW_TASKS, true) //ログイン成功後の遷移先を設定
				.usernameParameter("mailAddress") //formからmailAddressというフィールド名のものを探す
				.passwordParameter("password") //formからpasswordというフィールド名のものを探す
				.permitAll() //ユーザー権限に関係なく上記の処理を行う
		).logout(logout -> logout
				//.logoutRequestMatcher(new AntPathRequestMatcher("/logout**"))
				//.logoutSuccessUrl("/")
				.logoutUrl(AppConst.Url.LOGOUT) //ログアウト時に投げるURLを設定
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID")
				.permitAll()
		);
		
		return http.build();
	}

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
