package com.horadadiversao.Video;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.avocarrot.androidsdk.AvocarrotInterstitial;
import com.horadadiversao.R;
import com.horadadiversao.dados.VisualizacaoBD;
import com.horadadiversao.domain.Video;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.horadadiversao.telas.VideosActivity;
import com.horadadiversao.util.BackgroundSoundService;
import com.jirbo.adcolony.*;

/**
 *
 * @author CASSIO SOUZA
 *
 */

public class ReprodutorVideos extends Activity implements ObtenhaDadosVideos.ResultDadosVideo,
        AdColonyAdAvailabilityListener, AdColonyAdListener{

    private VideoView myVideoView;
    private int position = 1;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;
    private int visualizacoes;
    AvocarrotInterstitial avocarrotInterstitial;
    private boolean mostrouPropaganda;

    final private String APP_ID  = "app450bb9671933445b96";
    final private String ZONE_ID = "vzed1f40c0e55246a3a9";
    private AdColonyVideoAd ad;
    private boolean AdColonyPronto;

    public static final String PREFS_NAME = "settings";
    private boolean opcaoBotaoStartStopMusic;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        Video video = new Video();

        if (params != null) {
            video = (Video) params.getSerializable("video");
            visualizacoes = (int) params.getInt("visualizacoes");
        }

        opcaoBotaoStartStopMusic = this.obterOpcaoBotaoStartStopMusic();

        mostrouPropaganda = false;
        avocarrotInterstitial =
                new AvocarrotInterstitial(
                        this,                     /* reference to your Activity */
                        "b22c7b39584d1b9c77e1da21f163546cf4815ae0", /* this is your Avocarrot API Key */
                        "6e599f5d88cf1bd21157a46dfdd343171527c3db" /* this is your Avocarrot Placement Key */
                );
        avocarrotInterstitial.setSandbox(true);
        avocarrotInterstitial.setLogger(true, "ALL");
        avocarrotInterstitial.loadAd();

        AdColonyPronto = false;
        AdColony.configure(this, "version:1.0,store:google", APP_ID, ZONE_ID);
        AdColony.addAdAvailabilityListener(this);
        ad = new AdColonyVideoAd( ZONE_ID );

        Video dados = new Video();
        dados.setIdDoVideo(video.getIdDoVideo());
        ProgressDialog progress = new ProgressDialog(ReprodutorVideos.this);

        progress.setMessage("Aguarde...");
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);

        progress.show();
        try {
            ObtenhaDadosVideos dadosVideo = new ObtenhaDadosVideos(dados, this);
            dadosVideo.execute(dados);
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Erro ao tentar abrir o video: " + e.getMessage() + "", Toast.LENGTH_SHORT).show();
            return;
        }

        progress.dismiss();

    }


    public void ReproduzirVideo(Video dadosVideo){

        // Obter o layout da view video_main.xml
        setContentView(R.layout.activity_reprodutor_videos);

        if (mediaControls == null) {
            mediaControls = new MediaController(ReprodutorVideos.this);
        }
        // Achar View no video_main.xml layout
        myVideoView = (VideoView) findViewById(R.id.video_view);

        // Criar a progressbar
        progressDialog = new ProgressDialog(ReprodutorVideos.this);
        // Abrir progressbar com o Titulo do video
        progressDialog.setTitle(dadosVideo.getNomeDoVideo());
        // Abrir progressbar com  a MSN
        progressDialog.setMessage("Aguarde...");

        progressDialog.setCancelable(false);
        // Show progressbar
        progressDialog.show();

        try {

            myVideoView.setMediaController(mediaControls);

            Uri uri=Uri.parse(dadosVideo.getUrlDoVideo());

            myVideoView.setVideoURI(uri);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        myVideoView.requestFocus();

        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Feche a barra de progresso, e reproduza o video
            public void onPrepared(MediaPlayer mp) {
                progressDialog.dismiss();
                myVideoView.seekTo(position);
                myVideoView.start();

            }
        });

        myVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                if (AdColonyPronto) {
                    ad.show();
                } else {
                    if(opcaoBotaoStartStopMusic){
                        BackgroundSoundService.startMusic();
                    }
                    finish();
                }
                mostrouPropaganda = true;
            }
        });



    }

    @Override
    public void onBackPressed() {

        try {

            if(mostrouPropaganda){
                if(opcaoBotaoStartStopMusic){
                    BackgroundSoundService.startMusic();
                }
                ReprodutorVideos.super.onBackPressed();
            }else{
                myVideoView.pause();
                avocarrotInterstitial.showAd();
                mostrouPropaganda = true;
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("Position", myVideoView.getCurrentPosition());

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position = savedInstanceState.getInt("Position");
        myVideoView.seekTo(position);
    }

    public void gotResult(Video dadosVideo)
    {
        if (this.videoEhValido(dadosVideo)){
            SimpleDateFormat formatador = new SimpleDateFormat("yyyyMMdd");
            VisualizacaoBD bd = new VisualizacaoBD(getBaseContext());
            int dataAAAAMMDD = Integer.parseInt(formatador.format(new Date()));
            bd.atualizarQuantidadeDeVisualizacao(dataAAAAMMDD, visualizacoes+1);
            ReproduzirVideo(dadosVideo);
        }else{
            new AlertDialog.Builder(this)
                    .setTitle("Hora Da Diversão")
                    .setMessage("Vídeo não disponível para reprodução!")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            ReprodutorVideos.super.onBackPressed();
                        }
                    }).create().show();
        }

    }

    private boolean videoEhValido(Video video){

        if(video.getUrlDoVideo() == null || video.getUrlDoVideo().isEmpty() ||
           video.getNomeDoVideo() == null || video.getNomeDoVideo().isEmpty()){
            return false;
        }

        return true;
    }

    @Override
    public void onAdColonyAdAvailabilityChange( final boolean available, String zone_id )
    {
        runOnUiThread( new Runnable()
        {
            @Override
            public void run()
            {
                if (available)
                {
                    AdColonyPronto = true;
                }
            }
        } );
    }

    @Override
    public void onAdColonyAdAttemptFinished( AdColonyAd ad )
    {
        //Can use the ad object to determine information about the ad attempt:
        //ad.shown();
        //ad.notShown();
        //ad.canceled();
        //ad.noFill();
        //ad.skipped();


    }


    @Override
    public void onAdColonyAdStarted( AdColonyAd ad )
    {
        //Called when the ad has started playing
    }


    @Override
    public void onResume()
    {
        super.onResume();
        AdColony.resume(this);
        if(mostrouPropaganda){
            if(opcaoBotaoStartStopMusic){
                BackgroundSoundService.startMusic();
            }
            finish();
        }
    }

    @Override
    public void onPause()
    {
        Log.i("Stenio: ", "entrou onPause");
        super.onPause();
        AdColony.pause();
    }

    private boolean obterOpcaoBotaoStartStopMusic(){
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        return preferences.getBoolean("executarMusica", true);
    }

}


