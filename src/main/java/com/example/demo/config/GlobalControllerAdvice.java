package com.example.demo.config;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.demo.authentication.CustomUserDetails;
import com.example.demo.entity.User;

import jakarta.servlet.http.HttpServletRequest;

//全てのコントローラーで共通して使いたい処理を記述するクラス
@ControllerAdvice
public class GlobalControllerAdvice {

    //全てのコントローラーに『model.addAttribute("currentUri", request.getRequestURI());』を自動で追加し、ビュー側で使えるようにしている
    @ModelAttribute("currentUri")
    public String getCurrentUri(HttpServletRequest request) {
        return request.getRequestURI();
    }
    
    //全てのコントローラーに『model.addAttribute("user", userDetails.getUser());』を自動で追加
    @ModelAttribute("user")
    public User addCurrentUserToModel(@AuthenticationPrincipal CustomUserDetails userDetails) {
        
        //ログイン画面やユーザー登録画面など、まだログインしていない状態ではuserDetailsがnullになるため、nullを返す
        if (userDetails == null) {
            return null;
        }
        
        //ログインしている場合はログインユーザーの情報を渡す
        return userDetails.getUser();
    }
}
