package it.etoken.component.user.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import it.etoken.base.model.user.entity.UserInviteRecord;
import it.etoken.base.model.user.vo.UpInvite;
import it.etoken.component.user.dao.provider.AllProvider;

@Mapper
public interface UserInviteRecordMapper {
	
	@Insert("INSERT INTO user_invite_record(uid,iid) VALUES(#{uid}, #{iid})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	void insert(UserInviteRecord obj);
	
	@UpdateProvider(type=AllProvider.class,method="updateUserInviteRecord")
	void update(UserInviteRecord obj);
	
	@Delete("DELETE FROM user_invite_record WHERE id =#{id}")
	void delete(Long id);
	
	@Select("SELECT * FROM user_invite_record")
	List<UserInviteRecord> findAll();
	
	@Select("SELECT * FROM user_invite_record where id=#{id}")
	UserInviteRecord findById(Long id);

	@Select("SELECT ui.id,ui.uid FROM user_invite_record uir LEFT JOIN user_invite ui on uir.iid = ui.id where uir.uid=#{id} limit 1")
	UpInvite getUpInvite(Long id);
	
	
	@Select("select sum(ui.used) as c FROM user_invite_record uir LEFT JOIN user_invite ui on uir.uid=ui.uid WHERE uir.iid =(SELECT id from user_invite  where uid=#{id})")
	Long leve2Count(Long id);
	
	@Select("SELECT ui.code from user_invite_record uir LEFT JOIN user_invite ui on uir.iid=ui.id where uir.uid=#{id}")
	String getBindCode(Long id);
	
	@Select("select count(*) from user_invite_record where uid=#{uid}")
	Long isBind(@Param("uid")Long uid);
}