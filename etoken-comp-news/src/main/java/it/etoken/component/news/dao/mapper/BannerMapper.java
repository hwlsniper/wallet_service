package it.etoken.component.news.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import it.etoken.base.model.news.entity.Banner;
import it.etoken.component.news.dao.provider.BannerProvider;

@Mapper
public interface BannerMapper {
	
	@Insert("INSERT INTO banner(title,img,url) VALUES(#{title}, #{img}, #{url})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	void insert(Banner banner);
	
	@UpdateProvider(type=BannerProvider.class,method="update")
	void update(Banner banner);
	
	@Delete("DELETE FROM banner WHERE id =#{id}")
	void delete(Long id);
	
	@Select("SELECT * FROM banner where tid=#{tid}")
	List<Banner> findByTid(Long tid);
	
	@Select("SELECT * FROM banner")
	List<Banner> findAll();
	
	@Select("SELECT * FROM banner where id=#{id}")
	Banner findById(Long id);

}