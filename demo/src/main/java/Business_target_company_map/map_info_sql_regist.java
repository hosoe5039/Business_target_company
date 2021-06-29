package Business_target_company_map;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/map222lo")

public class map_info_sql_regist extends HttpServlet{

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{

		//文字コード設定
		request.setCharacterEncoding("UTF-8");

		//JSPから現在地の緯度経度を取得する
		String company = request.getParameter("company");
		String adress = request.getParameter("adress");
		double regist_lat = Double.parseDouble(request.getParameter("lat"));
		double regist_ing = Double.parseDouble(request.getParameter("lng"));

		map_info_regist(company,adress,regist_lat,regist_ing);
		request.getRequestDispatcher("/imakoko.jsp").forward(request, response);

	}

	private void map_info_regist(String company, String adress, double lat , double lng) {

		Connection conn = Sql_conn.getDbConnection();

		//テーブルcompanysのinsert用
		PreparedStatement companys_insert =  null;
		ResultSet companys_insert_res = null;

		//テーブルplacesのinsert用
		PreparedStatement places_insert =  null;

		int autoIncrementKey = 0;

		//insert定義
		String companys_table_insert_sql = "insert into companys(company,adress) values(?,?);";
		String places_table_insert_sql = "insert into places(company_id,latitude,longitude) values(?,?,?);";

		try {
			//companysテーブルにinsert処理実行
			companys_insert = conn.prepareStatement(companys_table_insert_sql,java.sql.Statement.RETURN_GENERATED_KEYS);
			companys_insert.setString(1,company);
			companys_insert.setString(2,adress);
			companys_insert.executeUpdate();

			companys_insert_res = companys_insert.getGeneratedKeys();

			//companysテーブルの登録時に採番されたidの取得→placesテーブルのcompany_idとしてinsertする
		if(companys_insert_res.next()){
			autoIncrementKey = companys_insert_res.getInt(1);
		}
			System.out.println(autoIncrementKey);

			//placesテーブルにinsert処理実行
			places_insert = conn.prepareStatement(places_table_insert_sql);
			places_insert.setInt(1,autoIncrementKey);
			places_insert.setDouble(2,lat);
			places_insert.setDouble(3,lng);
			places_insert.executeUpdate();

		} catch (SQLException e1) {
			e1.printStackTrace();
		}


	}
}