package Business_target_company_map;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

@WebServlet("/map2122")

public class regisit_confirm extends HttpServlet{

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{

		//文字コード設定
		request.setCharacterEncoding("UTF-8");
		//jspから住所、会社名の取得
		String company = request.getParameter("company");
		String adress = request.getParameter("adress");

		Map<String, String> place;
		try {
			place = place_info(company,adress);
			System.out.println(place);

			//マーカ情報をJSPに転送
			request.setAttribute("place", place);


		} catch (ApiException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		request.getRequestDispatcher("/regisit_confirm.jsp").forward(request, response);

	}

	private Map<String, String> place_info(String company,String adress) throws ApiException, InterruptedException, IOException {

		GeoApiContext context = new GeoApiContext.Builder()
				.apiKey("AIzaSyCbfxc6tVLzjs8YmrdB59CMT5Iy3ZEXm0E")
				.build();

		//APIを使用して位置情報の取得
		GeocodingResult[] results = GeocodingApi.geocode(context, adress).await();
		LatLng latLng = results[0].geometry.location;

		System.out.println("テスト2：位置情報が取得できたか");
		System.out.println(company + "の緯度 : " + latLng.lat);
		System.out.println(company +"の経度 : " + latLng.lng);

		Map<String, String> place = new HashMap<String, String>();

		//緯度経度を文字列の変数に代入
		String lat = String.valueOf(latLng.lat);
		String lng = String.valueOf(latLng.lng);

		place.put("company",company);
		place.put("adress",adress);
		place.put("lat",lat);
		place.put("lng",lng);

		return place;
	}
}