package it.etoken.component.api.eosrpc;

import java.io.IOException;

import com.squareup.okhttp.Response;

import it.etoken.component.api.exception.MLApiException;

public class ContractAccountInfo extends EosRpc {
	
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

	@Override
	EosResult processResult(Response resp) {
		EosResult checkRet = checkResult(resp);
		if(!checkRet.isSuccess()) {
			return checkRet;
		}
		String result = null;
		try {
			result = resp.body().string();
		} catch (IOException e1) {
			e1.printStackTrace();
			return new EosResult(MLApiException.SYS_ERROR, null);
		}

		return new EosResult(MLApiException.SUCCESS, result);
	}

	@Override
	String url() {
		return "get_table_rows";
	}

}
