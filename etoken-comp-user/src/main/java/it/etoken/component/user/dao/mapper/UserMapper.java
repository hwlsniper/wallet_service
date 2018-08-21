package it.etoken.component.user.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import it.etoken.base.model.user.entity.User;
import it.etoken.component.user.dao.provider.AllProvider;

@Mapper
public interface UserMapper {
	
	@Insert("INSERT INTO user(username,password,nickname,photo) VALUES(#{username}, #{password}, #{nickname}, #{photo})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	void insert(User user);
	
	@UpdateProvider(type=AllProvider.class,method="updateUser")
	void update(User user);
	
	@Delete("DELETE FROM user WHERE id =#{id}")
	void delete(Long id);
	
	@Select("SELECT id,username,nickname,photo,eost,status,createdate,modifydate FROM user")
	List<User> findAll();
	
	@SelectProvider(type=AllProvider.class,method="findUser")
	List<User> search(String nickname);
	
	@Select("SELECT id,username,nickname,photo,eost,point,status,createdate,modifydate FROM user where id=#{id}")
	User findById(Long id);

	@Select("SELECT * FROM user where username=#{username}")
	User login(String username);
	
	@Update("update user set eost = eost + #{e} where id=#{id}")
	void addEost(@Param("id")Long id,@Param("e") Double e);
	
	@Update("update user set point = point + #{p} where id=#{id}")
	void addPoint(@Param("id")Long id,@Param("p") Long p);
	
	@Update("update user set status = #{status} where id=#{id}")
	void updateStatus(@Param("id")Long id,@Param("status") Integer status);
	
	@Update("update user set password = #{password} where username=#{phone}")
	void updatePwd(@Param("phone")String phone,@Param("password") String password);
	
	@Select("SELECT count(*) FROM user where username=#{username}")
	int exist(String username);
	
	@Select("SELECT * FROM user where username=#{username}")
	User findByUsername(String username);
	
	@Update("update user set eost =#{e} where id=#{id}")
	void updateEost(@Param("id")Long id,@Param("e") Double e);
}