package com.horadadiversao.domain;

/**
 * Created by PC-1451 on 25/07/2015.
 */
public class DadosVideo {

    private String urlVideo;
    private String tituloVideo;
    private String idDoVideo;
    private String urlImagemDoVideo;

    public String getTituloVideo() {
        return tituloVideo;
    }

    public void setTituloVideo(String tituloVideo) {
        this.tituloVideo = tituloVideo;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    public String getIdDoVideo() {
        return idDoVideo;
    }

    public void setIdDoVideo(String idDoVideo) {
        this.idDoVideo = idDoVideo;
    }

    public String getUrlImagemDoVideo() { return urlImagemDoVideo;}

    public void setUrlImagemDoVideo(String urlImagemDoVideo) {this.urlImagemDoVideo = urlImagemDoVideo;}
}
