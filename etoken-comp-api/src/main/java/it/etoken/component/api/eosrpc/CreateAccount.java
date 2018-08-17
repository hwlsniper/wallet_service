package it.etoken.component.api.eosrpc;

import java.io.IOException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Response;

import it.etoken.component.api.exception.MLApiException;

public class CreateAccount extends EosRpc {

	@Override
	EosResult processResult(Response resp) {
		EosResult checkRet = checkResult(resp);
		if(!checkRet.isSuccess()) {
			return checkRet;
		}
		
		String result = null;
		try {
			result = resp.body().string();

			JSONObject jsonObjectInfo = null;
			try {
				jsonObjectInfo = JSONObject.parseObject(result);
			} catch (Exception e) {
				e.printStackTrace();
				return new EosResult(MLApiException.ACCOUNT_CREATE_ERR, null);
			}
			if (jsonObjectInfo.containsKey("code") && (jsonObjectInfo.getIntValue("code") != 0)) {
				JSONObject jsonObjectMsg = jsonObjectInfo.getJSONObject("msg");
				JSONObject jsonObjectError = jsonObjectMsg.getJSONObject("error");
				JSONArray jaDetails = jsonObjectError.getJSONArray("details");
				JSONObject jsonDetail = jaDetails.getJSONObject(0);
				String message = jsonDetail.getString("message");
				if(3050000 == jsonObjectError.getIntValue("code")) {
					message = "账号已存在";
				}
				return new EosResult(MLApiException.ACCOUNT_CREATE_ERR, message);
			}

			return new EosResult(MLApiException.SUCCESS, result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new EosResult(MLApiException.ACCOUNT_CREATE_ERR, null);
	}

	@Override
	String url() {
		return "account/create";
	}

}
