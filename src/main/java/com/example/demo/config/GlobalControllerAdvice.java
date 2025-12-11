package com.example.demo.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;

//全てのコントローラーで共通して使いたい処理を記述するクラス
@ControllerAdvice
public class GlobalControllerAdvice {

    //全てのコントローラーに『model.addAttribute("currentUri", request.getRequestURI());』を自動で追加し、ビュー側で使えるようにしている
    @ModelAttribute("currentUri")
    public String getCurrentUri(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
