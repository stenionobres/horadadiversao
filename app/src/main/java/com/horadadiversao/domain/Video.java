package com.horadadiversao.domain;

import java.io.Serializable;

/**
 * Created by PC-1451 on 30/07/2015.
 */
public class Video implements Serializable{

    private String idDoVideo;
    private String urlDoVideo;
    private String nomeDoVideo;
    private String urlImagemDoVideo;

    public String getIdDoVideo() {
        return idDoVideo;
    }

    public void setIdDoVideo(String idDoVideo) {
        this.idDoVideo = idDoVideo;
    }

    public String getUrlDoVideo() {
        return urlDoVideo;
    }

    public void setUrlDoVideo(String urlDoVideo) {
        this.urlDoVideo = urlDoVideo;
    }

    public String getNomeDoVideo() {
        return nomeDoVideo;
    }

    public void setNomeDoVideo(String nomeDoVideo) {
        this.nomeDoVideo = nomeDoVideo;
    }

    public String getUrlImagemDoVideo() {
        return urlImagemDoVideo;
    }

    public void setUrlImagemDoVideo(String urlImagemDoVideo) {
        this.urlImagemDoVideo = urlImagemDoVideo;
    }
}
