package com.eddc.weixinlink.entity;

import java.io.Serializable;

public class Medicine_SearchInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long Cid;
    private String ArticleId;
    private String ArticleTitle;
    private String FormatUrl;
    private String ArticleUrl;


    public Medicine_SearchInfo() {
        super();
    }

    public Medicine_SearchInfo(String ArticleId, String ArticleTitle, String FormatUrl, String ArticleUrl) {
        super();
        this.ArticleId = ArticleId;
        this.ArticleTitle = ArticleTitle;
        this.FormatUrl = FormatUrl;
        this.ArticleUrl = ArticleUrl;
    }

    public Long getCid() {
        return Cid;
    }

    public void setCid(Long cid) {
        Cid = cid;
    }

    public String getArticleId() {
        return ArticleId;
    }

    public void setArticleId(String articleId) {
        ArticleId = articleId;
    }

    public String getArticleTitle() {
        return ArticleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        ArticleTitle = articleTitle;
    }

    public String getFormatUrl() {
        return FormatUrl;
    }

    public void setFormatUrl(String formatUrl) {
        FormatUrl = formatUrl;
    }

    public String getArticleUrl() {
        return ArticleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        ArticleUrl = articleUrl;
    }
}
