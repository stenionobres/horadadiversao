package com.horadadiversao.dados;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by $tenio nobre$ on 03/10/2015.
 */
public class VisualizacaoBD {

    private SQLiteDatabase bd;


    public VisualizacaoBD(Context context){
        BDCore auxBd = new BDCore(context);
        bd = auxBd.getWritableDatabase();
    }

    public int buscarQuantidadeDeVisualizacao(int data){
        int quantidade = 0;
        String[] coluna = new String[]{"quant_videos_vistos"};

        Cursor cursor  = bd.query("visualizacao", coluna, "data=?", new String[]{Integer.toString(data)} , null, null, null);

        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            quantidade = cursor.getInt(0);
        }else{
            this.inserirNovaEntradaDeVisualizacao();
            quantidade = 0;
        }

        return quantidade;
    }

    private void inserirNovaEntradaDeVisualizacao(){
        ContentValues valores = new ContentValues();
        SimpleDateFormat formatador = new SimpleDateFormat("yyyyMMdd");

        valores.put("data", formatador.format(new Date()));
        valores.put("quant_videos_vistos", 0);

        bd.insert("visualizacao", null, valores);

    }


    public void atualizarQuantidadeDeVisualizacao(int data, int numeroDeVisualizacao){
        String[] coluna = new String[]{"quant_videos_vistos"};

        ContentValues valores = new ContentValues();
        valores.put("quant_videos_vistos", numeroDeVisualizacao);

        bd.update("visualizacao", valores, "data=?", new String[]{Integer.toString(data)});

    }



}
