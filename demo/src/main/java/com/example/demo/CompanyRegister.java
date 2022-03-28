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

public class CompanyRegister {
	@PostMapping("map2525")
	String index(Model modelMap,@RequestParam(name = "company")String company,@RequestParam(name = "adress")String adress,@RequestParam(name = "lat")String lat,@RequestParam(name = "lng")String lng) throws IOException {


		//HTMLからの値が取得できていることの確認
		System.out.println(company);
		System.out.println(adress);
		System.out.println("緯度：" + lat);
		System.out.println("経度：" + lng);

		//周辺の対象企業を取得する
		boolean marker_info = Register(company,adress,lat,lng);

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


	private boolean Register(String company,String adress,String lat,String lng) throws  IOException {



		String company_insert_sql = "";
		String place_insert_sql  = "";

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