package it.etoken.component.news.dao.provider;

import org.apache.ibatis.jdbc.SQL;

import it.etoken.base.model.news.entity.NewsType;

public class NewsTypeProvider {
	
	public String update(final NewsType newsType) {
        return new SQL() {
            {
                UPDATE("news_type");
                if (newsType.getName() != null) {
                    SET("name = #{name}");
                }
                if (newsType.getUrl() != null) {
                    SET("url = #{url}");
                }
                if (newsType.getType() != null) {
                    SET("type = #{type}");
                }
                WHERE("id = #{id}");
            }
        }.toString();
    }
}
