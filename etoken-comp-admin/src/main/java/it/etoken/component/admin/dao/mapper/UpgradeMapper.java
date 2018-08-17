package it.etoken.component.admin.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import it.etoken.base.model.admin.entity.Upgrade;

@Mapper
public interface UpgradeMapper {

	@Select("SELECT * FROM upgrade where os=#{os}")
	Upgrade findByOs(String os);
	
	@Select("SELECT * FROM upgrade")
	List<Upgrade> findAll();

	@Update("update upgrade set version=#{version},intr=#{intr},url=#{url},must=#{must} where id=#{id}")
	void update(Upgrade up);
}
