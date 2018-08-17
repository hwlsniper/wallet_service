package it.etoken.component.user.dao.provider;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import it.etoken.base.model.user.entity.User;
import it.etoken.base.model.user.entity.UserInvite;
import it.etoken.base.model.user.entity.UserInviteRecord;
import it.etoken.base.model.user.entity.UserPoint;

public class AllProvider {

	public String updateUser(final User user) {
		return new SQL() {
			{
				UPDATE("user");
				if (user.getUsername() != null) {
					SET("username = #{username}");
				}
				if (user.getPassword() != null) {
					SET("password = #{password}");
				}
				if (user.getNickname() != null) {
					SET("nickname = #{nickname}");
				}
				if (user.getPhoto() != null) {
					SET("photo = #{photo}");
				}
				if (user.getStatus() != null) {
					SET("status = #{status}");
				}
				if (user.getPoint() != null) {
					SET("point = #{point}");
				}
				WHERE("id = #{id}");
			}
		}.toString();
	}

	public String updateUserInvite(final UserInvite userInvite) {
		return new SQL() {
			{
				UPDATE("user_invite");
				if (userInvite.getUid() != null) {
					SET("uid = #{uid}");
				}
				if (userInvite.getCode() != null) {
					SET("code = #{code}");
				}
				if (userInvite.getMax_use() != null) {
					SET("max_use = #{max_use}");
				}
				if (userInvite.getUsed() != null) {
					SET("used = #{used}");
				}
				WHERE("id = #{id}");
			}
		}.toString();
	}

	public String updateUserInviteRecord(final UserInviteRecord obj) {
		return new SQL() {
			{
				UPDATE("user_invite_record");
				if (obj.getUid() != null) {
					SET("uid = #{uid}");
				}
				if (obj.getIid() != null) {
					SET("iid = #{iid}");
				}
				WHERE("id = #{id}");
			}
		}.toString();
	}

	public String findInvite(final String code) {
		if (StringUtils.isEmpty(code)) {
			return "select i.createdate,i.modifydate,u.nickname,ui.code from user_invite_record as i LEFT JOIN `user` u on i.uid=u.id LEFT JOIN user_invite ui on i.iid=ui.id";
		} else {
			return "select i.createdate,i.modifydate,u.nickname,ui.code from user_invite_record as i LEFT JOIN `user` u on i.uid=u.id LEFT JOIN user_invite ui on i.iid=ui.id where ui.code='"
					+ code + "'";
		}
	}

	public String findUser(final String nickname) {
		// if (StringUtils.isEmpty(nickname)) {
		// return "SELECT
		// u.username,u.nickname,u.photo,u.eost,u.status,u.createdate,u.modifydate,ui.code
		// FROM user u left JOIN user_invite ui on u.id=ui.uid";
		// } else {
		// return "SELECT
		// u.username,u.nickname,u.photo,u.eost,u.status,u.createdate,u.modifydate,ui.code
		// FROM user u left JOIN user_invite ui on u.id=ui.uid where username like '%"
		// + nickname + "%'";
		// }
		if (StringUtils.isEmpty(nickname)) {
			return "SELECT u.username,u.nickname,u.photo,u.eost,u.point,u.status,u.createdate,u.modifydate,ui.code FROM user u left JOIN user_invite ui on u.id=ui.uid";
		} else {
			return "SELECT u.username,u.nickname,u.photo,u.eost,u.point,u.status,u.createdate,u.modifydate,ui.code FROM user u left JOIN user_invite ui on u.id=ui.uid where username like '%"
					+ nickname + "%'";
		}
	}

	public String findRecord(final String nickname) {
		if (StringUtils.isEmpty(nickname)) {
			return "select e.type as action,e.createdate,e.modifydate,e.eost as reward,u.nickname from eost_record e left JOIN user u on u.id=e.uid ";
		} else {
			return "select e.type as action,e.createdate,e.modifydate,e.eost as reward,u.nickname from eost_record e left JOIN user u on u.id=e.uid where u.nickname like '%"
					+ nickname + "%'";
		}
	}

	public String findUserPoint(final String nickname) {
		if (StringUtils.isEmpty(nickname)) {
			return "SELECT * FROM userpoint";
		} else {
			return "SELECT * FROM userpoint where nickname like '%" + nickname + "%'";
		}
	}

	public String findUserPointRecord(final String nickname) {
		if (StringUtils.isEmpty(nickname)) {
			return "SELECT u.uid,u.nickname,u.type,u.point,u.createdate,u.modifydate FROM userpoint_record u";
		} else {
			return "SELECT u.uid,u.nickname,u.type,u.point,u.createdate,u.modifydate FROM userpoint_record u where nickname like '%"
					+ nickname + "%'";
		}
	}

	public String findTodayUserPointRecord(final String uid, final String type) {
		if(!StringUtils.isEmpty(uid) && !StringUtils.isEmpty(type)) {
			return "SELECT u.uid,u.nickname,u.type,u.point,u.createdate,u.modifydate FROM userpoint_record u where u.uid='"+ uid + "' and u.type='"+type+"' and u.createdate>=date(now())";
		} else if(!StringUtils.isEmpty(uid)) {
			return "SELECT u.uid,u.nickname,u.type,u.point,u.createdate,u.modifydate FROM userpoint_record u where u.uid='"+uid+"' and u.createdate>=date(now())";
		} else if(!StringUtils.isEmpty(type)) {
			return "SELECT u.uid,u.nickname,u.type,u.point,u.createdate,u.modifydate FROM userpoint_record u where u.type='"+type+"' and u.createdate>=date(now())";
		} else {
			return "SELECT u.uid,u.nickname,u.type,u.point,u.createdate,u.modifydate FROM userpoint_record u where u.createdate>=date(now())";
		}
	}
}
