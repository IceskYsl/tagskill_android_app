package com.morelap.tagskill.event;

import com.morelap.tagskill.model.ArticleList;
import com.morelap.tagskill.model.Slide;
import com.morelap.tagskill.model.User;

public class HttpResponseEvents {

    private HttpResponseEvents() {
    }

    public static abstract class HttpResponseEvent {

    }

    public static class SlideResponseEvent extends HttpResponseEvent {
        public Slide mSlide;
        public SlideResponseEvent(Slide slide) {
            mSlide = slide;
        }
    }
    
    public static class ArticleListResponseEvent extends HttpResponseEvent {
        public ArticleList mArticleList;
        public ArticleListResponseEvent(ArticleList articleList) {
            mArticleList = articleList;
        }
    }
    
    public static class UserInfoResponseEvent extends HttpResponseEvent {
        public User mUser;
        public UserInfoResponseEvent(User user) {
            mUser = user;
        }
    }
}
