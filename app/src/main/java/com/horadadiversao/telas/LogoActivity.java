package com.horadadiversao.telas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.horadadiversao.R;
import com.horadadiversao.domain.PacoteDeTela;
import com.horadadiversao.domain.PacoteDeVideo;
import com.horadadiversao.util.ObtenhaDadosPacote;

import java.util.ArrayList;


public class LogoActivity extends Activity implements ObtenhaDadosPacote.ResultPacoteDeTela {

    private String URL_CONNECTION = "https://dl.dropboxusercontent.com/s/21roz5cm3qio9vh/hora_da_diversao.json?dl=0";
    private ArrayList<PacoteDeVideo> pacotes = new ArrayList<PacoteDeVideo>();
    private PacoteDeTela pacoteTela= new PacoteDeTela();
    private ProgressBar progressBar;
    private int progressStatus = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressStatus += 5;
        progressBar.setProgress(progressStatus);

        ObtenhaDados();

    }


    private void ObtenhaDados(){

        try{

            ObtenhaDadosPacote pacoteDadosVideo = new ObtenhaDadosPacote(URL_CONNECTION,this);
            progressStatus += 20;
            progressBar.setProgress(progressStatus);
            pacoteDadosVideo.execute(URL_CONNECTION);

        }
        catch(Exception e){
            Toast.makeText(getBaseContext(), "Erro ao tentar abrir o video: " + e.getMessage() + "",
                    Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    public void gotResult(PacoteDeTela pacoteDeTela) {

        progressStatus += 20;
        progressBar.setProgress(progressStatus);

        pacoteTela= pacoteDeTela;

        Intent i = new Intent(LogoActivity.this, MainActivity.class);

        Bundle params = new Bundle();
        params.putSerializable("dadosTela", pacoteDeTela);
        i.putExtras(params);

        progressStatus = 100;
        progressBar.setProgress(progressStatus);
        startActivity(i);


    }
}
