package com.popit.popitproject.store.exception;

import com.popit.popitproject.store.entity.StoreEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

@Configuration
public class KakaoAddressChange {
    private static String GEOCODE_URL="https://dapi.kakao.com/v2/local/search/address.json?query=";

    @Value("${kakao.restapi.key}")
    private static String GEOCODE_USER_INFO = "KakaoAK cecd764535a3df2e33f0e29c79e78ba8";

//    @Value("${kakao.restapi.key}")
//    private static String GEOCODE_USER_INFO;

    public static StoreEntity addressChange(String storeAddress) throws IOException {
        URL obj;

        try{

            String address = URLEncoder.encode(storeAddress, "UTF-8");

            obj = new URL(GEOCODE_URL+address);

            HttpURLConnection con = (HttpURLConnection)obj.openConnection();


            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization",GEOCODE_USER_INFO);
            con.setRequestProperty("content-type","application/json");
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setDefaultUseCaches(false);

            Charset charset = Charset.forName("UTF-8");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), charset));

            String inputLine;
            StringBuffer sb = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            in.close();
            con.disconnect();

            JSONObject jObject = new JSONObject(sb.toString());

            JSONArray jArray = jObject.getJSONArray("documents");

            for (int i = 0; i < jArray.length(); i++) {
                StoreEntity dto = new StoreEntity();
                JSONObject obj1 = jArray.getJSONObject(i);
                Double x = obj1.getDouble("x");
                dto.setX(x);
                Double y = obj1.getDouble("y");
                dto.setY(y);
                System.out.println("x: " + x);
                System.out.println("y: " + y);
                System.out.println();

                return dto;

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
}
