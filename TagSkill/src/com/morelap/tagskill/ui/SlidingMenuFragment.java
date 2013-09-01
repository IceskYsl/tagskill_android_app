package com.morelap.tagskill.ui;

import java.util.ArrayList;
import java.util.List;

import com.loopj.android.http.RequestParams;
import com.morelap.tagskill.R;
import com.morelap.tagskill.event.DecouplingEvents;
import com.morelap.tagskill.event.HttpRequestEvents;
import com.morelap.tagskill.event.HttpResponseEvents;
import com.morelap.tagskill.eventbus.BusProvider;
import com.morelap.tagskill.model.Slide;
import com.morelap.tagskill.model.Slide.Data.SlideItem;
import com.squareup.otto.Subscribe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SlidingMenuFragment extends Fragment implements
        OnItemClickListener, View.OnClickListener {

    private ListView mListView;

    private TextView mAccountName;

    private Button mBindAccount;

    private Header mCurHeader;

    private String mToken;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sliding_menu, null);
        mListView = (ListView) v.findViewById(android.R.id.list);
        mListView.setOnItemClickListener(this);
        mAccountName = (TextView) v.findViewById(R.id.account_username);
        mBindAccount = (Button) v.findViewById(R.id.bind_account);
        mBindAccount.setOnClickListener(this);
        Button settings = (Button) v.findViewById(R.id.settings);
        settings.setOnClickListener(this);
        Button about = (Button) v.findViewById(R.id.about);
        about.setOnClickListener(this);
        BusProvider.getInstance().register(this);
        return v;
    }

    @Override
    public void onDestroyView() {
        BusProvider.getInstance().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        Header header = ((HeaderAdapter) parent.getAdapter()).getItem(position);
        if (!header.equals(mCurHeader)) {
            mCurHeader = header;
            mCurHeader.onHeaderClick();
        }
    }

    @Subscribe
    public void subscribeUserInfoResponseEvent(
            HttpResponseEvents.UserInfoResponseEvent event) {
        if (event.mUser == null) {
            mBindAccount.setVisibility(View.VISIBLE);
            mAccountName.setVisibility(View.GONE);
        } else {
            mAccountName.setText("" + event.mUser.username);
            mToken = event.mUser.private_token;
            mBindAccount.setVisibility(View.GONE);
            mAccountName.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe
    public void subscribeSlideResponseEvent(
            HttpResponseEvents.SlideResponseEvent event) {
        Slide slide = event.mSlide;
        if (slide != null) {
            ArrayList<Header> headers = new ArrayList<Header>();
            for (Slide.Data data : slide.data) {
                Header header = new Header();
                header.sort_id = 0;
                header.title = data.title;
                headers.add(header);
                if (data.items != null) {
                    for (SlideItem slideItem : data.items) {
                        header = new Header();
                        header.sort_id = data.sort_id;
                        header.label = slideItem.label;
                        header.title = slideItem.title;
                        header.auth = slideItem.auth;
                        header.uri = slideItem.uri;
                        header.count = slideItem.count;
                        if (slideItem.auto_loading) {
                            header.onHeaderClick();
                        }
                        headers.add(header);
                    }
                }
            }
            mListView.setAdapter(new HeaderAdapter(getActivity(), headers));
        } else {
            mListView.setAdapter(null);
        }
    }

    private final class Header {
        public int sort_id;
        public String label;
        public String title;
        public int auth;
        public String uri;
        public int count;

        public void onHeaderClick() {
            BusProvider.getInstance().post(
                    new DecouplingEvents.SlidingMenuCloseEvent());
            BusProvider.getInstance().post(
                    new DecouplingEvents.TitleChangedEvent(title));
            RequestParams requestParams = null;
            switch (auth) {
            case 1:
                if (!TextUtils.isEmpty(mToken)) {
                    requestParams = new RequestParams("token", mToken);
                }
                break;
            case 2:
                if (!TextUtils.isEmpty(mToken)) {
                    requestParams = new RequestParams("token", mToken);
                }
                break;
            default:
                break;
            }
            BusProvider.getInstance().post(
                    new HttpRequestEvents.ArticleListRequestEvent(uri,
                            requestParams));
        }
    }

    private static class HeaderAdapter extends BaseAdapter {

        static final int HEADER_TYPE_CATEGORY = 0;

        static final int HEADER_TYPE_ONE = 1;

        static final int HEADER_TYPE_TWO = 2;

        static final int HEADER_TYPE_THREE = 3;

        private static final int HEADER_TYPE_COUNT = HEADER_TYPE_THREE + 1;

        private List<Header> mHeaders;

        private LayoutInflater mInflater;

        private Context mContext;

        HeaderAdapter(Context context, List<Header> headers) {
            mContext = context;
            mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mHeaders = headers;
        }

        static int getHeaderType(Header header) {
            return header.sort_id;
        }

        @Override
        public int getCount() {
            return mHeaders.size();
        }

        @Override
        public Header getItem(int position) {
            return mHeaders.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return getHeaderType(getItem(position));
        }

        @Override
        public int getViewTypeCount() {
            return HEADER_TYPE_COUNT;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int position) {
            return getItemViewType(position) != HEADER_TYPE_CATEGORY;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HeaderViewHolder holder;
            Header header = getItem(position);
            int headerType = getHeaderType(header);
            View view = null;
            if (convertView == null) {
                holder = new HeaderViewHolder();
                switch (headerType) {
                case HEADER_TYPE_CATEGORY:
                    view = new TextView(mContext, null,
                            android.R.attr.listSeparatorTextViewStyle);
                    holder.mTitle = (TextView) view;
                    break;
                case HEADER_TYPE_ONE:
                    view = mInflater.inflate(R.layout.slide_title, parent,
                            false);
                    holder.mTitle = (TextView) view.findViewById(R.id.title);
                    break;
                case HEADER_TYPE_TWO:
                    view = mInflater.inflate(R.layout.slide_title, parent,
                            false);
                    holder.mTitle = (TextView) view.findViewById(R.id.title);
                    break;
                case HEADER_TYPE_THREE:
                    view = mInflater.inflate(R.layout.slide_sort_three, parent,
                            false);
                    holder.mTitle = (TextView) view.findViewById(R.id.title);
                    holder.mCount = (TextView) view.findViewById(R.id.count);
                    break;
                default:
                    break;
                }
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (HeaderViewHolder) view.getTag();
            }
            switch (headerType) {
            case HEADER_TYPE_CATEGORY:
                holder.mTitle.setText(header.title);
                break;
            case HEADER_TYPE_ONE:
                holder.mTitle.setText(header.title);
                break;
            case HEADER_TYPE_TWO:
                holder.mTitle.setText(header.title);
                break;
            case HEADER_TYPE_THREE:
                holder.mTitle.setText(header.title);
                holder.mCount.setText("" + header.count);
                break;
            default:
                break;
            }
            return view;
        }

        private static class HeaderViewHolder {
            TextView mTitle;
            TextView mCount;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.bind_account:
        case R.id.settings:
            Intent intent = new Intent();
            intent.setClass(getActivity(), SettingsActivity.class);
            startActivity(intent);
            break;
        case R.id.about:
            intent = new Intent();
            intent.setClass(getActivity(), AboutActivity.class);
            startActivity(intent);
            break;
        default:
            break;
        }
        BusProvider.getInstance().post(
                new DecouplingEvents.SlidingMenuCloseEvent());
    }

}
