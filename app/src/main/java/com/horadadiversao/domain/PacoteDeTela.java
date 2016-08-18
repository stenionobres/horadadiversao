package com.horadadiversao.domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by PC-1451 on 30/07/2015.
 */
public class PacoteDeTela implements Serializable {

    private int visualizacoesDiarias;
    private int compartilhamento;

    public int getCompartilhamento() {
        return compartilhamento;
    }

    public void setCompartilhamento(int compartilhamento) {
        this.compartilhamento = compartilhamento;
    }


    private ArrayList<PacoteDeVideo> pacotesDeVideos;


    public int getVisualizacoesDiarias() {
        return visualizacoesDiarias;
    }

    public void setVisualizacoesDiarias(int visualizacoesDiarias) {
        this.visualizacoesDiarias = visualizacoesDiarias;
    }

    public ArrayList<PacoteDeVideo> getPacoteDeVideo() {
        return pacotesDeVideos;
    }

    public void setPacoteDeVideo(ArrayList<PacoteDeVideo> pacoteDeVideo) {
        this.pacotesDeVideos = pacoteDeVideo;
    }

}
