package com.morelap.tagskill.model;

public class Slide {

    public boolean background;
    public boolean status;
    public long expires;
    public Data[] data;
    
    public static class Data {

        public String title;
        public int sort_id;
        public SlideItem[] items;

        public static class SlideItem {
            public String label;
            public String title;
            public int auth;
            public String uri;
            public int count;
            public boolean auto_loading;

            @Override
            public String toString() {
                return "label = " + label + "; title = " + title + "; auth = "
                        + auth + "; uri = " + uri + "; count = " + count
                        + "; auto_loading = " + auto_loading;
            }
        }

        @Override
        public String toString() {
            return "title = " + title + "; sort_id = " + sort_id + ";";
        }
    }
}
