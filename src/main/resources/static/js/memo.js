document.addEventListener('DOMContentLoaded', function() {
    
    // 1. 操作したい部品（テキストエリア）を探して、変数に入れる
    const memoArea = document.querySelector('.memo-textarea');
    
    // 2. 送信機能を持つ部品（フォーム）を探して、変数に入れる
    // memoFormにはHTMLの塊（DOM要素）が入っている状態
    const memoForm = document.getElementById('memoForm');

    // 3. テキストエリアに「監視員」をつける
    // blur：「フォーカスが外れた（書き終わって別の場所をクリックした）」瞬間を指す
    memoArea.addEventListener('blur', function() {
        
        console.log('書き終わりました。保存します。');
        
        // 1. フォームに入力されているHTMLの塊を、キーと値のデータにして詰めなおす
        // (CSRFトークンなども自動で入る)
        const formData = new FormData(memoForm);

        // 2. fetchを使って、画面遷移せずに送信する
        // memoForm.action には "th:action" で設定したURLが入る
        fetch(memoForm.action, {
            method: 'POST',	//送り方
            body: formData	//中身
        })
        .then(response => {
            // 送信がうまくいった時の処理
            if (response.ok) {
                console.log('保存成功');
            } else {
				// alert("【注意】メモの保存に失敗しました。");
                console.log('保存失敗');
            }
        })
        .catch(error => {
            // 通信エラーが起きた時の処理
            // alert("【通信エラー】サーバーに繋がりません。メモは保存されていません。");
            console.error('エラーが発生しました:', error);
        });

    });
});