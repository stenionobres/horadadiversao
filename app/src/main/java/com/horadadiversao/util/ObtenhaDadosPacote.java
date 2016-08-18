package com.horadadiversao.util;


import android.os.AsyncTask;

import com.horadadiversao.domain.PacoteDeTela;
import com.horadadiversao.domain.PacoteDeVideo;
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
import java.util.ArrayList;

/**
 * Created by PC-1451 on 25/07/2015.
 */
public class ObtenhaDadosPacote extends AsyncTask<String, Void, PacoteDeTela> {

    private PacoteDeTela pacoteDeTela =new PacoteDeTela();

    public interface ResultPacoteDeTela{
        void gotResult(PacoteDeTela pacoteDeTela);
    }

    private ResultPacoteDeTela myDados;

    //constructor
    public ObtenhaDadosPacote(String params,ResultPacoteDeTela callback){

        this.myDados = callback;
    }

    protected void onPostExecute(PacoteDeTela result) {

        myDados.gotResult(result);
    }

    @Override
    protected PacoteDeTela doInBackground(String... dados) {

        String resultado="";
        dados=dados;

        try {
            ObtenhaUrlDoVideo(dados[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this.pacoteDeTela;

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
            JSONObject jHoraDaDiversaoResources = trendLists.getJSONObject("HoraDaDiversaoResources");

            pacoteDeTela.setVisualizacoesDiarias(jHoraDaDiversaoResources.getInt("VisualizacoesDiarias"));
            pacoteDeTela.setCompartilhamento(jHoraDaDiversaoResources.getInt("Compartilhamento"));


            JSONObject jPacotesDeVideos = jHoraDaDiversaoResources.getJSONObject("PacotesDeVideos");

            //Lendo Agora os dados do Pacote

            JSONArray jArrayPacoteDeVideo = jPacotesDeVideos.getJSONArray("PacoteDeVideo");
            ArrayList<PacoteDeVideo> pacoteVideo = new ArrayList<PacoteDeVideo>();

            for (int i = 0; i < jArrayPacoteDeVideo.length(); i++) {
                PacoteDeVideo pacote = new PacoteDeVideo();
                JSONObject json_data = jArrayPacoteDeVideo.getJSONObject(i);

                JSONObject jVideos = json_data.getJSONObject("Videos");
                JSONArray jVideo = jVideos.getJSONArray("Video");

                ArrayList<Video> ListaIDSVideo= new ArrayList<Video>();

                for(int c = 0; c < jVideo.length(); c++) {

                    Video video = new Video();
                    JSONObject json_dataVideo = jVideo.getJSONObject(c);

                    video.setIdDoVideo(json_dataVideo.getString("IDDoVideo"));
                    ListaIDSVideo.add(video);
                }

                pacote.setNomeDoPacote(json_data.getString("NomeDoPacote"));
                pacote.setVideo(ListaIDSVideo);

                pacoteVideo.add(pacote);

            }

             pacoteDeTela.setPacoteDeVideo(pacoteVideo);

            ArrayList<PacoteDeVideo> tes = new ArrayList<PacoteDeVideo>();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

