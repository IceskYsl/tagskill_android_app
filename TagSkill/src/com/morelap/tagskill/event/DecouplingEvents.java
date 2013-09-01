package com.morelap.tagskill.event;

public class DecouplingEvents {

    private DecouplingEvents() {
    }

    public static class SlidingMenuCloseEvent {
    }

    public static class TitleChangedEvent {
        public String mTitle;

        public TitleChangedEvent(String title) {
            mTitle = title;
        }
    }

}
