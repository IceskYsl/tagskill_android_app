package com.morelap.tagskill.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.actionbarsherlock.view.MenuItem;
import com.morelap.tagskill.R;
import com.morelap.tagskill.event.DecouplingEvents;
import com.morelap.tagskill.event.HttpResponseEvents;
import com.morelap.tagskill.eventbus.BusProvider;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;
import com.squareup.otto.Subscribe;

public class ArticleListActivity extends SlidingFragmentActivity {

    private Fragment mContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);

        setContentView(R.layout.content_frame);

        // check if the content frame contains the menu frame
        if (findViewById(R.id.menu_frame) == null) {
            setBehindContentView(R.layout.menu_frame);
            getSlidingMenu().setSlidingEnabled(true);
            getSlidingMenu()
                    .setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
            // show home as up so we can toggle
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            // add a dummy view
            View v = new View(this);
            setBehindContentView(v);
            getSlidingMenu().setSlidingEnabled(false);
            getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }

        // set the Above View Fragment
        if (savedInstanceState != null) {
            mContent = getSupportFragmentManager().getFragment(
                    savedInstanceState, "mContent");
            if (mContent == null)
                mContent = new ArticleListFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, mContent).commit();
            // here we should concern about SlidingMenu's request and response
        }

        // set the Behind View Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_frame, new SlidingMenuFragment()).commit();

        // customize the SlidingMenu
        SlidingMenu sm = getSlidingMenu();
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindScrollScale(0.25f);
        sm.setFadeDegree(0.25f);
        BusProvider.getInstance().register(this);
    }
    
    @Override
    protected void onDestroy() {
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getSlidingMenu().toggle(true);
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Subscribe
    public void subscribeSlidingMenuCloseEvent(DecouplingEvents.SlidingMenuCloseEvent event) {
        if (getSlidingMenu().isMenuShowing()) {
            getSlidingMenu().toggle(true);
        }
    }
    
    @Subscribe
    public void subscribeArticleListResponseEvent(HttpResponseEvents.ArticleListResponseEvent event) {
        ArticleListFragment fragment = new ArticleListFragment();
        fragment.mArticleList = event.mArticleList;
        mContent = fragment;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, mContent).commit();
    }
    
    @Subscribe
    public void subscribeTitleChanged(DecouplingEvents.TitleChangedEvent event) {
        getSupportActionBar().setTitle(event.mTitle);
    }
}
