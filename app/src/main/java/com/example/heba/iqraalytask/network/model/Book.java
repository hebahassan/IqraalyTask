package com.example.heba.iqraalytask.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Book implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("author_id")
    @Expose
    private String authorId;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("publisher_name")
    @Expose
    private String publisherName;
    @SerializedName("about")
    @Expose
    private String about;
    @SerializedName("is_paid")
    @Expose
    private String isPaid;
    @SerializedName("narrator_id")
    @Expose
    private String narratorId;
    @SerializedName("narrator_name")
    @Expose
    private String narratorName;
    @SerializedName("cover")
    @Expose
    private String cover;
    @SerializedName("episodes")
    @Expose
    private List<Episode> episodes = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(String isPaid) {
        this.isPaid = isPaid;
    }

    public String getNarratorId() {
        return narratorId;
    }

    public void setNarratorId(String narratorId) {
        this.narratorId = narratorId;
    }

    public String getNarratorName() {
        return narratorName;
    }

    public void setNarratorName(String narratorName) {
        this.narratorName = narratorName;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }
}
