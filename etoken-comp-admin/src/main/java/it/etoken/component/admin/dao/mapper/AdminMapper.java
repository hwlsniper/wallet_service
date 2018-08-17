package it.etoken.component.admin.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import it.etoken.base.model.admin.entity.Admin;

@Mapper
public interface AdminMapper {

	@Select("SELECT * FROM admin where username=#{username}")
	Admin findByUsername(String username);
	
	@Update("update admin set login_error = login_error-1 where id=#{id}")
	void loginError(@Param("id")Long id);
	
	@Update("update admin set login_error = 5 where id=#{id}")
	void resetLoginError(@Param("id")Long id);
	
	@Update("update admin set password = #{password} where id=#{id}")
	void changePwd(@Param("id")Long id,@Param("password")String password);
}
