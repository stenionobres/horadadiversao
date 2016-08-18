package com.horadadiversao.domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by PC-1451 on 30/07/2015.
 */
public class PacoteDeVideo implements Serializable{

    private String NomeDoPacote;
    private ArrayList<Video> video;


    public String getNomeDoPacote() {return NomeDoPacote;}

    public void setNomeDoPacote(String nomeDoPacote) {NomeDoPacote = nomeDoPacote;}

    public ArrayList<Video> getVideo() {
        return video;
    }

    public void setVideo(ArrayList<Video> video) {this.video = video;}


}
