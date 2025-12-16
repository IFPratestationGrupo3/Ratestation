package com.example.ratestation.Models;


import com.google.gson.annotations.SerializedName;

    public class Podcast {

        // iTunes JSON key: "collectionId" -> Java variable: id
        @SerializedName("collectionId")
        private int id;

        // iTunes JSON key: "trackName" -> Java variable: titulo
        @SerializedName("trackName")
        private String titulo;

        // iTunes JSON key: "artistName" -> Java variable: creador
        @SerializedName("artistName")
        private String creador;

        // iTunes JSON key: "artworkUrl600" -> Java variable: artworkPath
        @SerializedName("artworkUrl600")
        private String artworkPath;

        // iTunes JSON key: "primaryGenreName" -> Java variable: generoPrincipal
        @SerializedName("primaryGenreName")
        private String generoPrincipal;

        // iTunes JSON key: "feedUrl" -> Java variable: feedUrl
        @SerializedName("feedUrl")
        private String feedUrl;

        // ... Getters y Setters ...

        public int getId(){return id;}
        public String getTitulo(){return titulo;}
        public String getCreador(){return creador;}
        public String getArtworkPath(){return artworkPath;}
        public String getGeneroPrincipal(){return generoPrincipal;}
        public String getFeedUrl(){return feedUrl;}

        public void setId(int id){this.id= id;}
        public void setTitulo(String titulo){this.titulo=titulo;}
        public void setCreador(String creador){this.creador=creador;}
        public void setArtworkPath(String artworkPath){this.artworkPath=artworkPath;}
        public void setGeneroPrincipal(String generoPrincipal){this.generoPrincipal=generoPrincipal;}
        public void setFeedUrl(String feedUrl){this.feedUrl=feedUrl;}

    }





