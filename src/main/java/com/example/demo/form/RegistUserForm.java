package com.example.demo.form;

import java.util.Objects;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistUserForm {
	
	@NotBlank(message = "{E0004}")	//バリデーションをアノテーションで設定
	@Size(max = 20, message = "{E0005}")
	private String name;
	
	@NotBlank(message = "{E0001}")
	@Pattern(regexp = "^[ -~]{0,255}$", message = "{E0006}")	//[ -~]でASCⅡコード表にあるすべての文字を対象取る
	private String mailAddress;
	
	@NotBlank(message = "{E0002}")
	//Javaの正規表現エンジン（java.util.regex.Pattern）に登録されている記号グループ（Punct）を使用
	//\p{〇〇}という書き方でグループ名を指定可能（\はエスケープシーケンスであるため、二つ重ねる必要がある）
	//^$|という記述で「空文字または～」とし、空文字の際にはNotBlankのみに引っかかるようにしている
	@Pattern(regexp = "^$|^(?=.*\\p{Punct})[!-~]{6,20}$", message = "{E0007}")
	private String password;
	
	private String passwordConfirmation;
	
	//特定の項目に直接付与されるものではないため、html側でこれに該当するエラーをどこに付与するか設定が必要
	@AssertTrue(message = "{E0009}")
	public boolean isPasswordValid() {
		return Objects.equals(password, passwordConfirmation);	//引数同士を比べて内容が同じか判別する
	}
	
}