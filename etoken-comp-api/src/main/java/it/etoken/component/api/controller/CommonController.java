package it.etoken.component.api.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.code.kaptcha.impl.DefaultKaptcha;

import it.etoken.base.common.exception.MLCommonException;
import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.result.MLResult;
import it.etoken.base.common.result.MLResultObject;
import it.etoken.base.common.sms.SmsService;
import it.etoken.base.model.admin.entity.Upgrade;
import it.etoken.base.model.user.vo.LoginUser;
import it.etoken.cache.service.CacheService;
import it.etoken.component.api.exception.MLApiException;
import it.etoken.componet.admin.facade.UpgradeFacadeAPI;
import it.etoken.componet.user.facade.UserFacadeAPI;

@Controller
@RequestMapping(value = "/")
public class CommonController extends BaseController {

	private final static Logger logger = LoggerFactory.getLogger(BaseController.class);

	@Reference(version = "1.0.0")
	UserFacadeAPI userFacadeAPI;

	@Autowired
	CacheService cacheService;

	@Autowired
	SmsService smsService;

	@Reference(version = "1.0.0")
	UpgradeFacadeAPI upgradeFacadeAPI;

	@Autowired
	DefaultKaptcha defaultKaptcha;

	@ResponseBody
	@RequestMapping(value = "/login")
	public Object login(@RequestBody Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/login request map : " + requestMap);
		try {
			String phone = requestMap.get("phone");
			String password = requestMap.get("password");

			if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(password)) {
				return this.error(MLApiException.PARAM_ERROR, null);
			}

			String code = requestMap.get("code");

			if (StringUtils.isEmpty(code)) {
				return this.error(MLApiException.PARAM_ERROR, null);
			}

			String kcode = cacheService.get("vrifyCode_" + phone, String.class);

			if (!code.trim().toLowerCase().equals(kcode)) {
				Long i = cacheService.incr("error_" + kcode);
				if (i > 2) {
					cacheService.expire("vrifyCode_" + phone, 1);
					cacheService.expire("error_" + kcode, 1);
					return this.error(MLApiException.CODE_EXP, null);
				}
				return this.error(MLApiException.KCAPTURE_ERR, null);
			}
			cacheService.expire("vrifyCode_" + phone, 1);

			MLResultObject<LoginUser> result = userFacadeAPI.login(phone, password);
			if (result.isSuccess()) {
				return this.success(result.getResult());
			} else {
				return this.error(result.getErrorCode(), result.getErrorHint(), null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLApiException.SYS_ERROR, null);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/forget")
	public Object forget(@RequestBody Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/forget request map : " + requestMap);
		try {
			String phone = requestMap.get("phone");
			String password = requestMap.get("password");
			String code = requestMap.get("code");
			if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(password) || StringUtils.isEmpty(code)) {
				return this.error(MLApiException.PARAM_ERROR, null);
			}
			// 验证手机号
			String vcode = cacheService.get("capture_" + phone, String.class);
			if (!code.equals(vcode)) {
				return this.error(MLApiException.CAPTURE_ERR, null);
			}
			MLResult result = userFacadeAPI.chagePwd(phone, password);
			if (result.isSuccess()) {
				return this.success(true);
			} else {
				return this.error(result.getErrorCode(), result.getErrorHint(), null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLApiException.SYS_ERROR, null);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/register")
	public Object register(@RequestBody Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/register request map : " + requestMap);
		try {
			String phone = requestMap.get("phone");
			String code = requestMap.get("code");
			String invite = requestMap.get("invite");
			String password = requestMap.get("password");

			if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(password) || StringUtils.isEmpty(code)) {
				return this.error(MLApiException.PARAM_ERROR, null);
			}
			// // 验证手机号
			String vcode = cacheService.get("capture_" + phone, String.class);
			if (!code.equals(vcode)) {
				Long i = cacheService.incr("capture_" + phone + vcode);
				cacheService.expire("capture_" + phone + vcode, 50);
				if (i > 2) {
					cacheService.expire("capture_" + phone, 1);
					cacheService.expire("capture_" + vcode, 1);
					return this.error(MLApiException.CODE_EXP, null);
				}
				return this.error(MLApiException.CAPTURE_ERR, null);
			}

			cacheService.expire("capture_" + phone, 1);// ok 后删除

			MLResultObject<LoginUser> result = userFacadeAPI.register(phone, invite, password);
			if (result.isSuccess()) {
				return this.success(result.getResult());
			} else {
				return this.error(result.getErrorCode(), result.getErrorHint(), null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLApiException.SYS_ERROR, null);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/register1")
	public Object register1(@RequestParam Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/register1 request map : " + requestMap);
		return this.register(requestMap, request);
	}
	
	@ResponseBody
	@RequestMapping(value = "/existRegisteredUser")
	public Object existRegisteredUser(@RequestBody Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/selectphone request map : " + requestMap);
		try {
			String phone = requestMap.get("phone");
			
			if (StringUtils.isEmpty(phone)) {
				return this.error(MLApiException.PARAM_ERROR, null);
			}
			//查询改手机号码是否已经注册如果注册则返回
			MLResultObject<Boolean> result = userFacadeAPI.is_registered_user(phone);
			// 账户已存在true是已存在false是不存在
			return this.success(result.getResult());
		} catch (Exception e) {
			e.printStackTrace();
			return this.error(MLApiException.SYS_ERROR, null);
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value = "/capture")
	public Object capture(@RequestBody Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/capture request map : " + requestMap);
		try {

			String phone = requestMap.get("phone");

			String code = requestMap.get("code");

			if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
				return this.error(MLApiException.PARAM_ERROR, null);
			}

			if (phone.startsWith("170") || phone.startsWith("171")) {
				return this.error(MLApiException.PHONE_ERR, null);
			}
			String kcode = cacheService.get("vrifyCode_" + phone, String.class);
			if (!code.trim().toLowerCase().equals(kcode)) {
				Long i = cacheService.incr("error_" + kcode);
				if (i > 2) {
					cacheService.expire("vrifyCode_" + phone, 1);
					cacheService.expire("error_" + kcode, 1);
					return this.error(MLApiException.CODE_EXP, null);
				}
				return this.error(MLApiException.KCAPTURE_ERR, null);
			}

			cacheService.expire("vrifyCode_" + phone, 1);

			String ip = getIpAddress(request);

			logger.info("=========== ip " + ip);

			Long phoneMax = new Long(5);

			Long ipMax = new Long(10);

			try {
				phoneMax = cacheService.getSmsPhoneMax();
			} catch (Exception e) {
				logger.error("", e);
			}

			try {
				ipMax = cacheService.getSmsIpMax();
			} catch (Exception e) {
				logger.error("", e);
			}

			if (StringUtils.isEmpty(ip)) {
				throw new MLException(MLCommonException.sms_ip);
			}

			if (StringUtils.isEmpty(phone)) {
				throw new MLException(MLCommonException.sms_error);
			}

			Long i = cacheService.incr(ip);
			// 10小时内一个IP最多发送15条
			if (i == 1) {
				cacheService.expire(ip, 10 * 60 * 60);
			} else {
				if (i.intValue() >= ipMax.intValue()) {
					throw new MLException(MLCommonException.sms_excess);
				}
			}

			Long c = cacheService.incr(phone);
			// 10小时内一个手机只能发送5条
			if (c == 1) {
				cacheService.expire(phone, 10 * 60 * 60);
			} else {
				if (c.intValue() >= phoneMax.intValue()) {
					throw new MLException(MLCommonException.sms_excess);
				}
			}
			int phoneCode = smsService.send(phone, SmsService.SmsType.MESSAGE);
			cacheService.set("capture_" + phone, String.valueOf(phoneCode));
			cacheService.expire("capture_" + phone, 180);
			return this.success(true);
		} catch (MLException ex) {
			return this.error(ex.getCode(), ex.getMsg(), null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLApiException.SYS_ERROR, null);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/capture1")
	public Object capture1(@RequestParam Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/capture1 request map : " + requestMap);
		return this.capture(requestMap, request);
	}

	@ResponseBody
	@RequestMapping(value = "/upgrade")
	public Object upgrade(@RequestParam Map<String, String> requestMap, HttpServletRequest request) {
		logger.info("/upgrade request map : " + requestMap);
		try {
			String os = requestMap.get("os");
			if (StringUtils.isEmpty(os)) {
				return this.error(MLApiException.PARAM_ERROR, null);
			}
			MLResultObject<Upgrade> result = upgradeFacadeAPI.findByOS(os.toLowerCase());

			if (result.isSuccess()) {
				return this.success(result.getResult());
			} else {
				return this.error(result.getErrorCode(), result.getErrorHint(), null);
			}
		} catch (MLException ex) {
			return this.error(ex.getCode(), ex.getMsg(), null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return this.error(MLApiException.SYS_ERROR, null);
		}
	}

	@RequestMapping("/kapimg/{phone}")
	public void defaultKaptcha(@PathVariable String phone, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws Exception {

		String ip = getIpAddress(httpServletRequest);

		if (StringUtils.isEmpty(ip)) {
			httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		if (StringUtils.isEmpty(phone)) {
			httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		// Long phoneMax = new Long(5);

		// Long ipMax = new Long(10);

		// try {
		// phoneMax = cacheService.getSmsPhoneMax();
		// } catch (Exception e) {
		// logger.error("", e);
		// }

		// try {
		// ipMax = cacheService.getSmsIpMax();
		// } catch (Exception e) {
		// logger.error("", e);
		// }

		// Long i = cacheService.incr("kcaptrue_" + ip);
		//
		// if (i == 1) {
		// cacheService.expire("kcaptrue_" + ip, 1 * 60 * 60);
		// } else {
		// if (i.intValue() >= ipMax.intValue()) {
		// httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
		// return;
		// }
		// }

		// Long c = cacheService.incr("kcaptrue_" + phone);
		// // 1小时内一个手机只能发送5条
		// if (c == 1) {
		// cacheService.expire("kcaptrue_" + phone, 1 * 60 * 60);
		// } else {
		// if (c.intValue() >= phoneMax.intValue()) {
		// httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
		// return;
		// }
		// }

		ServletOutputStream responseOutputStream = null;
		byte[] captchaChallengeAsJpeg = null;
		ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
		try {
			String createText = defaultKaptcha.createText();
			String a = createText.substring(0, 2);
			String b = createText.substring(3, 5);
			String KeyString = "+-";
			char op = KeyString.charAt((int) Math.round(Math.random() * (KeyString.length() - 1)));
			int result = 0;
			int ia = Integer.parseInt(a);
			int ib = Integer.parseInt(b);
			if (op == '+') {
				result = ia + ib;
				createText = a + op + b + "=";
			} else if (op == '-') {
				if (ia < ib) {
					ia = ia + ib;// 此时a为两者之和 b没变
					ib = ia - ib;// a-b 为原来a的值 赋值给b
					ia = ia - ib;// 将 原来b的值赋值给a
				}
				result = ia - ib;
				createText = String.valueOf(ia) + op + String.valueOf(ib) + "=";
			}

			BufferedImage challenge = defaultKaptcha.createImage(createText);
			ImageIO.write(challenge, "jpg", jpegOutputStream);
			captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
			httpServletResponse.setHeader("Cache-Control", "no-store");
			httpServletResponse.setHeader("Pragma", "no-cache");
			httpServletResponse.setDateHeader("Expires", 0);
			httpServletResponse.setContentType("image/jpeg");
			responseOutputStream = httpServletResponse.getOutputStream();
			responseOutputStream.write(captchaChallengeAsJpeg);
			responseOutputStream.flush();
			responseOutputStream.close();
			// cacheService.set("vrifyCode_" + phone,
			// String.valueOf(createText.toLowerCase()));
			cacheService.set("vrifyCode_" + phone, String.valueOf(result));
			cacheService.expire("vrifyCode_" + phone, 60);
		} catch (IllegalArgumentException e) {
			httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		} catch (Exception ex) {

		} finally {
			if (responseOutputStream != null)
				responseOutputStream.close();
			if (jpegOutputStream != null)
				jpegOutputStream.close();
		}
	}

}
