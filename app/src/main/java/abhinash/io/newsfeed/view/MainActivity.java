package abhinash.io.newsfeed.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

import abhinash.io.newsfeed.R;
import abhinash.io.newsfeed.controller.FeedRecyclerViewAdapter;
import abhinash.io.newsfeed.model.Article;

/**
 * Created by khanal on 2/17/17.
 * This is the main Activity.
 */

public class MainActivity extends AppCompatActivity implements FeedRecyclerViewAdapter.FeedRecyclerViewListener{

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
     * Starting item for the recycler view for between sessions.
     */
    private int startingItem;

    /**
     * Activity is created. Do view setup here.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_main);
        loadMockData();

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
            if (null == mFeedRecyclerViewAdapter) {
                mFeedRecyclerViewAdapter = new FeedRecyclerViewAdapter(mArticles, this);
            }
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false));
            mRecyclerView.setAdapter(mFeedRecyclerViewAdapter);
            mRecyclerView.getLayoutManager().scrollToPosition(startingItem);
    }

    /**
     * Load mock data.
     */
    private void loadMockData() {
        mArticles.clear();
        for (int i = 0; i < 10; i ++) {
            Article article = new Article();
            article.setWebTitle(String.format(Locale.US, "Title %d", i));
            article.setSectionName(String.format(Locale.US, "Section %d", i));
            article.setWebPublicationDate(String.format(Locale.US, "Date %d", i));
            mArticles.add(article);
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
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String url = article.getWebUrl();
        if (null == url) {
            url = "https://google.com";
        }
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void onScrollToNextPage() {

    }
}
