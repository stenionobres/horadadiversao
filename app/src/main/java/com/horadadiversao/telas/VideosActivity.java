package com.horadadiversao.telas;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.horadadiversao.Video.ObtenhaDadosVideos;
import com.horadadiversao.dados.VisualizacaoBD;
import com.horadadiversao.domain.Video;
import com.horadadiversao.util.BackgroundSoundService;
import com.horadadiversao.util.CustomGridViewAdapter;
import com.horadadiversao.util.Item;
import com.horadadiversao.R;
import com.horadadiversao.Video.ReprodutorVideos;
import com.horadadiversao.util.MusicActivityControl;
import com.horadadiversao.util.ObtenhaDadosPacote;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by $tenio nobre$ on 02/09/2015.
 */
public class VideosActivity extends Activity {
    GridView gridView;
    ArrayList<Item> gridArray = new ArrayList<Item>();
    CustomGridViewAdapter customGridAdapter;
    private ArrayList<Video> videos = new ArrayList<Video>();
    private int visualizacoesDiarias;
    public static final String PREFS_NAME = "settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(this.obterOpcaoBotaoStartStopMusic() == true){
            BackgroundSoundService.startMusic();
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_videos);

        //set grid view item
        Bitmap homeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.notfound);

        Intent intent = getIntent();
        Bundle params = intent.getExtras();

        if(params!=null){
            videos = (ArrayList<Video>) params.getSerializable("videos");
            visualizacoesDiarias = (int) params.getInt("visualizacoesDiarias");
        }

      //  ObtenhaDados(videos);


        for(Video video : videos){



            gridArray.add(new Item(homeIcon));
        }


        gridView = (GridView) findViewById(R.id.gridViewVideos);
        customGridAdapter = new CustomGridViewAdapter(this, R.layout.row_grid, gridArray);
        gridView.setAdapter(customGridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(VideosActivity.this, "Click" + position + R.layout.row_grid, Toast.LENGTH_SHORT).show();

                SimpleDateFormat formatador = new SimpleDateFormat("yyyyMMdd");
                VisualizacaoBD bd = new VisualizacaoBD(getBaseContext());
                int dataAAAAMMDD = Integer.parseInt(formatador.format(new Date()));
                int visualizacoesBd = bd.buscarQuantidadeDeVisualizacao(dataAAAAMMDD);

                if (visualizacoesBd < visualizacoesDiarias) {
                    Toast.makeText(getBaseContext(), "Você ainda pode assistir " + (visualizacoesDiarias - (visualizacoesBd + 1)) + " vídeo(s) hoje!",
                            Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(VideosActivity.this, ReprodutorVideos.class);

                    Bundle params = new Bundle();
                    Video video = videos.get(position);

                    params.putSerializable("video", video);
                    params.putInt("visualizacoes", visualizacoesBd);
                    i.putExtras(params);

                    BackgroundSoundService.pauseMusic();
                    startActivity(i);

                } else {
                    Toast.makeText(getBaseContext(), "Você já atingiu o número máximo de " + visualizacoesDiarias + " visualizações por dia!",
                            Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    public void onResume() {
        super.onResume();
        if(this.obterOpcaoBotaoStartStopMusic() == true){
            BackgroundSoundService.startMusic();
        }
        Log.i("Musica: ", "Iniciou musica!");
    }


    @Override
    protected void onStop() {
        super.onStop();
        MusicActivityControl.stopMusic(getSystemService(Context.ACTIVITY_SERVICE), getPackageName().toString());
        Log.i("Musica: ", "Parou musica!");
    }


    private boolean obterOpcaoBotaoStartStopMusic(){
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        return preferences.getBoolean("executarMusica", true);
    }


    private void ObtenhaDados(ArrayList<Video> videos){

      /*  try{
            ProgressDialog progress = new ProgressDialog(VideosActivity.this);

            progress.setMessage("Aguarde...");
            progress.setCanceledOnTouchOutside(false);
            progress.setCancelable(false);

            progress.show();
            try {
                ObtenhaDadosVideos dadosVideo = new ObtenhaDadosVideos(dados, this);
                dadosVideo.execute(dados);
            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Erro ao tentar abrir o video: " + e.getMessage() + "",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            progress.dismiss();

        }
        catch(Exception e){
            Toast.makeText(getBaseContext(), "Erro ao tentar abrir o video: " + e.getMessage() + "",
                    Toast.LENGTH_SHORT).show();
            return;
        } */
    }


    private void MonteDadosNaTela(){


       /* for(Video video : videos){



            gridArray.add(new Item(homeIcon));
        }


        gridView = (GridView) findViewById(R.id.gridViewVideos);
        customGridAdapter = new CustomGridViewAdapter(this, R.layout.row_grid, gridArray);
        gridView.setAdapter(customGridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(VideosActivity.this, "Click" + position + R.layout.row_grid, Toast.LENGTH_SHORT).show();

                SimpleDateFormat formatador = new SimpleDateFormat("yyyyMMdd");
                VisualizacaoBD bd = new VisualizacaoBD(getBaseContext());
                int dataAAAAMMDD = Integer.parseInt(formatador.format(new Date()));
                int visualizacoesBd = bd.buscarQuantidadeDeVisualizacao(dataAAAAMMDD);

                if(visualizacoesBd < visualizacoesDiarias){
                    Toast.makeText(getBaseContext(), "Você ainda pode assistir " + (visualizacoesDiarias - (visualizacoesBd+1)) + " vídeo(s) hoje!",
                            Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(VideosActivity.this, ReprodutorVideos.class);

                    Bundle params = new Bundle();
                    Video video = videos.get(position);

                    params.putSerializable("video", video);
                    params.putInt("visualizacoes", visualizacoesBd);
                    i.putExtras(params);

                    startActivity(i);

                }else{
                    Toast.makeText(getBaseContext(), "Você já atingiu o número máximo de " + visualizacoesDiarias  + " visualizações por dia!",
                            Toast.LENGTH_SHORT).show();

                }

            }
        });*/
    }


}
