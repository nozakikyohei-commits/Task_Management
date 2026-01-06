document.addEventListener('DOMContentLoaded', function () {
    
    // class="toast" を持つ要素をすべて探す（リストで取得）
    const toastElList = document.querySelectorAll('.toast');
    
    // 見つかったトースト全てに対して処理を行う
    // （リストをループさせて、一つずつ初期化＆表示する）
    const toastList = [...toastElList].map(toastEl => {
        const toast = new bootstrap.Toast(toastEl);
        toast.show(); // 表示！
        return toast;
    });
    
});