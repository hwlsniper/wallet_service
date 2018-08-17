package it.etoken.component.api.eosrpc;

import java.io.IOException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Response;

import it.etoken.component.api.eosaction.EosAction;
import it.etoken.component.api.exception.MLApiException;
import it.etoken.component.api.utils.SortJsonArray;

public class GetActions extends EosRpc {
	@Override
	String url() {
		return "get_actions";
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
			JSONArray actionsOri = jsonObjectInfo.getJSONArray("actions");
			JSONArray actionsNew = new JSONArray();
			for (int i = 0; i < actionsOri.size(); i++) {
				JSONObject action = actionsOri.getJSONObject(i);
				if (action == null) {
					continue;
				}

				JSONObject at = action.getJSONObject("action_trace");
				JSONObject act = at.getJSONObject("act");
				String account = act.getString("account");
				String func = act.getString("name");
				EosAction eosAction = EosAction.getEosAction(account + "::" + func);
				if(eosAction == null) {
					continue;
				}
				JSONObject actionDetail = eosAction.getDetail(action, JSONObject.parseObject(reqParams));
				if(actionDetail == null) {
					continue;
				}
				actionsNew.add(actionDetail);
			}
			JSONObject res = new JSONObject();
			res.put("actions", SortJsonArray.sortJsonArrayString(actionsNew, "blockTime"));
			return new EosResult(MLApiException.SUCCESS, res.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new EosResult(MLApiException.GET_KEY_ACOUNT_ERR, null);
	}
}
