package it.etoken.component.api.eosrpc;

import java.io.IOException;

import com.squareup.okhttp.Response;

import it.etoken.component.api.exception.MLApiException;

public class PushTransaction extends EosRpc {

	@Override
	EosResult processResult(Response resp) {
		EosResult checkRet = checkResult(resp);
		if(!checkRet.isSuccess()) {
			return checkRet;
		}
		String result = null;
		try {
			result = resp.body().string();
			// sResp = sResp.replace("[", "").replace("]", "").replace("\\",
			// "").replace("/", "").replace("\"", "");
			// JSONObject jsonObjectInfo = new JSONObject();
			// jsonObjectInfo.put("data", sResp);
			// result = jsonObjectInfo.toJSONString();
		} catch (IOException e1) {
			e1.printStackTrace();
			return new EosResult(MLApiException.SYS_ERROR, null);
		}

		return new EosResult(MLApiException.SUCCESS, result);
	}

	@Override
	String url() {
		return "push_transaction";
	}

}
