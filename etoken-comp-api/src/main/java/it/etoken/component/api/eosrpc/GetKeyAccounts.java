package it.etoken.component.api.eosrpc;

import java.io.IOException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Response;

import it.etoken.component.api.exception.MLApiException;

public class GetKeyAccounts extends EosRpc {
	@Override
	String url() {
		return "get_key_accounts";
	}

	@Override
	EosResult processResult(Response resp) {
		EosResult checkRet = checkResult(resp);
		if(!checkRet.isSuccess()) {
			return checkRet;
		}
		
		String result = null;
		try {
			result = resp.body().string();

			JSONObject jsonObjectInfo = JSONObject.parseObject(result);
			JSONArray account_names = jsonObjectInfo.getJSONArray("account_names");
			String account_name = account_names.getString(0);
			if(account_name == null) {
				return new EosResult(MLApiException.GET_KEY_ACOUNT_ERR, null);
			}
			
			return new EosResult(MLApiException.SUCCESS, result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new EosResult(MLApiException.GET_KEY_ACOUNT_ERR, null);
	}
	
	
}
