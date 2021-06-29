package Business_target_company_map;

import java.sql.Connection;
import java.sql.DriverManager;



public class Sql_conn {

	static Connection getDbConnection(){

		Connection conn = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Connectionの作成

			//本番環境
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/company_map?serverTimezone=UTC&useSSL=false",
					"root", "Onion#01");

			//テスト環境
//			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/youtubeinfo_test?serverTimezone=UTC&useSSL=false",
//					"root", "Onion#01");

		}catch (Exception e) {
			e.printStackTrace();
			// TODO ログにエラーメッセージを出力する
		}
		return conn;
	}
}
