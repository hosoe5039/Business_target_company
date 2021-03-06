package com.example.demo;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller

public class Surrounding_company {
	@PostMapping("map2525")
	String index(Model modelMap,@RequestParam(name = "lat")String lat,@RequestParam(name = "lng")String lng) throws IOException {

		//文字コード設定
		System.out.println("値の取得"+lat + " "+ lng);

		//JSPから現在地の緯度経度を取得する
		double imakoko_lat = Double.parseDouble(lat);
		double imakoko_ing = Double.parseDouble(lng);
		String Current_location = "{lat:"+ lat + ",lng:" + lng + "}";

		System.out.println("テスト:jspから現在地を取得出来ていることを確認する");
		System.out.println(Current_location);

		//JSPに現在地の緯度経度を転送する
		modelMap.addAttribute("Current_location", Current_location);

		//周辺の対象企業を取得する
		String marker_info = marker_info(imakoko_lat,imakoko_ing);

		if(marker_info.equals("該当なし")) {
			return "index";

		}else {
			//マーカ情報をJSPに転送
			modelMap.addAttribute("marker_info", marker_info);
			return "map";

		}

	}


	@Autowired
	private JdbcTemplate jdbcTemplate;


	private String marker_info(double imakoko_lat,double imakoko_ing) throws  IOException {



		String sql_1 = "select companys.company, companys.adress, places.latitude, places.longitude,";
		String sql_2 = "(6371 * ACOS(COS(RADIANS(?)) * COS(RADIANS(places.latitude)) * COS(RADIANS(places.longitude) - RADIANS(?))+ SIN(RADIANS(?)) * SIN(RADIANS(places.latitude)))) as DISTANCE";
		String sql_3 = " from companys inner join places ON companys.id = places.company_ID ORDER BY distance asc;";

		String sql = sql_1 + sql_2 + sql_3;

	    RowMapper<Company_data> rowMapper = new BeanPropertyRowMapper<Company_data>(Company_data.class);
	    List<Company_data> MapList = jdbcTemplate.query(sql,new PreparedStatementSetter() {
	        public void setValues(PreparedStatement ps) throws SQLException {
	            ps.setDouble(1,imakoko_lat);
	            ps.setDouble(2,imakoko_ing);
	            ps.setDouble(3,imakoko_lat);
	          }
	        },rowMapper);

	    System.out.println(MapList);

		//マーカー情報をリストで保存すらためのリスト
		List<String> marker_list = new ArrayList<>();

        for(Company_data company_data : MapList) {

        	String company_name = "name:" + "'"+ company_data.getCompany() + "'";
        	//String adress = "adress:" + "'"+ company_data.getAdress() + "'";
        	String lat_info = "lat:"+ company_data.getLatitude();
        	String lng_info = "lng:"+ company_data.getLongitude() ;
        	String icon = "icon: '/static/images/icon:pin66.jpg'";

        	String location_info =  "{" + String.join(",",company_name,lat_info,lng_info,icon) + "}";
        	marker_list.add(location_info);

        }

	    System.out.println(marker_list);
	    String marker_info = String.join(",", marker_list);
	    System.out.println(marker_info);
	    System.out.println("Hello");
		return marker_info;

	}
}