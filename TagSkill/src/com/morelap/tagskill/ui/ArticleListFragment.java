package com.morelap.tagskill.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.morelap.tagskill.R;
import com.morelap.tagskill.eventbus.BusProvider;
import com.morelap.tagskill.model.ArticleList;
import com.morelap.tagskill.model.ArticleList.Article;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ArticleListFragment extends Fragment {
    
    private ListView mListView;
    private ArticleListAdapter mArticleListAdapter;
    ArticleList mArticleList;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mListView = (ListView) inflater.inflate(R.layout.article_list, container, false);
        mArticleListAdapter = new ArticleListAdapter(getActivity(), new ArrayList<ArticleList.Article>());
        if (mArticleList != null) {
            Article[] articles = mArticleList.items;
            if (articles != null) {
                mArticleListAdapter.addArticleList(Arrays.asList(articles));
            }
        }
        mListView.setAdapter(mArticleListAdapter);
        BusProvider.getInstance().register(this);
        return mListView;
    }
    
    @Override
    public void onDestroyView() {
        BusProvider.getInstance().unregister(this);
        super.onDestroyView();
    }
    
    private static class ArticleListAdapter extends BaseAdapter {

        LayoutInflater mInflater;
        List<Article> mArticles;
        
        ArticleListAdapter(Context context, List<Article> articles) {
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mArticles = articles;
        }
        
        @Override
        public int getCount() {
            return mArticles.size();
        }

        @Override
        public Article getItem(int position) {
            return mArticles.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Article article = getItem(position);
            View view;
            ViewHolder holder = null;
            if (convertView == null) {
                view = mInflater.inflate(R.layout.article_list_item, parent, false);
                holder = new ViewHolder();
                holder.mTitle = (TextView) view.findViewById(R.id.article_title);
                holder.mAuthor = (TextView) view.findViewById(R.id.article_author);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) convertView.getTag();
            }
            holder.mTitle.setText(article.title);
            holder.mAuthor.setText(article.author);
            return view;
        }
        
        void addArticleList(List<Article> articles) {
            mArticles.addAll(articles);
        }
        
        private class ViewHolder {
            TextView mTitle;
            TextView mAuthor;
        }
    }
}
