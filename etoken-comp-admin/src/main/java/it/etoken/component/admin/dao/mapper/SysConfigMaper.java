package it.etoken.component.admin.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import it.etoken.base.model.admin.entity.SysConfig;

@Mapper
public interface SysConfigMaper {

	@Select("SELECT * FROM sysconf where name=#{name}")
	SysConfig findByName(String name);
	
	@Select("SELECT * FROM sysconf")
	List<SysConfig> Findall();
		
	@Update("update sysconf set name=#{name},value=#{value} where name=#{name}")
	void update(SysConfig up);
	
}
