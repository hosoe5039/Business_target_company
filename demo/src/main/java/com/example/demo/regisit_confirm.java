package com.example.demo;


import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

@Controller

public class regisit_confirm{


	@PostMapping("map000122")

	//登録する住所、会社名の取得
	String index(Model modelMap,@RequestParam(name = "company")String company,@RequestParam(name = "adress")String adress) throws IOException {


		try {
			//位置情報の取得メソッドの呼び出し
			place_info(company,adress, modelMap);

		} catch (ApiException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//登録確認ページへ遷移する
		return "regisit_confirm";
	}

	private void place_info(String company,String adress, Model modelMap) throws ApiException, InterruptedException, IOException {

		GeoApiContext context = new GeoApiContext.Builder()
				.apiKey("AIzaSyCbfxc6tVLzjs8YmrdB59CMT5Iy3ZEXm0E")
				.build();

		//APIを使用して位置情報の取得
		GeocodingResult[] results = GeocodingApi.geocode(context, adress).await();
		LatLng latLng = results[0].geometry.location;

		System.out.println("テスト2：位置情報が取得できたか");
		System.out.println(company + "の緯度 : " + latLng.lat);
		System.out.println(company +"の経度 : " + latLng.lng);

		//緯度経度を文字列の変数に代入
		String lat = String.valueOf(latLng.lat);
		String lng = String.valueOf(latLng.lng);
		String marker_info = "{lat:" + lat + ", lng:" + lng + "}";

		//登録する企業の位置情報をhtmlへ送る
		modelMap.addAttribute("company",company);
		modelMap.addAttribute("adress",adress);
		modelMap.addAttribute("lat",lat);
		modelMap.addAttribute("lng",lng);
		modelMap.addAttribute("marker_info",marker_info);

	}
}