package it.etoken.component.api.eosaction;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSONObject;

public class UnDelegatebw extends EosAction {

	@Override
	public JSONObject getDetail(JSONObject action, JSONObject params) {
		if (action == null) {
			return null;
		}

		JSONObject at = action.getJSONObject("action_trace");
		JSONObject act = at.getJSONObject("act");
		String description = EosAction.EosActionEnum.UNDELEGATEBW.getDescription();
		String blockNum = action.getString("block_num");
		String blockTime = action.getString("block_time");
		String transactionId = at.getString("trx_id");
		JSONObject data = act.getJSONObject("data");
		String from;
		String to;
		String unstake_net_quantity;
		String unstake_cpu_quantity;
		String quantity;
		String memo = " ";
		
		String code = params.getString("code");
		if(null != code && !code.isEmpty()) {
			if(!code.equalsIgnoreCase("EOS")) {
				return null;
			}
		}

		from = data.getString("from");
		to = data.getString("receiver");
		unstake_net_quantity = data.getString("unstake_net_quantity").replace(" EOS", "");
		unstake_cpu_quantity = data.getString("unstake_cpu_quantity").replace(" EOS", "");
		quantity = String.valueOf(Float.parseFloat(unstake_net_quantity) + Float.parseFloat(unstake_cpu_quantity));

		String type = EosAction.EosActionEnum.UNDELEGATEBW.getType();

		JSONObject actionDetail = new JSONObject();
		actionDetail.put("description", description);
		actionDetail.put("type", type);
		actionDetail.put("from", from);
		actionDetail.put("to", to);
		actionDetail.put("quantity", new BigDecimal(quantity).toString() + " EOS");
		actionDetail.put("memo", memo);
		actionDetail.put("blockNum", blockNum);
		actionDetail.put("blockTime", blockTime.replace("T", " ").substring(0, 16));
		actionDetail.put("transactionId", transactionId);
		return actionDetail;
	}
}
