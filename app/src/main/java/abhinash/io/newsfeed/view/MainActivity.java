package abhinash.io.newsfeed.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.ArrayList;

import abhinash.io.newsfeed.R;
import abhinash.io.newsfeed.controller.FeedAsyncLoader;
import abhinash.io.newsfeed.controller.FeedRecyclerViewAdapter;
import abhinash.io.newsfeed.model.Article;
import abhinash.io.newsfeed.model.SerializableArrayList;
import abhinash.io.newsfeed.util.AppConstants;

/**
 * Created by khanal on 2/17/17.
 * This is the main Activity.
 */

public class MainActivity extends AppCompatActivity implements
        FeedRecyclerViewAdapter.FeedRecyclerViewListener,
        Loader.OnLoadCompleteListener<ArrayList<Article>>,
        SearchView.OnQueryTextListener
{

    /**
     * Recyclerview adapter.
     */
    private FeedRecyclerViewAdapter mFeedRecyclerViewAdapter;

    /**
     * The list of articles to display;
     */
    private ArrayList<Article> mArticles = new ArrayList<>();

    /**
     * The recycler view to display the feed.
     */
    private RecyclerView mRecyclerView;

    /**
     * Seacrh view
     */
    private SearchView mSearchView;

    /**
     * Last used query string do not make the same call again.
     */
    private String queryString;

    /**
     * Starting item for the recycler view for between sessions.
     */
    private int startingItem = 0;

    /**
     * Current page for api call.
     */
    private int page = 1;

    /**
     * Async loader for feed.
     */
    private FeedAsyncLoader mFeedAsyncLoader;

    /**
     * Activity is created. Do view setup here.
     * @param savedInstanceState -.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_main);
        mSearchView = (SearchView) findViewById(R.id.searchbar_main);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpFeed();
    }

    /**
     * Set up the feed with the items and the saved position.
     */
    private void setUpFeed() {
        mSearchView.setOnQueryTextListener(this);
        if (null == mFeedAsyncLoader) {
            mFeedAsyncLoader = new FeedAsyncLoader(this, page, this);
        }
        if (null == mFeedRecyclerViewAdapter) {
            mFeedRecyclerViewAdapter = new FeedRecyclerViewAdapter(mArticles, this);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mFeedRecyclerViewAdapter);
        mRecyclerView.getLayoutManager().scrollToPosition(startingItem);
        if (mArticles.isEmpty()) {
            mSearchView.setQuery(AppConstants.API_QUERY_KEYWORD, true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveFirstCompletelyVisibleItemPosition();
    }

    /**
     * Save the first item position.
     */
    private void saveFirstCompletelyVisibleItemPosition() {
        startingItem = ((LinearLayoutManager)mRecyclerView.getLayoutManager())
                .findFirstCompletelyVisibleItemPosition();
    }

    @Override
    public void onArticleSelected(@NonNull final Article article) {
        if (null != article.getWebUrl()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(article.getWebUrl()));
            startActivity(intent);
        }
    }

    /**
     * Save the page number for data as well as starting position for the recycler view.
     * @param outState -.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (null != outState) {
            outState.putInt(AppConstants.API_QUERY_PAGE_KEY, page);
            outState.putInt(AppConstants.RECYCLER_STARTING_POSITION, startingItem);
            SerializableArrayList<Article> articleSerializableArrayList =
                    new SerializableArrayList<>();
            articleSerializableArrayList.setArrayList(mArticles);
            outState.putSerializable(AppConstants.API_QUERY_KEY, articleSerializableArrayList);
        }
    }

    /**
     * Restore the page number for data as well as starting position for the recycler view.
     * @param savedInstanceState -.
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (null != savedInstanceState) {
            page = savedInstanceState.getInt(AppConstants.API_QUERY_PAGE_KEY, 1);
            startingItem = savedInstanceState.getInt(AppConstants.RECYCLER_STARTING_POSITION);
            SerializableArrayList serializableArrayList =
                    (SerializableArrayList) savedInstanceState.
                            getSerializable(AppConstants.API_QUERY_KEY);
            if (null != serializableArrayList && null != serializableArrayList.getArrayList()) {
                if (!serializableArrayList.getArrayList().isEmpty()) {
                    Object object = serializableArrayList.getArrayList().get(0);
                    if (object instanceof Article) {
                        for (Object articleObject : serializableArrayList.getArrayList()) {
                            mArticles.add((Article) articleObject);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onScrollToNextPage() {
        page += 1;
        mFeedAsyncLoader.setPage(page);
        mFeedAsyncLoader.startLoading();
    }

    @Override
    public void onLoadComplete(Loader<ArrayList<Article>> loader, ArrayList<Article> articles) {
        if (null != articles && articles.size() != 0) {
            int oldSize = mArticles.size();
            mArticles.addAll(articles);
            int newSize = articles.size();
            if (null != mFeedRecyclerViewAdapter) {
                mFeedRecyclerViewAdapter.notifyItemRangeInserted(oldSize, newSize - oldSize);
            }
        } else {
            if (page != 1)
            page -= 1;
        }
    }

    /**
     * Make the query
     * @param queryString -.
     */
    private void makeQuery(@NonNull final String queryString) {
        if (!queryString.equals(this.queryString)) {
            this.queryString = queryString;
            mArticles.clear();
            mFeedRecyclerViewAdapter.notifyDataSetChanged();
            mFeedAsyncLoader.setNewQueryString(queryString);
        }
    }

    /**
     * Submit new query.
     * @param query -.
     * @return -.
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        boolean handled = false;
        if (null != query && query.length() >= 2) {
            makeQuery(query);
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
            handled = true;
        } else {
            Toast.makeText(this, getString(R.string.query_error_message), Toast.LENGTH_SHORT)
                    .show();
        }
        return handled;
    }

    /**
     * Query text changed. If the text is 2 characters or more, start the load process.
     * @param query -.
     * @return -.
     */
    @Override
    public boolean onQueryTextChange(String query) {
        if (null != query && query.length() >= 2) {
            makeQuery(query);
        }
        return false;
    }
}
