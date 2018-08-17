package it.etoken.component.market.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import it.etoken.base.model.market.entity.Coins;
import it.etoken.component.market.dao.provider.CoinsProvider;

@Mapper
public interface CoinsMapper {
	
	@Insert("INSERT INTO coins(name,intr,site,img,tag,total,marke,code,symble,contract_account,is_support_market) VALUES(#{name}, #{intr}, #{site}, #{img}, #{tag}, #{total}, #{marke},#{code},#{symble},#{contractAccount},#{isSupportMarket})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	void insert(Coins coins);
	
	@UpdateProvider(type=CoinsProvider.class,method="update")
	void update(Coins coins);
	
	@Delete("DELETE FROM coins WHERE id =#{id}")
	void delete(Long id);
	
	@Select("SELECT * FROM coins")
	List<Coins> findAll();
	
	@Select("SELECT * FROM coins where id=#{id}")
	Coins findById(Long id);

}