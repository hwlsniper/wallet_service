package it.etoken.component.api.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import it.etoken.base.common.exception.MLExceptionEnum;
import it.etoken.component.api.exception.MLApiException;


public class BaseController
{

private final static Logger logger = LoggerFactory.getLogger(BaseController.class);
	
	final String MAP_MSG = "msg";
	
	final String MAP_DATA = "data";
	
	final String MAP_CODE = "code";

	@Resource
	public Environment env;


	/**
	 * 获取用户ip
	 *
	 * @param request
	 * @return
	 */
	final protected String getIpAddress(HttpServletRequest request)
	{
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getRemoteAddr();
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = "0.0.0.0";
		}
		return ip;
	}

	/**
	 * @param code
	 * @param msg
	 * @param data
	 * @return
	 */
	final protected Map<String, Object> success(Object data)
	{
		Map<String, Object> map = new HashMap<String, Object>(3);
		map.put(MAP_CODE,MLApiException.SUCCESS.getCode());
		map.put(MAP_MSG,MLApiException.SUCCESS.getMsg());
		map.put(MAP_DATA, data);
		logger.info("return map : " + map);
		return map;
	}
	
	/**
	 * @param code
	 * @param msg
	 * @param data
	 * @return
	 */
	final protected Map<String, Object> error(MLExceptionEnum ex,Object data)
	{
		Map<String, Object> map = new HashMap<String, Object>(3);
		map.put(MAP_CODE,ex.getCode());
		map.put(MAP_MSG,ex.getMsg());
		map.put(MAP_DATA, data);
		logger.info("return map : " + map);
		return map;
	}
	
	/**
	 * @param code
	 * @param msg
	 * @param data
	 * @return
	 */
	final protected Map<String, Object> error(String code,String msg,Object data)
	{
		Map<String, Object> map = new HashMap<String, Object>(3);
		map.put(MAP_CODE,code);
		map.put(MAP_MSG,msg);
		map.put(MAP_DATA, data);
		logger.info("return map : " + map);
		return map;
	}
}