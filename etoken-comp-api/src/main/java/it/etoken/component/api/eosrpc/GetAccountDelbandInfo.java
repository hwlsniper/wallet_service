package it.etoken.component.api.eosrpc;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Response;

import it.etoken.component.api.exception.MLApiException;

public class GetAccountDelbandInfo extends EosRpc {
	@Override
	String url() {
		return "get_table_rows";
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
			
			return new EosResult(MLApiException.SUCCESS, jsonObjectInfo.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new EosResult(MLApiException.GET_ACCOUNT_ERR, null);
	}
	
}
