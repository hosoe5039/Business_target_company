package Business_target_company_map;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/map2525")

public class Surrounding_company extends HttpServlet{

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{

		//文字コード設定
		request.setCharacterEncoding("UTF-8");

		//JSPから現在地の緯度経度を取得する
		double imakoko_lat = Double.parseDouble(request.getParameter("lat"));
		double imakoko_ing = Double.parseDouble(request.getParameter("lng"));
		String Current_location = imakoko_lat + "," + imakoko_ing;

		System.out.println("テスト:jspから現在地を取得出来ていることを確認する");
		System.out.println(Current_location);

		//JSPに現在地の緯度経度を転送する
		request.setAttribute("Current_location", Current_location);

		//周辺の対象企業を取得する
		String marker_info = marker_info(imakoko_lat,imakoko_ing);

		if(marker_info.equals("該当なし")) {
			request.getRequestDispatcher("/not_found.jsp").forward(request, response);

		}else {
			//マーカ情報をJSPに転送
			request.setAttribute("marker_info", marker_info);
			request.getRequestDispatcher("/map.jsp").forward(request, response);

		}

	}

	private String marker_info(double imakoko_lat,double imakoko_ing) throws  IOException {

		Connection conn = Sql_conn.getDbConnection();
		PreparedStatement company_adress_state =  null;
		ResultSet company_adress = null;

		String sql_1 = "select companys.company, companys.adress, place.latitude, place.longitude,";
		String sql_2 = "(6371 * ACOS(COS(RADIANS(?)) * COS(RADIANS(place.latitude)) * COS(RADIANS(place.longitude) - RADIANS(?))+ SIN(RADIANS(?)) * SIN(RADIANS(place.latitude)))) as DISTANCE";
		String sql_3 = " from companys inner join place ON companys.id = place.company_ID ORDER BY distance asc;";

		String sql = sql_1 + sql_2 + sql_3;


		//マーカー情報をリストで保存すらためのリスト
		List<String> marker_list = new ArrayList<>();
		//周辺1Km以内にある企業のカウント
		int targert_count = 0;

		try {
			company_adress_state = conn.prepareStatement(sql);

			company_adress_state.setDouble(1,imakoko_lat);
			company_adress_state.setDouble(2,imakoko_ing);
			company_adress_state.setDouble(3,imakoko_lat);

			company_adress = company_adress_state.executeQuery();

			System.out.println("テスト:SQL文は正しいか？");
			System.out.println(sql);


			while (company_adress.next()) {

				//初期化
				double distance = 2.718; //距離
				String company = ""; //会社名
				String adress = ""; //住所
				String lat = ""; //緯度
				String lng = "";

				//SQLから距離を取得する
				distance = company_adress.getDouble("DISTANCE");

				//距離が1Kmいないならば位置情報を取得する
				if(distance <= 1.0) {

					//周辺にあり企業数をカウント
					targert_count ++;

					//DBから会社名,住所,緯度,経度の取得
					company =company_adress.getString("company");
					adress = company_adress.getString("adress");
					lat = company_adress.getString("latitude");
					lng = company_adress.getString("longitude");

					//テスト1:会社名と住所,緯度、経度が正しいかどうか
					System.out.println("テスト1:会社名と住所、緯度、経度が正しいかどうか");
					System.out.println("会社名:" + company);
					System.out.println("住所:" + adress);
					System.out.println("緯度:" + lat);
					System.out.println("経度:" + lng);

					//mapにマーカーピンを打つための情報を纏めるmap.jspにてjavaScriptコードで使用します
					String company_name = "name:" + "'" + company + "'";
					String lat_info = "lat:" + lat ;
					String lng_info = "lng:" + lng ;
					String location_info = "{" + String.join(",",company_name,lat_info,lng_info) + "}";

					//マーカー情報をリストに保存
					marker_list.add(location_info);

					//テスト3：javaScriptの形式で纏めたマーカーピ情報が正しいか
					System.out.println("テスト3：javaScriptの形式で纏めたマーカーピ情報が正しいか");
					System.out.println(company_name);
					System.out.println(lat_info);
					System.out.println(lng_info);
					System.out.println(location_info);
				}
				else {
					//1.0km以上のならば処理を抜ける
					System.out.println("結果の取得を終了します");
					System.out.println(company_adress.getString("company"));
					System.out.println(company_adress.getString("adress"));
					System.out.println(company_adress.getString("latitude"));
					System.out.println(company_adress.getString("longitude"));
					System.out.println("現在地からの距離" + distance + "Km");
					break;
				}
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		if(targert_count > 0) {
			//マーカ情報の一覧を一つの文字列に纏める
			String marker_info = String.join(",", marker_list);
			//テスト4:マーカ情報の一覧が正しいことの確認
			System.out.println(marker_info);
			return marker_info;

		}else {
			return "該当なし";
		}
	}
}