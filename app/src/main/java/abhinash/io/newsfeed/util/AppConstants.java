package abhinash.io.newsfeed.util;

/**
 * Created by khanal on 2/17/17.
 * This holds the costants required for the app.
 */

public interface AppConstants {

    /**
     * App Config keys
     */
    String API_KEY_KEY = "api-key";
    String API_KEY = "b13dbcff-f820-4819-aac4-b33ee847f3f9";
    String API_QUERY_KEY = "q";
    String API_QUERY_KEYWORD = "music";
    String API_QUERY_PAGE_KEY = "page";
    String API_QUERY_PAGE_SIZE_KEY = "page-size";
    String RECYCLER_STARTING_POSITION = "startingPostion";
    int PAGE_SIZE = 10;

    String URL_STRING = "https://content.guardianapis.com/search";

    int FEED_LOAD_KEY = 1;


    /**
     * Domain keys.


    private String id;
    private String webTitle;
    private String sectionName;
    private String webUrl;
    private String webPublicationDate;

     */
    String RESULTS_KEY = "results";
    String RESPONSE_KEY = "response";
    String ARTICLE_ID = "id";
    String ARTICLE_WEB_TITLE = "webTitle";
    String ARTICLE_SECTION_NAME = "sectionName";
    String ARTICLE_WEB_URL = "webUrl";
    String ARTICLE_WEB_PUBLICATION_DATE = "webPublicationDate";
}
