package it.etoken.component.user.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import it.etoken.base.model.user.entity.UserInviteRecord;
import it.etoken.base.model.user.entity.UserPoint;
import it.etoken.base.model.user.entity.UserPointRecord;
import it.etoken.base.model.user.vo.UserPointVO;
import it.etoken.component.user.dao.provider.AllProvider;

@Mapper
public interface UserPointMapper {
	@Insert("INSERT INTO userpoint(uid,nickname,signin,share,interact,store,turnin,turnout) VALUES(#{uid}, #{nickname}, #{signin}, #{share}, #{interact}, #{store}, #{turnin}, #{turnout})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	void insert(UserPoint userPoint);

	@Select("SELECT * FROM userpoint where uid=#{uid}")
	UserPoint findByUid(String uid);

	@SelectProvider(type=AllProvider.class,method="findUserPoint")
	List<UserPointVO> findAll(String nickname);

	@Update("update userpoint set nickname=#{nickname},signin=#{signin},share=#{share},interact=#{interact},store=#{store},turnin=#{turnin},turnout=#{turnout} where uid=#{uid}")
	void update(UserPoint userPoint);
	
	// 积分记录
	@Insert("INSERT INTO userpoint_record(uid,nickname,type,point) VALUES(#{uid}, #{nickname}, #{type}, #{point})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	void insertRecord(UserPointRecord record);
	
	// 修改积分记录
	@Update("update userpoint_record set point=#{point} where uid=#{uid} and type='SIGN_IN' ORDER BY createdate DESC LIMIT 1")
	void updateRecord(UserPointRecord record);
	
	@Select("SELECT * FROM userpoint_record where uid=#{uid} and type='SIGN_IN' ORDER BY createdate DESC LIMIT 1  ")
	UserPointRecord findUserPointRecordByUid(Long uid);
	
	@Select("SELECT count(*) FROM userpoint_record where uid=#{uid} and type='SIGN_IN' and createdate>=date(now())")
	long countSigninRecordByUid(String uid);
	
	@SelectProvider(type=AllProvider.class,method="findUserPointRecord")
	List<UserPointRecord> findAllRecord(String nickname);

	@SelectProvider(type=AllProvider.class,method="findTodayUserPointRecord")
	List<UserPointRecord> findTodayRecord(String uid, String type);
}