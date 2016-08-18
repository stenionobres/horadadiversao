package com.horadadiversao.menu;

import android.os.AsyncTask;

import com.horadadiversao.domain.PacoteDeTela;
import com.horadadiversao.domain.DadosVideo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by PC-1451 on 30/07/2015.
 */
public class AuxMenu extends AsyncTask<DadosVideo, Void, DadosVideo> {


    private final String URL_CONNECTION = "https://dl.dropboxusercontent.com/s/21roz5cm3qio9vh/hora_da_diversao.json?dl=0";


    @Override
    protected DadosVideo doInBackground(DadosVideo... params) {

       try {
            ObtenhaUrlDoVideo(URL_CONNECTION);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void ObtenhaUrlDoVideo (String CONNECTION_URL) throws IOException {

        InputStream is = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(CONNECTION_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            conn.getResponseCode();

            is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Le cada linha da resposta ate o final
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }


            LerJson(sb.toString());

        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void LerJson(String jsonString){

       try {

           JSONObject trendLists = new JSONObject(jsonString);

           PacoteDeTela pacoteDeTela = new PacoteDeTela();


            JSONObject jPacotesDeVideos = trendLists.getJSONObject("ImagemTelaInicial");



           // JSONObject jFiles = jRequest.getJSONObject("files");
           // JSONObject jH264 = jFiles.getJSONObject("h264");
          //  JSONObject jSd = jH264.getJSONObject("sd");
           // JSONObject jVideo = trendLists.getJSONObject("video");

          //  dadosVideo.setUrlVideo(jSd.getString("url"));
          //  dadosVideo.setTituloVideo(jVideo.getString("title"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
