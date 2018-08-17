package it.etoken.component.news.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import it.etoken.base.model.news.entity.News;
import it.etoken.component.news.dao.provider.NewsProvider;

@Mapper
public interface NewsMapper {
	
	@Insert("INSERT INTO news(title,content,url,tid,source,html,visable,otherid) VALUES(#{title}, #{content}, #{url}, #{tid}, #{source}, #{html}, #{visable}, #{otherid})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	void insert(News news);
	
	@UpdateProvider(type=NewsProvider.class,method="update")
	void update(News news);
	
	@Delete("DELETE FROM news WHERE id =#{id}")
	void delete(Long id);
	
	@Select("SELECT id,title,content,url,tid,source,createdate,modifydate,otherid FROM news where tid=#{tid} and visable=1")
	List<News> findByTid(Long tid);
	
	@Select("select * from news n LEFT JOIN news_statistics ns on n.id=ns.nid where title like concat('%', #{title}, '%') order by n.createdate desc")
	List<News> findAll(@Param("title")String title);
	
	@Select("select * from news n LEFT JOIN news_statistics ns on n.id=ns.nid order by n.createdate desc limit 20")
	List<News> findLatestNews();
	
	@Select("select * from news ")
	List<News> findAllNews();
	
	@Select("SELECT * FROM news where id=#{id}")
	News findById(Long id);
	
	@Select("SELECT * FROM news where otherid=#{otherid} and source=#{source} limit 1")
	News findByOtherIdAndSource(@Param("otherid")String otherid, @Param("source")String source);

	@Select("SELECT * FROM news where title=#{title} and source=#{source} limit 1")
	News findByTitleAndSource(@Param("title")String otherid, @Param("source")String source);
	
	@Select("SELECT * FROM news where content=#{content}  limit 1")
	News findByContent(@Param("content")String content);
	
	@Select("select * FROM news where title like concat('%', #{title}, '%') order by createdate desc ")
	List<News> findByTitle(@Param("title")String title);
}