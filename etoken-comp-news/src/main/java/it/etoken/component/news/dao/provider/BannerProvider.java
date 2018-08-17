package it.etoken.component.news.dao.provider;

import org.apache.ibatis.jdbc.SQL;

import it.etoken.base.model.news.entity.Banner;

public class BannerProvider {
	
	public String update(final Banner banner) {
        return new SQL() {
            {
                UPDATE("banner");
                if (banner.getTitle() != null) {
                    SET("title = #{title}");
                }
                if (banner.getImg() != null) {
                    SET("img = #{img}");
                }
                if (banner.getUrl() != null) {
                    SET("url = #{url}");
                }
                WHERE("id = #{id}");
            }
        }.toString();
    }
}
