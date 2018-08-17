package it.etoken.component.news.dao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import it.etoken.base.model.news.entity.NewsStatistics;

@Mapper
public interface NewsStatisticsMapper {
	
	@Insert("INSERT INTO news_statistics(nid,up,down,share,view) VALUES(#{nid}, #{up}, #{down}, #{share}, #{view})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	void insert(NewsStatistics newsStatistics);
	
	@Update("update news_statistics set up=up+1 where nid=#{nid}")
	void updateUp(Long nid);
	
	@Update("update news_statistics set down=down+1 where nid=#{nid}")
	void updateDown(Long nid);
	
	@Update("update news_statistics set share=share+1 where nid=#{nid}")
	void updateShare(Long nid);
	
	@Update("update news_statistics set view=view+1 where nid=#{nid}")
	void updateView(Long nid);
	
	@Select("SELECT * FROM news_statistics where nid=#{nid}")
	NewsStatistics findById(Long nid);
	
}