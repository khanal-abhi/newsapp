package abhinash.io.newsfeed.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import abhinash.io.newsfeed.R;
import abhinash.io.newsfeed.model.Article;
import abhinash.io.newsfeed.util.AppConstants;

/**
 * Created by khanal on 2/18/17.
 * This is the custom recycler view adapter for the feed. This will liaise between the asyncloader
 * and the recycler view.
 */

public class FeedRecyclerViewAdapter extends RecyclerView.Adapter<FeedRecyclerViewAdapter.ViewHolder>{

    /**
     * The list of articles to display.
     */
    private ArrayList<Article> mArticles;

    /**
     * The application context.
     */
    private Context mCotext;

    /**
     * The listener for this adapter.
     */
    private FeedRecyclerViewListener mFeedRecyclerViewListener;

    /**
     * Page size.
     */
    private final int pageSize = AppConstants.pageSize;

    /**
     * Current page.
     */
    private int page = 0;

    /**
     * Public constructor to take in the list of articles.
     * @param articles
     */
    public FeedRecyclerViewAdapter(@NonNull final ArrayList<Article> articles,
                                   @NonNull final FeedRecyclerViewListener feedRecyclerViewListener) {
        mArticles = articles;
        mFeedRecyclerViewListener = feedRecyclerViewListener;
    }

    /**
     * When creating the view holder, hold a refence to the context.
     * @param parent viewgroup to hold the row.
     * @param viewType n/a only using a single row type.
     * @return -.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        mCotext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mCotext);
        View row = inflater.inflate(R.layout.row_news_feed, parent, false);

        if (null != row) {
            viewHolder = new ViewHolder(row);
        }
        return viewHolder;
    }

    /**
     * This is called when a particular view needs to be bound to the article.
     * @param holder the (possibly) recycled viewholder
     * @param position position of the row.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article article = mArticles.get(position);
        if (null != article) {
            holder.bind(mArticles.get(position));
        }
    }

    /**
     * The total number of items in the recyclerview.
     * @return
     */
    @Override
    public int getItemCount() {
        return mArticles.size();
    }


    /**
     * The viewholder to hold the article.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * Text view for the title.
         */
        private TextView mTitle;
        /**
         * Text view for the section.
         */
        private TextView mSection;
        /**
         * Text view for the date.
         */
        private TextView mDate;
        /**
         * Text view for the type.
         */
        private TextView mType;

        /**
         * The bound article to display.
         */
        private Article mArticle;

        /**
         * Public constructor to bind the views.
         * @param itemView
         */
        public ViewHolder(View itemView) {
            super(itemView);

            mTitle = (TextView)itemView.findViewById(R.id.txt_tite);
            mSection = (TextView)itemView.findViewById(R.id.txt_section);
            mDate = (TextView)itemView.findViewById(R.id.txt_date);

        }


        /**
         * This binds the article to the viewholder.
         * @param article -.
         */
        public void bind(@NonNull final Article article) {
            mArticle = article;
            if (null != article) {
                if (null != mArticle.getWebTitle()) {
                    mTitle.setVisibility(View.VISIBLE);
                    mTitle.setText(mArticle.getWebTitle());
                } else {
                    mTitle.setVisibility(View.GONE);
                    mTitle.setText("");
                }

                if (null != mArticle.getSectionName()) {
                    mSection.setVisibility(View.VISIBLE);
                    mSection.setText(mArticle.getSectionName());
                } else {
                    mSection.setVisibility(View.GONE);
                    mSection.setText("");
                }

                if (null != mArticle.getWebPublicationDate()) {
                    mDate.setVisibility(View.VISIBLE);
                    mDate.setText(mArticle.getWebPublicationDate());
                } else {
                    mDate.setVisibility(View.GONE);
                    mDate.setText("");
                }
            }
        }
    }

    /**
     * This is the listener interface for the Feed Recyclerview
     */
    public interface FeedRecyclerViewListener {

        /**
         * Fired when the article is seclected.
         */
        void onArticleSelected();

        /**
         * Fired when the next page is required.
         */
        void onScrollToNextPage();
    }
}
