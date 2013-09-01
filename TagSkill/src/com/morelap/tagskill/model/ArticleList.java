package com.morelap.tagskill.model;

public class ArticleList {

    public boolean status;
    public boolean background;
    public long expires;
    public Article[] items;
    public MoreUri more_uri;

    public static class Article {
        public int id;
        public String title;
        public String author;
        public String url;
        public String created_at;
        public String website;
        public String image_url;
        public String is_block;
        public String is_en;
        public String lede;
    }
    
    public static class MoreUri {
        public int auth;
        public String label;
        public String title;
        public String uri;
    }
}
