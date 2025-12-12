//htmlの読み込みが完了するのを待ってから処理を走らせるという指示
//これがないとhtml側でid=calendarが読み込まれる前にcalendarの場所を探し始めエラーを起こしてしまう
document.addEventListener('DOMContentLoaded', function() {
  
  //カレンダーを表示する場所を指定（html側でid=calendarの要素を指定）
  var calendarEl = document.getElementById('calendar');
  
  //配列taskListの中身一つ一つに{}内の加工処理を行って、新たな配列を作成する
  var calendarEvents = taskList.map(function(task) {
    
    //オブジェクト定義（return内）にはキーと値のペアしか書けないため、条件によって値を変更する際は先に準備して変数を渡すだけの状態にしておく
    
    //色を決める変数を定義
    var eventColor = 'gray';

    //重要度に応じて変数の中身（色）を変更
    switch(task.importance) {
        case 1: // 低
            eventColor = '#28a745'; // Bootstrapのsuccessと同じ
            break;
        case 2: // 中
            eventColor = '#ffc107'; // Bootstrapのwarningと同じ
            break;
        case 3: // 高
            eventColor = '#dc3545'; // Bootstrapのdangerと同じ
            break;
        default:
            eventColor = 'gray';
    }
    
    var borderColor = 'transparent';	//初期値（透明）
    
    var eventClassNames = [];	//初期値（空配列）
    
    //表示タイトルを決めるための変数を定義
    var eventTitle = task.name;

    // status: 2 (期限切れ) の場合に表示を変更する
    if (task.status === 2) {
		//期限切れであることがわかりやすいように炎のマークを表示する
        eventTitle = '🔥 ' + eventTitle + ' 🔥 ';
        //重要度に応じた色はボーダーに持たせ、期限切れでも重要度が何だったかわかるようにする
        borderColor = eventColor;
        //期限切れであることがわかりやすいように色を変更する
        eventColor = '#343a40';
        //配列の末尾にデータを追加する指示（ボーダーの太さを設定する独自クラスを持たせられるよう設定しておく）
        eventClassNames.push('expired-task-border');
    }

    //設定済みの色・タイトルを使ってオブジェクトを返す
    return {
	  //FullCalendar側が探す要素名に合わせてキーと値を設定する
      title: eventTitle,
      start: task.deadline,	//表示させる日付の設定
      url: '/edit-task/' + task.taskId,	//クリック後の遷移先の設定
      color: eventColor,
      borderColor: borderColor,
      classNames: eventClassNames,
    };
  });

  var calendar = new FullCalendar.Calendar(calendarEl, {
	//FullCalendarで設定されている「月表示モード」を選択
    initialView: 'dayGridMonth',
    //上で詰めたデータを渡す
    events: calendarEvents
  });
  //持たせた情報を活用してカレンダーを作成
  calendar.render();
});