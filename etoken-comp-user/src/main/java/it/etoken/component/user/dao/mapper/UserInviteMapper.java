package it.etoken.component.user.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import it.etoken.base.model.user.entity.UserInvite;
import it.etoken.base.model.user.vo.Invite;
import it.etoken.component.user.dao.provider.AllProvider;

@Mapper
public interface UserInviteMapper {
	
	@Insert("INSERT INTO user_invite(uid,code,max_use,used) VALUES(#{uid}, #{code}, #{max_use}, #{used})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	void insert(UserInvite obj);
	
	@UpdateProvider(type=AllProvider.class,method="updateUserInvite")
	void update(UserInvite obj);
	
	@Delete("DELETE FROM user_invite WHERE id =#{id}")
	void delete(Long id);
	
	@Select("SELECT * FROM user_invite")
	List<UserInvite> findAll();
	
	@Select("SELECT * FROM user_invite where id=#{id}")
	UserInvite findById(Long id);
	
	@Select("SELECT * FROM user_invite where uid=#{uid}")
	UserInvite findByUid(Long uid);
	
	@Select("SELECT * FROM user_invite where code=#{code}")
	UserInvite findByCode(String code);
	
	@Update("update user_invite set used=used+1 where id=#{id}")
	void updateUsed(Long id);
	
	@SelectProvider(type=AllProvider.class,method="findInvite")
	List<Invite> findInvite();

}