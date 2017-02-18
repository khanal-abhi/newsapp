package abhinash.io.newsfeed.model;

import java.io.Serializable;

/**
 * Created by khanal on 2/17/17.
 * The article domain object.
 */

public class Article implements Serializable {

    /**
     * Article Id
     */
    private String id;

    /**
     * Article Title.
     */
    private String webTitle;

    /**
     * Section Name
     */
    private String sectionName;

    /**
     * URL to open on click.
     */
    private String webUrl;

    /**
     * Stringified date of publication.
     */
    private String webPublicationDate;

    public Article() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public void setWebTitle(String webTitle) {
        this.webTitle = webTitle;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getWebPublicationDate() {
        return webPublicationDate;
    }

    public void setWebPublicationDate(String webPublicationDate) {
        this.webPublicationDate = webPublicationDate;
    }
}
