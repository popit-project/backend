package com.popit.popitproject.common.kakaoAddress;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoAddressParseService {

    private static String GEOCODE_URL = "https://dapi.kakao.com/v2/local/search/address.json?query=";
    private static String GEOCODE_USER_INFO = "KakaoAK 9416084b8233f3961782ced9247e35ea";

    private static StringBuffer getStringBuffer(URL obj) throws IOException {
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", GEOCODE_USER_INFO);
        con.setRequestProperty("content-type", "application/json");
        con.setDoOutput(true);
        con.setUseCaches(false);
        con.setDefaultUseCaches(false);

        Charset charset = Charset.forName("UTF-8");
        BufferedReader in = new BufferedReader(
            new InputStreamReader(con.getInputStream(), charset));

        String inputLine;
        StringBuffer sb = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            sb.append(inputLine);
        }
        in.close();
        con.disconnect();
        return sb;
    }

    public String parseAddress(String storeAddress) {
        URL obj;
        try {
            String address = URLEncoder.encode(storeAddress, "UTF-8");
            obj = new URL(GEOCODE_URL + address);

            StringBuffer sb = getStringBuffer(obj);
            JSONArray documentsArray = getObjects(sb);

            if (documentsArray.length() > 0) {
                JSONObject documentObject = documentsArray.getJSONObject(0);
                JSONObject addressObject = documentObject.getJSONObject("address");

                String region3DepthName = addressObject.getString("region_3depth_name");

                return region3DepthName;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "잘못된 주소입니다.";
    }

    private static JSONArray getObjects(StringBuffer sb) {
        JSONObject jsonObject = new JSONObject(sb.toString());
        JSONArray documentsArray = jsonObject.getJSONArray("documents");
        return documentsArray;
    }

}