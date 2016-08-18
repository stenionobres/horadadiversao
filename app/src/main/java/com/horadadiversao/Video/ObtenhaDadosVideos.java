package com.horadadiversao.Video;

import android.os.AsyncTask;

import com.horadadiversao.domain.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by CASSIO SOUZA on 25/07/2015.
 */

public class ObtenhaDadosVideos extends AsyncTask<Video, Void, Video> {

    private Video video =new Video();

    public interface ResultDadosVideo
    {
        void gotResult(Video video);
    }

    private ResultDadosVideo myDados;

    //constructor
    public ObtenhaDadosVideos(Video params,ResultDadosVideo callback)
    {

        this.myDados = callback;
    }

    protected void onPostExecute(Video result)
    {

        myDados.gotResult(result);
    }

    @Override
    protected Video doInBackground(Video... dados) {

        String resultado="";
        dados=dados;


        try {
             ObtenhaUrlDoVideo(MonteUrl(dados[0].getIdDoVideo()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this.video;

    }

    private String MonteUrl(String IDVideo){

        return new StringBuilder("https://player.vimeo.com/video/").append(IDVideo).append("/config").toString();
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
            JSONObject jRequest = trendLists.getJSONObject("request");
            JSONObject jFiles = jRequest.getJSONObject("files");
            JSONArray jArrayprogressive = jFiles.getJSONArray("progressive");
            JSONObject jProgressive = jArrayprogressive.getJSONObject(0);
            JSONObject jVideo = trendLists.getJSONObject("video");
            video.setUrlDoVideo(jProgressive.getString("url"));
            video.setNomeDoVideo(jVideo.getString("title"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


  }
