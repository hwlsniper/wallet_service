package it.etoken.base.common.sms;

import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.etoken.base.common.exception.MLCommonException;
import it.etoken.base.common.exception.MLException;
import it.etoken.base.common.http.HttpClientService;
import it.etoken.base.common.http.HttpResult;
import it.etoken.base.common.utils.MathUtil;

@Component
public class SmsService {

	private final static Logger logger = LoggerFactory.getLogger(SmsService.class);

	final String encode = "UTF-8";

	public enum SmsType {
		VOICE, // 语音
		MESSAGE, // 短信
	};

	/**
	 * 语音短信方式
	 */
	final String username = "";

	final String password_md5 = "";

	final String apikey = "";

	/**
	 * 手机短信方式
	 */
	final String usernameMsg = "";

	final String passwordmd5Msg = "";

	final String apikeyMsg = "";

	@Autowired
	HttpClientService httpClientService;

	public int send(String phone) throws MLException {
		return send(phone, SmsType.VOICE);
	}

	public int send(String phone, Enum smsType) throws MLException {
		int code = MathUtil.randomCode();
		try {
			String contentUrlEncode = URLEncoder.encode("您的验证码是：" + code, encode);
			String url = "";
			if (smsType == SmsType.VOICE) {
				url = "http://m.5c.com.cn/api/send/index.php?username=" + username + "&password_md5=" + password_md5
						+ "&mobile=" + phone + "&apikey=" + apikey + "&content=" + contentUrlEncode + "&encode="
						+ encode;
			} else if (smsType == SmsType.MESSAGE) {
				url = "http://m.5c.com.cn/api/send/index.php?username=" + usernameMsg + "&password_md5="
						+ passwordmd5Msg + "&mobile=" + phone + "&apikey=" + apikeyMsg + "&content=" + contentUrlEncode
						+ "&encode=" + encode;
			}
			HttpResult result = httpClientService.doPost(url);
			if (!result.getBody().startsWith("success")) {
				throw new MLException(MLCommonException.sms_error);
			}

//			logger.info("phone: "+phone+"  code: "+code);
			return code;
		} catch (Exception e) {
			logger.error("", e);
			throw new MLException(MLCommonException.sms_error);
		}
	}
}
