package it.etoken.component.api.eosrpc;

import java.io.IOException;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Response;

import it.etoken.component.api.exception.MLApiException;

public class GetVotingInfo extends EosRpc {
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
			JSONArray rows = jsonObjectInfo.getJSONArray("rows");
			if(rows.size() <= 0) {
				return new EosResult(MLApiException.GET_ACCOUNT_ERR, null);
			}
			JSONObject row = rows.getJSONObject(0);
			if(row == null) {
				return new EosResult(MLApiException.GET_ACCOUNT_ERR, null);
			}
			String owner = row.getString("owner");
			JSONObject jParams = JSONObject.parseObject(reqParams);
			String voter = jParams.getString("lower_bound");
			if(StringUtils.isEmpty(voter) || !voter.equals(owner)) {
				return new EosResult(MLApiException.GET_ACCOUNT_ERR, null);
			}

			return new EosResult(MLApiException.SUCCESS, row.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new EosResult(MLApiException.GET_ACCOUNT_ERR, null);
	}
	
	
}
