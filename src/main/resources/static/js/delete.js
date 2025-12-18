document.addEventListener('DOMContentLoaded', function() {
	
	const deleteTask = document.querySelector('.btn-danger');
	
	deleteTask.addEventListener('click', function() {
		return confirm("このタスクを削除しますか？");
	})
	
});