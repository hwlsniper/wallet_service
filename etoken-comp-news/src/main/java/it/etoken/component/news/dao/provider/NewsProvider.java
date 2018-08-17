package it.etoken.component.news.dao.provider;

import org.apache.ibatis.jdbc.SQL;

import it.etoken.base.model.news.entity.News;

public class NewsProvider {
	
	public String update(final News news) {
        return new SQL() {
            {
                UPDATE("news");
                if (news.getTitle() != null) {
                    SET("title = #{title}");
                }
                if (news.getContent() != null) {
                    SET("content = #{content}");
                }
                if (news.getUrl() != null) {
                    SET("url = #{url}");
                }
                if (news.getSource() != null) {
                    SET("source = #{source}");
                }
                if (news.getTid() != null) {
                    SET("tid = #{tid}");
                }
                if (news.getHtml() != null) {
                    SET("html = #{html}");
                }
                if (news.getVisable() != null) {
                    SET("visable = #{visable}");
                }
                if (news.getCreatedate() != null) {
                    SET("createdate = #{createdate}");
                }
                if (news.getModifydate() != null) {
                    SET("modifydate = #{modifydate}");
                }
                WHERE("id = #{id}");
            }
        }.toString();
    }
}
