package com.example.demo.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.constant.AppConst;
import com.example.demo.entity.Task;
import com.example.demo.form.CreateTaskForm;
import com.example.demo.form.EditTaskForm;
import com.example.demo.form.SearchTaskForm;
import com.example.demo.mapper.TaskMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {
	
	private final TaskMapper taskMapper;
	
	public Task getsTaskById(int taskId) {
		return taskMapper.getsTaskById(taskId);
	}
	
	public List<Task> getTasksForCalendar(int userId) {
		
		return taskMapper.getTasksForCalendar(userId, AppConst.TaskStatus.INCOMPLETE, AppConst.TaskStatus.EXPIRED);
	}
	
	public List<Task> getIncompleteTasksByUserId(int userId, String sort, String order) {
		
		return taskMapper.getIncompleteTasksByUserId(userId, sort, order, AppConst.TaskStatus.INCOMPLETE, AppConst.TaskStatus.EXPIRED);
	}
	
	public List<Task> getCompletedTasksByUserId(int userId, String sort, String order) {
		
		return taskMapper.getCompletedTasksByUserId(userId, sort, order, AppConst.TaskStatus.COMPLETED, AppConst.TaskStatus.EXPIRED_COMPLETED);
	}
	
	public List<Task> searchTasks(SearchTaskForm form, String sort, String order) {
		
		return taskMapper.searchTasks(form, sort, order);
	}
	
	@Transactional(readOnly = false)
	public void create(CreateTaskForm form, int userId) {
		
		Task task = new Task();
		
		task.setUserId(userId);
		task.setName(form.getName());
		task.setContent(form.getContent());
		task.setImportance(form.getImportance());
		task.setDeadline(form.getDeadline());
		
		taskMapper.create(task);
	}
	
	@Transactional(readOnly = false)
	public void update(EditTaskForm form, int taskId, int userId) {
		
		Task task = new Task();
		
		task.setTaskId(taskId);
		task.setUserId(userId);
		task.setName(form.getName());
		task.setContent(form.getContent());
		task.setImportance(form.getImportance());
		task.setDeadline(form.getDeadline());
		task.setCompletedDate(form.getCompletedDate());
		
		int newStatus = determineStatus(form.getCompletedDate(), form.getDeadline());
		task.setStatus(newStatus);
		
		taskMapper.update(task);
	}
	
	//タスク更新時に、フォームに入力されている完了日と期限に応じたステータスを返すメソッド
	private int determineStatus(LocalDate completedDate, LocalDate deadline) {
		
		LocalDate today = LocalDate.now();
		
		//完了日が設定されている場合
		if (completedDate != null) {
			//期限が設定されていて、かつ「完了日」が「期限」を過ぎている場合
			if (deadline != null && completedDate.isAfter(deadline)) {
				return AppConst.TaskStatus.EXPIRED_COMPLETED; //期限切れ完了 
			}
			//それ以外は通常の「完了」
			return AppConst.TaskStatus.COMPLETED; //完了
		}
		
		//完了日が設定されていない場合
		else {
			//期限が設定されていて、かつ「期限」が「今日」より前の場合
			if (deadline != null && deadline.isBefore(today)) {
				return AppConst.TaskStatus.EXPIRED; //期限切れ
			}
			//それ以外は「未完了」
			return AppConst.TaskStatus.INCOMPLETE; //未完了
		}
	}
	
	@Transactional(readOnly = false)
	public void updateStatusToExpired(int userId) {
		taskMapper.updateStatusToExpired(userId, AppConst.TaskStatus.INCOMPLETE, AppConst.TaskStatus.EXPIRED);
	}
	
	@Transactional(readOnly = false)
	public void updateStatusToCompleted(int taskId, int userId) {
		taskMapper.updateStatusToCompleted(taskId, userId, AppConst.TaskStatus.COMPLETED, AppConst.TaskStatus.EXPIRED_COMPLETED);
	}
	
	@Transactional(readOnly = false)
	public void updateStatusToIncompleted(int taskId, int userId) {
		taskMapper.updateStatusToIncompleted(taskId, userId, AppConst.TaskStatus.INCOMPLETE, AppConst.TaskStatus.EXPIRED);
	}
	
	@Transactional(readOnly = false)
	public void delete(int taskId, int userId) {
		taskMapper.delete(taskId, userId);
	}
	
	/*
     * CSVデータの作成
     */
    public byte[] generateCsvForIncompleteTasks(int userId) {
        // 出力したいデータを取得（例として未完了タスクを取得）
        // ※必要に応じて検索条件などを含めた全件取得メソッドを使うのも良いです
        List<Task> tasks = taskMapper.getIncompleteTasksByUserId(userId, "deadline", "asc", AppConst.TaskStatus.INCOMPLETE, AppConst.TaskStatus.EXPIRED);

        StringBuilder sb = new StringBuilder();

        // ヘッダー行
        sb.append("タスク名,期限,重要度,ステータス,詳細\n");

        // データ行
        for (Task task : tasks) {
            sb.append(escapeCsv(task.getName())).append(",");
            sb.append(task.getDeadline()).append(",");
            sb.append(getImportanceText(task.getImportance())).append(",");
            sb.append(getStatusText(task.getStatus())).append(",");
            sb.append(escapeCsv(task.getContent())).append("\n");
        }

        return addBomToCsv(sb.toString());
    }
    
    public byte[] generateCsvForCompletedTasks(int userId) {
        
        List<Task> tasks = taskMapper.getCompletedTasksByUserId(userId, "deadline", "asc", AppConst.TaskStatus.COMPLETED, AppConst.TaskStatus.EXPIRED_COMPLETED);

        StringBuilder sb = new StringBuilder();

        sb.append("タスク名,期限,重要度,ステータス,完了日,詳細\n");

        for (Task task : tasks) {
            sb.append(escapeCsv(task.getName())).append(",");
            sb.append(task.getDeadline()).append(",");
            sb.append(getImportanceText(task.getImportance())).append(",");
            sb.append(getStatusText(task.getStatus())).append(",");
            sb.append(task.getCompletedDate()).append(",");
            sb.append(escapeCsv(task.getContent())).append("\n");
        }

        return addBomToCsv(sb.toString());
    }
    
    // 文字列をBOM付きバイト配列に変換する処理
    private byte[] addBomToCsv(String csvContent) {
    	
    	try {
    		// csvContentの中身をUTF-8の文字コードに変換したものを格納する
            byte[] csvBytes = csvContent.getBytes(StandardCharsets.UTF_8);
            // 上記の配列と結合し、これを最初にコンピュータに読み込ませることでUTF-8として読み込ませる（※WindowsのExcelで開く場合、デフォルトではShift_JISとして読み込もうとし、BOMがないと文字化けすることがあるため）
            // 0xは16進数であることを示す（10進数でいうと、239,187,191という並び）
            byte[] bom = {(byte)0xEF, (byte)0xBB, (byte)0xBF};
            // 最終的に返すための「上記二つがぴったり入る箱」を用意
            byte[] result = new byte[bom.length + csvBytes.length];
            // それぞれの引数の意味：(コピー元, コピー元の開始位置, コピー先, コピー先の開始位置, コピーする長さ)
            // まずは用意した箱の先頭に文字コードを表すbomの中身を格納
            System.arraycopy(bom, 0, result, 0, bom.length);
            // 先ほど入れた239,187,191のあとに続くように文字コード化した文字を格納
            System.arraycopy(csvBytes, 0, result, bom.length, csvBytes.length);

            return result;

        } catch (Exception e) {
            // 万が一エラーが起きたら、システムエラー（RuntimeException）として報告する
            throw new RuntimeException("CSV作成中にエラーが発生しました", e);
        }
    }

    // CSVエスケープ処理（カンマや改行を含むデータ対策）
    private String escapeCsv(String text) {
        if (text == null) return "";
        if (text.contains(",") || text.contains("\n") || text.contains("\"")) {
        	// アウトプット時に正規の区切りを機能させるために、ダブルクォーテーションで囲んで返すことで、テキスト内のエスケープシーケンスなどを通常の文字として認識させる
            return "\"" + text.replace("\"", "\"\"") + "\"";
        }
        return text;
    }

    // 重要度の数値を文字に変換
    private String getImportanceText(int importance) {
        switch (importance) {
            case AppConst.TaskImportance.LOW: return "低";
            case AppConst.TaskImportance.MEDIUM: return "中";
            case AppConst.TaskImportance.HIGH: return "高";
            default: return "指定なし";
        }
    }
    
    // 重要度の数値を文字に変換
    private String getStatusText(int status) {
        switch (status) {
            case AppConst.TaskStatus.COMPLETED: return "完了";
            case AppConst.TaskStatus.EXPIRED: return "期限切れ";
            case AppConst.TaskStatus.EXPIRED_COMPLETED: return "期限切れ完了";
            default: return "未完了";
        }
    }

}
