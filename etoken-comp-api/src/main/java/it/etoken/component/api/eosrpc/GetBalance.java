package it.etoken.component.api.eosrpc;

import java.io.IOException;

import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Response;

import it.etoken.component.api.exception.MLApiException;

public class GetBalance extends EosRpc {
	@Override
	String url() {
		return "get_currency_balance";
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
			result = result.replace("[", "").replace("]", "").replace("\\", "").replace("/", "").replace("\"", "");
			JSONObject jParams = JSONObject.parseObject(reqParams);
			if(jParams.containsKey("symbol")) {
			    String symbol = jParams.getString("symbol");
				result = result.replace(symbol, "").replace(" ", "");
			}

			return new EosResult(MLApiException.SUCCESS, result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new EosResult(MLApiException.GET_BALANCE_ERR, null);
	}
	
	
}
