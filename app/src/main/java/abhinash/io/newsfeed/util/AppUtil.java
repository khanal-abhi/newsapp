package abhinash.io.newsfeed.util;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import abhinash.io.newsfeed.model.Article;

/**
 * Created by khanal on 2/17/17.
 * Utility method for the app to convert String response to JSON Object and to Article Domain Object.
 */

public class AppUtil {


    /**
     * Extracts the json object from the response body string.
     * @param jsonObjectString -.
     * @return -.
     */
    private static JSONObject responseStringToJsonObject(@NonNull final String jsonObjectString) {
        JSONObject jsonObject = null;
        if (null != jsonObjectString) {
            try {
                jsonObject = new JSONObject(jsonObjectString);
            } catch (JSONException exception) {
                exception.printStackTrace();
            }
        }

        return jsonObject;
    }

    /**
     * Extracts the JSON array from the object.
     * @param jsonObject -.
     * @return -.
     */
    private static JSONArray jsonArrayFromJsonObject(@NonNull final JSONObject jsonObject) {
        JSONArray jsonArray = null;

        try {
            if (null != jsonObject && jsonObject.has(AppConstants.RESULTS_KEY)) {
                jsonArray = jsonObject.getJSONArray(AppConstants.RESULTS_KEY);
            }
        } catch (JSONException exception) {
            exception.printStackTrace();
        }

        return jsonArray;
    }

    /**
     * Extract the atricle from the json object.
     * @param jsonArticle -.
     * @return -.
     */
    private static Article getArticleFromJsonObject(@NonNull final JSONObject jsonArticle) {
        Article article = null;

        try {
            Article tempArticle = new Article();
            if (jsonArticle.has(AppConstants.ARTICLE_ID)) {
                tempArticle.setId(jsonArticle.getString(AppConstants.ARTICLE_ID));
            }
            if (jsonArticle.has(AppConstants.ARTICLE_SECTION_NAME)) {
                tempArticle.setSectionName(jsonArticle.getString(AppConstants.ARTICLE_SECTION_NAME));
            }
            if (jsonArticle.has(AppConstants.ARTICLE_WEB_TITLE)) {
                tempArticle.setWebTitle(jsonArticle.getString(AppConstants.ARTICLE_WEB_TITLE));
            }
            if (jsonArticle.has(AppConstants.ARTICLE_WEB_PUBLICATION_DATE)) {
                tempArticle.setWebPublicationDate(jsonArticle.getString(AppConstants.ARTICLE_WEB_PUBLICATION_DATE));
            }
            if (jsonArticle.has(AppConstants.ARTICLE_WEB_URL)) {
                tempArticle.setWebUrl(jsonArticle.getString(AppConstants.ARTICLE_WEB_URL));
            }
            article = tempArticle;
        } catch (JSONException exception) {
            exception.printStackTrace();
        }

        return article;
    }

    /**
     * Gets the articles array from the response body.
     * @param responseString -.
     * @return -.
     * @throws JSONException -.
     */
    public static ArrayList<Article> getArticlesFromResponseString(@NonNull final String responseString) throws JSONException {
        ArrayList<Article> articles = new ArrayList<>();
        JSONObject jsonObject = responseStringToJsonObject(responseString);
        if (null != jsonObject) {
            JSONArray jsonArray = jsonArrayFromJsonObject(jsonObject);
            if (null != jsonArray && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    Article article = getArticleFromJsonObject(jsonArray.getJSONObject(i));
                    if (null != article) {
                        articles.add(article);
                    }
                }
            }
        }
        return articles;
    }

}
