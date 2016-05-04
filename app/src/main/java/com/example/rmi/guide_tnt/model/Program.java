package com.example.rmi.guide_tnt.model;

import android.graphics.drawable.Drawable;

import java.util.Date;

/**
 * Created by Rémi on 13/04/2016.
 */
public class Program {

    private int id;
    protected String title;
    protected String description;
    protected Date startDate;
    protected Date endDate;
    protected String imageURL;
    protected Drawable imageThumb;
    protected String review;
    protected String season;
    protected String episode;
    protected Integer rating;
    protected String category;

    public Program(int id, String title, String description, Date startDate, Date endDate, String imageURL, Drawable thumb, String review, String season, String episode, Integer rating, String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imageURL = imageURL;
        this.review = review;
        this.season = season;
        this.episode = episode;
        this.rating = rating;
        this.category = category;
        this.imageThumb = thumb;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getReview() {
        return review;
    }

    public String getSeason() {
        return season;
    }

    public String getEpisode() {
        return episode;
    }

    public Integer getRating() {
        return rating;
    }

    public String getCategory() {
        return category;
    }

    public Drawable getImageThumb() {
        return imageThumb;
    }
}
