package it.etoken.component.api.eosrpc;

import java.io.IOException;

import com.squareup.okhttp.Response;

import it.etoken.component.api.exception.MLApiException;

public class GetGlobalInfo extends EosRpc {
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
			
			return new EosResult(MLApiException.SUCCESS, result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new EosResult(MLApiException.GET_UNDELEGATEBW_ERR, null);
	}
	
	
}
