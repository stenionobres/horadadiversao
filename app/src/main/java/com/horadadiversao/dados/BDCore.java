package com.horadadiversao.dados;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by $tenio nobre$ on 03/10/2015.
 */
public class BDCore extends SQLiteOpenHelper {

    public static final String NOME_BD = "HORA_DIVERSAO";
    public static final int VERSAO_BD = 1;


    public BDCore(Context context){
        super(context, NOME_BD, null, VERSAO_BD);
    }



    @Override
    public void onCreate(SQLiteDatabase bd) {
        bd.execSQL("create table visualizacao (data int not null, quant_videos_vistos int not null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase bd, int i, int i1) {
        bd.execSQL("drop table visualizacao;");
        onCreate(bd);
    }
}
