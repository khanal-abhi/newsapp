package abhinash.io.newsfeed.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

import abhinash.io.newsfeed.model.Article;
import abhinash.io.newsfeed.service.FetchNewsFeed;
import abhinash.io.newsfeed.util.AppConstants;

/**
 * Created by khanal on 2/18/17.
 * This is the asyncloader for the feed.
 */

public class FeedAsyncLoader extends AsyncTaskLoader<ArrayList<Article>> {

    /**
     * List of articles loaded.
     */
    private ArrayList<Article> mArticles = new ArrayList<>();

    /**
     * Current page number.
     */
    private int page = -1;

    /**
     * last page number.
     */
    private int oldPage = -1;

    /**
     * Singleton instance of Fetch Feed Service.
     */
    private FetchNewsFeed mFetchNewsFeed = FetchNewsFeed.sharedInstance();

    /**
     * Public constructor with context.
     * @param context -.
     */
    public FeedAsyncLoader(Context context, final int page,
                           @NonNull final OnLoadCompleteListener onLoadCompleteListener) {
        super(context);
        this.page = page;
        this.oldPage = page - 1;
        if (null != onLoadCompleteListener) {
            registerListener(AppConstants.FEED_LOAD_KEY, onLoadCompleteListener);
        } else {
            throw new RuntimeException("Must register listener!");
        }
    }

    /**
     * Load data in the background. Make the API call and send the list if paging.
     * @return -.
     */
    @Override
    public ArrayList<Article> loadInBackground() {
        /**
         * Make the call for the page number.
         */
        List<Article> articles = mFetchNewsFeed.getArticlesFromPage(page);
        if (null != articles) {
            mArticles.clear();
            mArticles.addAll(articles);
        }
        return mArticles;
    }

    /**
     * Deliver data to the adpater.
     * @param data -.
     */
    @Override
    public void deliverResult(ArrayList<Article> data) {

        mArticles = data;

        /**
         * Deliver it if the loader has started.
         */
        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    /**
     * Start loading data.
     */
    @Override
    protected void onStartLoading() {
        /**
         * Deliver the data that is already present.
         */
        if (null != mArticles) {
            mArticles.clear();
        }

        /**
         * If the page number has changed since last load, then force another load.
         */
        if (oldPage != page) {
            forceLoad();
            oldPage = page;
        }
    }

    /**
     * Handle the stop loading.
     */
    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    /**
     * Handle the cancel loading.
     * @param data
     */
    @Override
    public void onCanceled(ArrayList<Article> data) {
        super.onCanceled(data);
    }

    /**
     * Handle the reset. Revert the loader back to its initial state (page and oldpage and empty
     * articles list).
     */
    @Override
    protected void onReset() {
        super.onReset();

        onStopLoading();
        oldPage = 0;
        page = 1;
        mArticles.clear();

    }

    /**
     * Set a new page number.
     * @param page -.
     */
    public void setPage(final int page) {
        oldPage = this.page;
        this.page = page;
    }
}
