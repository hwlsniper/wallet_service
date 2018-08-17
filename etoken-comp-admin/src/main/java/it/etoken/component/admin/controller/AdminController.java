package it.etoken.component.admin.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.dubbo.config.annotation.Reference;

import it.etoken.base.cache.utils.MLCacheUser;
import it.etoken.base.cache.utils.MLCacheUserPoint;
import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLPage;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.common.utils.MathUtil;
import it.etoken.base.model.user.entity.UserPointRecord;
import it.etoken.base.model.user.vo.UserPointVO;
import it.etoken.cache.service.CacheService;
import it.etoken.component.admin.exception.MLAdminException;
import it.etoken.component.admin.service.AdminService;
import it.etoken.component.admin.service.SysConfigService;
import it.etoken.componet.user.facade.UserFacadeAPI;

@Controller
@RequestMapping(value = "/admin")
public class AdminController extends BaseController {

	private final static Logger logger = LoggerFactory.getLogger(BaseController.class);

	@Autowired
	AdminService adminService;

	@Autowired
	SysConfigService sysConfigService;

	@Autowired
	CacheService cacheService;

	@Value("${img.server}")
	String ImgServer;

	@Value("${img.save}")
	String ImgSave;

	@Reference(version = "1.0.0", timeout=10000)
	UserFacadeAPI userFacadeAPI;

