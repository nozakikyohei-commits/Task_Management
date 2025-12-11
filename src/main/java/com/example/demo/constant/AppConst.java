package com.example.demo.constant;

public class AppConst {
	
	public static class Url {
		public static final String DEFAULT = "/";
		public static final String LOGIN = "/login";
		public static final String LOGOUT = "/logout";
		public static final String CREATE_USER = "/create-user";
		public static final String EDIT_USER = "/edit-user";
		public static final String CREATE_TASK = "/create-task";
		public static final String EDIT_TASK = "/edit-task";
		public static final String VIEW_TASKS = "/view-tasks";
		public static final String VIEW_TASKS_CALENDAR = "/view-tasks-calendar";
		public static final String VIEW_TASKS_MEMOS = "/view-tasks-memos";
		public static final String VIEW_ALL_TASKS = "/view-all-tasks";
		public static final String VIEW_ALL_USERS = "/view-all-users";
	}
	
	public static class View {
		public static final String LOGIN = "login";
		public static final String CREATE_USER = "create-user";
		public static final String EDIT_USER = "edit-user";
		public static final String CREATE_TASK = "create-task";
		public static final String EDIT_TASK = "edit-task";
		public static final String VIEW_TASKS = "view-tasks";
		public static final String VIEW_TASKS_CALENDAR = "view-tasks-calendar";
		public static final String VIEW_TASKS_MEMOS = "view-tasks-memos";
		public static final String VIEW_ALL_TASKS = "view-all-tasks";
		public static final String VIEW_ALL_USERS = "view-all-users";
	}
	
	public static class UserRole {
        public static final int GENERAL = 0;
        public static final int ADMIN = 1;
    }
	
	public static class TaskImportance {
        public static final int LOW = 1;
        public static final int MEDIUM = 2;
        public static final int HIGH = 3;
    }
	
	public static class TaskStatus {
        public static final int INCOMPLETE = 0;
        public static final int COMPLETED = 1;
        public static final int EXPIRED = 2;
        public static final int EXPIRED_COMPLETED = 3;
    }

}
