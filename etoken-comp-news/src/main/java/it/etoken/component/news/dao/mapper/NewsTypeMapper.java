package it.etoken.component.news.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import it.etoken.base.model.news.entity.NewsType;
import it.etoken.component.news.dao.provider.NewsTypeProvider;

@Mapper
public interface NewsTypeMapper{
	
	@Insert("INSERT INTO news_type(name,url,type) VALUES(#{name},#{url},#{type})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id") 
	void insert(NewsType newsType);
	
	@UpdateProvider(type=NewsTypeProvider.class,method="update")
	void update(NewsType newsType);
	
	@Delete("DELETE FROM news_type WHERE id =#{id}")
	void delete(Long id);
	
	@Select("SELECT * FROM news_type")
	List<NewsType> findAll();
	
	@Select("SELECT * FROM news_type where id=#{id}")
	NewsType findById(Long id);

}