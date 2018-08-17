package it.etoken.component.api.eosrpc;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import it.etoken.component.api.exception.MLApiException;

public abstract class EosRpc {

	protected static String reqParams = null;
	private static Response call(String url, String params) throws Exception {
		reqParams = params;
		OkHttpClient client = new OkHttpClient();
		client.setConnectTimeout(30, TimeUnit.SECONDS);
		MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
		Request httpReq = new Request.Builder().url(url).post(RequestBody.create(mediaType, params)).build();
		Response httpRes = null;

		httpRes = client.newCall(httpReq).execute();

		return httpRes;
	}

	EosResult checkResult(Response resp) {
		if (resp == null) {
			return new EosResult(MLApiException.EOSRPC_COMM_ERR, null);
		}
		if (!resp.isSuccessful()) {
			String msg = null;
			try {
				msg = resp.body().string();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new EosResult(MLApiException.EOSRPC_FAIL, msg);
		}
		return new EosResult(MLApiException.SUCCESS, "");
	}
	
	EosResult processResult(Response resp) {
		EosResult checkRet = checkResult(resp);
		if(!checkRet.isSuccess()) {
			return checkRet;
		}
		String result = null;
		try {
			result = resp.body().string();
			result = result.replace("[", "").replace("]", "").replace("\\", "").replace("/", "").replace("\"", "");
		} catch (IOException e1) {
			e1.printStackTrace();
			return new EosResult(MLApiException.SYS_ERROR, null);
		}

		return new EosResult(MLApiException.SUCCESS, result);
	}

	public EosResult run(String path, String params) throws Exception {
		Response response = call(path + url(), params);
		return processResult(response);
	}
	
	/**
	 * 
	 * @param path 请求path
	 * @param backupPath 备用path
	 * @param params 参数
	 * @return
	 * @throws Exception
	 */
	public EosResult run(String path, String backupPath, String params) throws Exception {
		Response response = null;
		try {
			response = call(path + url(), params);
			
			EosResult checkRet = checkResult(response);
			if(!checkRet.isSuccess()) {
				response = call(backupPath + url(), params);
			}
		}
		catch(Exception e) {
			try {
				response = call(backupPath + url(), params);
			}catch(Exception ee) {
				throw new Exception(ee);
			}
		}
		
		return processResult(response);
	}

	abstract String url();
}
