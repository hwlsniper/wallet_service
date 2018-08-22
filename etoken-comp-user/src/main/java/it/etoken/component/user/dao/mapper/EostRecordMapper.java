package it.etoken.component.user.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import it.etoken.base.model.user.entity.EostRecord;
import it.etoken.base.model.user.vo.Reward;
import it.etoken.component.user.dao.provider.AllProvider;

@Mapper
public interface EostRecordMapper {
	
	@Insert("INSERT INTO eost_record(uid,type,bid,eost) VALUES(#{uid}, #{type}, #{bid}, #{eost})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	void insert(EostRecord obj);
	
	@SelectProvider(type=AllProvider.class,method="findRecord")
	List<Reward> findRecord(String nickname);
	
	@Select("select count(*) from eost_record where type='reg' and uid=#{id}")
	Long regCount(Long id);
		
	@Select("select * from eost_record where eos_account IS NOT NULL and uid=#{id}")
	List<EostRecord> findEosRecord(Long id);
	
	@Insert("INSERT INTO eost_record(uid,type,bid,eost,eos_account) VALUES(#{uid}, #{type}, #{bid}, #{eost},#{eos_account})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	void insertNew(EostRecord obj);

}