package db.mysql;

public class MySQLInit {
	public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String HOSTNAME = "localhost";
	private static final String PORT_NUM = "3306";  
	public static final String DB_NAME = "around_db";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "";
	public static final String URL = "jdbc:mysql://"
			+ HOSTNAME + ":" + PORT_NUM + "/" + DB_NAME
			+ "?user=" + USERNAME + "&password=" + PASSWORD
			+ "&autoReconnect=true&serverTimezone=UTC";

}
