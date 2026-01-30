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
public class EditUserForm {
	
	@NotBlank(message = "{E0004}")
	@Size(max = 20, message = "{E0005}")
	private String name;
	
	@NotBlank(message = "{E0001}")
	@Size(max = 255, message = "{E0006}")
	//全体として「ドットが連続しない」、ローカル部はまず使用できるのが「半角英数字と!#$%&'*+-/=?^_`{|}~.」で「一文字目はドットなし」「二文字目以降があるなら、最後はドットなしで中間部はドットもあり」という条件
	//ドメイン部は『「一文字目は英数字」「二文字目以降は英数字か-」「.の前は必ず英数字」「.で終わる」』という文字列が一回以上繰り返され、その後「英字のみ」で二文字以上という縛りも加え、正しくないメールアドレスは弾く
	//未入力の場合は@NotBlankのエラーメッセージのみ表示されるように、「空文字または～」を表す「~$|」をつけておく
	@Pattern(regexp = "^$|^(?!.*\\.\\.)[a-zA-Z0-9!#$%&'*+\\-/=?^_`{|}~](?:[a-zA-Z0-9!#$%&'*+\\-/=?^_`{|}~.]*[a-zA-Z0-9!#$%&'*+\\-/=?^_`{|}~])?@([a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,}$",message = "{E0016}")
	private String mailAddress;
	
	private String password;
	
	private String passwordConfirmation;
	
		//特定の項目に直接付与されるものではないため、html側でこれに該当するエラーをどこに付与するか設定が必要
		@AssertTrue(message = "{E0007}")
		public boolean isPasswordFormatValid() {
			
			//画面表示の段階ではフィールド値はnull、POSTリクエスト後に入力値がなかった場合は空文字として認識する
			//画面表示する際にも*{passwordValid}を見つけると実行してしまうため、空文字チェックのみではNullPointerExceptionが発生してしまう
			boolean isPasswordEmpty = (password == null || password.isEmpty());
	        boolean isConfirmEmpty = (passwordConfirmation == null || passwordConfirmation.isEmpty());
			
			//どちらも入力値なしであればエラーが出ないようにし、コントローラー側の記述によってパスワードを変更しなくて済むようにする
	        if(isPasswordEmpty && isConfirmEmpty) {
	            return true;
	        } else if(!isPasswordEmpty) {
				//入力値があれば「記号を必ず含む6～20字」かチェックする
				return password.matches("^(?=.*\\p{Punct})[!-~]{6,20}$");
			}
			//パスワード：null,パスワード確認用：入力有の場合は{E0009}のみ表示されるようtrueを返す
			return true;
		}
	
		//特定の項目に直接付与されるものではないため、html側でこれに該当するエラーをどこに付与するか設定が必要
		@AssertTrue(message = "{E0009}")
		public boolean isPasswordMatching() {
			return Objects.equals(password, passwordConfirmation);	//引数同士を比べて内容が同じか判別する
		}

}