	@ResponseBody
	@RequestMapping(value = "/point/config")
	public Object point(@RequestParam Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/grow request map : " + requestMap);
		try {
			List<Map> config = new ArrayList<>();

			Map<String, String> item1 = new HashMap<>();
			item1.put("name", MLCacheUserPoint.SIGNIN_REWARD.getKey());
			item1.put("desc", MLCacheUserPoint.SIGNIN_REWARD.getDesc());
			item1.put("value", cacheService.getSigninReward().toString());

			Map<String, String> item2 = new HashMap<>();
			item2.put("name", MLCacheUserPoint.UP_REWARD.getKey());
			item2.put("desc", MLCacheUserPoint.UP_REWARD.getDesc());
			item2.put("value", cacheService.getUpReward().toString());

			Map<String, String> item3 = new HashMap<>();
			item3.put("name", MLCacheUserPoint.DOWN_REWARD.getKey());
			item3.put("desc", MLCacheUserPoint.DOWN_REWARD.getDesc());
			item3.put("value", cacheService.getDownReward().toString());

			Map<String, String> item4 = new HashMap<>();
			item4.put("name", MLCacheUserPoint.SHARE_REWARD.getKey());
			item4.put("desc", MLCacheUserPoint.SHARE_REWARD.getDesc());
			item4.put("value", cacheService.getShareReward().toString());

			Map<String, String> item5 = new HashMap<>();
			item5.put("name", MLCacheUserPoint.SIGNIN_REWARD_PER_DAY.getKey());
			item5.put("desc", MLCacheUserPoint.SIGNIN_REWARD_PER_DAY.getDesc());
			item5.put("value", cacheService.getSigninRewardPerDay().toString());

			Map<String, String> item6 = new HashMap<>();
			item6.put("name", MLCacheUserPoint.UP_REWARD_PER_DAY.getKey());
			item6.put("desc", MLCacheUserPoint.UP_REWARD_PER_DAY.getDesc());
			item6.put("value", cacheService.getUpRewardPerDay().toString());

			Map<String, String> item7 = new HashMap<>();
			item7.put("name", MLCacheUserPoint.DOWN_REWARD_PER_DAY.getKey());
			item7.put("desc", MLCacheUserPoint.DOWN_REWARD_PER_DAY.getDesc());
			item7.put("value", cacheService.getDownRewardPerDay().toString());

			Map<String, String> item8 = new HashMap<>();
			item8.put("name", MLCacheUserPoint.SHARE_REWARD_PER_DAY.getKey());
			item8.put("desc", MLCacheUserPoint.SHARE_REWARD_PER_DAY.getDesc());
			item8.put("value", cacheService.getShareRewardPerDay().toString());

			Map<String, String> item9 = new HashMap<>();
			item9.put("name", MLCacheUserPoint.TOTAL_REWARD_PER_DAY.getKey());
			item9.put("desc", MLCacheUserPoint.TOTAL_REWARD_PER_DAY.getDesc());
			item9.put("value", cacheService.getTotalRewardPerDay().toString());

			config.add(item1);
			config.add(item2);
			config.add(item3);
			config.add(item4);
			config.add(item5);
			config.add(item6);
			config.add(item7);
			config.add(item8);
			config.add(item9);

			return this.success(config);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLAdminException.SYS_ERROR, null);
		}

	}

	@ResponseBody
	@RequestMapping(value = "/point/updateConfig")
	public Object updatePointConfig(@RequestBody Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/updategrow request map : " + requestMap);
		try {
			String name = requestMap.get("name");
			String value = requestMap.get("value");
			if (StringUtils.isEmpty(name) || StringUtils.isEmpty(value)) {
				return this.error(MLAdminException.PARAM_ERROR, null);
			}
			if (name.equals(MLCacheUserPoint.SIGNIN_REWARD.getKey())) {
				if (!MathUtil.isInteger(value))
					return this.error(MLAdminException.PARAM_ERROR, null);
				cacheService.setSigninReward(Integer.parseInt(value));
			} else if (name.equals(MLCacheUserPoint.UP_REWARD.getKey())) {
				if (!MathUtil.isInteger(value))
					return this.error(MLAdminException.PARAM_ERROR, null);
				cacheService.setUpReward(Integer.parseInt(value));
			} else if (name.equals(MLCacheUserPoint.DOWN_REWARD.getKey())) {
				if (!MathUtil.isInteger(value))
					return this.error(MLAdminException.PARAM_ERROR, null);
				cacheService.setDownReward(Integer.parseInt(value));
			} else if (name.equals(MLCacheUserPoint.SHARE_REWARD.getKey())) {
				if (!MathUtil.isInteger(value))
					return this.error(MLAdminException.PARAM_ERROR, null);
				cacheService.setShareReward(Integer.parseInt(value));
			} else if (name.equals(MLCacheUserPoint.SIGNIN_REWARD_PER_DAY.getKey())) {
				if (!MathUtil.isInteger(value))
					return this.error(MLAdminException.PARAM_ERROR, null);
				cacheService.setSigninRewardPerDay(Integer.parseInt(value));
			} else if (name.equals(MLCacheUserPoint.UP_REWARD_PER_DAY.getKey())) {
				if (!MathUtil.isInteger(value))
					return this.error(MLAdminException.PARAM_ERROR, null);
				cacheService.setUpRewardPerDay(Integer.parseInt(value));
			} else if (name.equals(MLCacheUserPoint.DOWN_REWARD_PER_DAY.getKey())) {
				if (!MathUtil.isInteger(value))
					return this.error(MLAdminException.PARAM_ERROR, null);
				cacheService.setDownRewardPerDay(Integer.parseInt(value));
			} else if (name.equals(MLCacheUserPoint.SHARE_REWARD_PER_DAY.getKey())) {
				if (!MathUtil.isInteger(value))
					return this.error(MLAdminException.PARAM_ERROR, null);
				cacheService.setShareRewardPerDay(Integer.parseInt(value));
			}

			int totalPerDay = cacheService.getSigninRewardPerDay() + cacheService.getUpRewardPerDay()
					+ cacheService.getDownRewardPerDay() + cacheService.getShareRewardPerDay();
			cacheService.setTotalRewardPerDay(totalPerDay);
			return this.success(null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLAdminException.SYS_ERROR, null);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/point/record")
	public Object pointList(@RequestBody Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/point/list request map : " + requestMap);
		try {
			String page = requestMap.get("page");
			String search = requestMap.get("search");
			if (StringUtils.isEmpty(page) || !MathUtil.isInteger(page)) {
				return this.error(MLAdminException.PARAM_ERROR, null);
			}
			MLResultObject<MLPage<UserPointRecord>> result = userFacadeAPI.getPointRecord(search, Integer.parseInt(page));
			if (result.isSuccess()) {
				return this.success(result.getResult());
			} else {
				return this.error(result.getErrorCode(), result.getErrorHint(), null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLAdminException.SYS_ERROR, null);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/config")
	public Object login(@RequestParam Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/config request map : " + requestMap);
		try {
			List<Map> config = new ArrayList<>();

			Map<String, String> item1 = new HashMap<>();
			item1.put("name", MLCacheUser.LEV1_REWARD.getKey());
			item1.put("desc", MLCacheUser.LEV1_REWARD.getDesc());
			item1.put("value", cacheService.getLevel1Reward().toString());

			Map<String, String> item2 = new HashMap<>();
			item2.put("name", MLCacheUser.LEV2_REWARD.getKey());
			item2.put("desc", MLCacheUser.LEV2_REWARD.getDesc());
			item2.put("value", cacheService.getLevel2Reward().toString());

			Map<String, String> item3 = new HashMap<>();
			item3.put("name", MLCacheUser.REG_REWARD.getKey());
			item3.put("desc", MLCacheUser.REG_REWARD.getDesc());
			item3.put("value", cacheService.getRegReward().toString());

			Map<String, String> item4 = new HashMap<>();
			item4.put("name", MLCacheUser.SMS_IP.getKey());
			item4.put("desc", MLCacheUser.SMS_IP.getDesc());
			item4.put("value", cacheService.getSmsIpMax().toString());

			Map<String, String> item5 = new HashMap<>();
			item5.put("name", MLCacheUser.SMS_PHONE.getKey());
			item5.put("desc", MLCacheUser.SMS_PHONE.getDesc());
			item5.put("value", cacheService.getSmsPhoneMax().toString());

			Map<String, String> item6 = new HashMap<>();
			item6.put("name", MLCacheUser.INVITE.getKey());
			item6.put("desc", MLCacheUser.INVITE.getDesc());
			item6.put("value", cacheService.getInviteUrl());

			config.add(item1);
			config.add(item2);
			config.add(item3);
			config.add(item4);
			config.add(item5);
			config.add(item6);

			return this.success(config);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLAdminException.SYS_ERROR, null);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/updateConfig")
	public Object updateConfig(@RequestBody Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/updateConfig request map : " + requestMap);
		try {
			String name = requestMap.get("name");
			String value = requestMap.get("value");
			if (StringUtils.isEmpty(name) || StringUtils.isEmpty(value)) {
				return this.error(MLAdminException.PARAM_ERROR, null);
			}
			if (name.equals(MLCacheUser.LEV1_REWARD.getKey())) {
				if (!MathUtil.isDouble(value))
					return this.error(MLAdminException.PARAM_ERROR, null);
				cacheService.setLevel1Reward(Double.parseDouble(value));
			}
			if (name.equals(MLCacheUser.LEV2_REWARD.getKey())) {
				if (!MathUtil.isDouble(value))
					return this.error(MLAdminException.PARAM_ERROR, null);
				cacheService.setLevel2Reward(Double.parseDouble(value));
			}
			if (name.equals(MLCacheUser.REG_REWARD.getKey())) {
				if (!MathUtil.isDouble(value))
					return this.error(MLAdminException.PARAM_ERROR, null);
				cacheService.setRegReward(Double.parseDouble(value));
			}
			if (name.equals(MLCacheUser.SMS_IP.getKey())) {
				if (!MathUtil.isInteger(value))
					return this.error(MLAdminException.PARAM_ERROR, null);
				cacheService.setSmsIpMax(Long.parseLong(value));
			}
			if (name.equals(MLCacheUser.SMS_PHONE.getKey())) {
				if (!MathUtil.isInteger(value))
					return this.error(MLAdminException.PARAM_ERROR, null);
				cacheService.setSmsPhoneMax(Long.parseLong(value));
			}
			if (name.equals(MLCacheUser.INVITE.getKey())) {
				cacheService.setInviteUrl(value);
			}
			return this.success(null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLAdminException.SYS_ERROR, null);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/changePassword")
	public Object changePassword(@RequestBody Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/changePassword request map : " + requestMap);
		try {
			String username = requestMap.get("username");
			String password = requestMap.get("password");
			String newPassword = requestMap.get("newPassword");
			if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || StringUtils.isEmpty(newPassword)) {
				return this.error(MLAdminException.PARAM_ERROR, null);
			}
			adminService.changePwd(username, password, newPassword);
			return this.success(null);
		} catch (MLException ex) {
			logger.error(ex.toString());
			return this.error(ex.getCode(), ex.getMsg(), null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLAdminException.SYS_ERROR, null);
		}
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public Object upload(HttpServletRequest request) {
		try {
			List<String> paths = new ArrayList<>();
			List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
			BufferedOutputStream stream = null;
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			String folder = df.format(System.currentTimeMillis());
			for (MultipartFile file : files) {
				if (!file.isEmpty()) {
					try {
						String filePath = folder + "/" + System.currentTimeMillis() + ".png";
						File f = new File(ImgSave + filePath);
						if (!f.getParentFile().exists()) {
							f.getParentFile().mkdirs();
						}
						byte[] bytes = file.getBytes();
						stream = new BufferedOutputStream(new FileOutputStream(f));
						stream.write(bytes);
						stream.close();
						paths.add(ImgServer + filePath);
					} catch (Exception e) {
						logger.error("", e);
					} finally {
						if (stream != null) {
							try {
								stream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			return this.success(paths);
		} catch (MLException ex) {
			logger.error(ex.getMessage(), ex);
			return this.error(ex.getCode(), ex.getMsg(), null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLAdminException.SYS_ERROR, null);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/point/add")
	public Object pointAdd(@RequestBody Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/point/add request map : " + requestMap);
		try {
			String username = requestMap.get("username");
			String pointStr = requestMap.get("point");
			if (StringUtils.isEmpty(username) || !MathUtil.isInteger(username)) {
				return this.error(MLAdminException.PARAM_ERROR, null);
			}
			
			if (StringUtils.isEmpty(pointStr) || !MathUtil.isInteger(pointStr)) {
				return this.error(MLAdminException.PARAM_ERROR, null);
			}
			int point = Integer.parseInt(pointStr);
			
			MLResultObject<Boolean> result = userFacadeAPI.addPointByUsername(username, point);
			if (result.isSuccess()) {
				return this.success(result.getResult());
			} else {
				return this.error(result.getErrorCode(), result.getErrorHint(), null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLAdminException.SYS_ERROR, null);
		}
	}
}
