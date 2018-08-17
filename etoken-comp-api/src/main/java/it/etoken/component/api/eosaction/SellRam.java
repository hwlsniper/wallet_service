package it.etoken.component.api.eosaction;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSONObject;

public class SellRam extends EosAction {

	@Override
	public JSONObject getDetail(JSONObject action, JSONObject params) {
		if (action == null) {
			return null;
		}

		JSONObject at = action.getJSONObject("action_trace");
		JSONObject act = at.getJSONObject("act");
		String description = EosAction.EosActionEnum.SELL_RAM.getDescription();
		String blockNum = action.getString("block_num");
		String blockTime = action.getString("block_time");
		String transactionId = at.getString("trx_id");
		JSONObject data = act.getJSONObject("data");
		String bytes = data.getString("bytes");
		String from;
		String to;
		String quantity;
		String memo = " ";

		JSONObject dataInLineTrace =at.getJSONArray("inline_traces").getJSONObject(0).getJSONObject("act").getJSONObject("data");
		from = dataInLineTrace.getString("from");
		to = dataInLineTrace.getString("to");
		
		String code = params.getString("code");
		if(null != code && !code.isEmpty()) {
			if(!code.equalsIgnoreCase("EOS")) {
				return null;
			}
		}
		
		quantity = dataInLineTrace.getString("quantity").replace(" EOS", "");

		String type = EosAction.EosActionEnum.SELL_RAM.getType();

		JSONObject actionDetail = new JSONObject();
		actionDetail.put("description", description);
		actionDetail.put("type", type);
		actionDetail.put("from", from);
		actionDetail.put("to", to);
		actionDetail.put("quantity", new BigDecimal(quantity).toString() +" EOS");
		actionDetail.put("memo", memo);
		actionDetail.put("bytes", bytes);
		actionDetail.put("blockNum", blockNum);
		actionDetail.put("blockTime", blockTime.replace("T", " ").substring(0, 16));
		actionDetail.put("transactionId", transactionId);
		return actionDetail;
	}
}
