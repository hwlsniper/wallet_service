package it.etoken.component.api.eosrpc;

import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Response;

import it.etoken.component.api.exception.MLApiException;

public class ListProducers extends EosRpc {

	@Override
	String url() {
		return "get_producers";
	}

	@Override
	EosResult processResult(Response resp) {
		EosResult checkRet = checkResult(resp);
		if (!checkRet.isSuccess()) {
			return checkRet;
		}

		String result = null;
		try {
			result = resp.body().string();

			JSONObject jsonObjectInfo = JSONObject.parseObject(result);
			jsonObjectInfo.getJSONArray("rows");

		} catch (Exception e) {
			e.printStackTrace();
			return new EosResult(MLApiException.GET_PRODUCERS_ERR, null);
		}

		return new EosResult(MLApiException.SUCCESS, result);
	}
}
