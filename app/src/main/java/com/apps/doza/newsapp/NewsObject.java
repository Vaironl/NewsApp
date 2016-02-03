package com.apps.doza.newsapp;

/**
 * Created by vaironl on 2/2/16.
 */
public class NewsObject {

    private String title, imageLink, author, description;

    public NewsObject(String _title, String _imageLink, String _author, String _description) {

        title = _title;
        imageLink = _imageLink;
        author = _author;
        description = _description;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }


}
