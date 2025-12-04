package com.example.demo.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {

    public static void main(String[] args) {
        // 1. エンコーダーを作成
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // 2. 暗号化したいパスワード（平文）
        String rawPassword = "sample";

        // 3. 暗号化（ハッシュ化）を実行
        String encodedPassword = encoder.encode(rawPassword);

        // 4. 結果をコンソールに出力
        System.out.println("--------------------------------------------------");
        System.out.println("元のパスワード: " + rawPassword);
        System.out.println("暗号化後: " + encodedPassword);
        System.out.println("--------------------------------------------------");
    }
}