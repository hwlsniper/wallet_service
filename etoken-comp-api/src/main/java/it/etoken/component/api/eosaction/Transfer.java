package it.etoken.component.api.eosaction;

import java.math.BigDecimal;
import java.util.Arrays;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;

public class Transfer extends EosAction {

	private static final String[] filterMemo = new String[] { "sell ram", "ram fee", "buy ram", "stake bandwidth" };

	@Override
	public JSONObject getDetail(JSONObject action, JSONObject params) {
		if (action == null) {
			return null;
		}

		String account_name = params.getString("account_name");
		if (StringUtils.isEmpty(account_name)) {
			return null;
		} 
		
		JSONObject at = action.getJSONObject("action_trace");
		JSONObject receipt = at.getJSONObject("receipt");
		String receiver = receipt.getString("receiver");
		
		if (!account_name.equals(receiver)) {
			return null;
		}
		JSONObject act = at.getJSONObject("act");
		String description = EosAction.EosActionEnum.TRANSFER.getDescription();
		String blockNum = action.getString("block_num");
		String blockTime = action.getString("block_time");
		String transactionId = at.getString("trx_id");
		JSONObject data = act.getJSONObject("data");
		String account = act.getString("account");
		String from;
		String to;
		String quantity;
		String memo;

		try {
			memo = data.getString("memo");
			if (Arrays.asList(filterMemo).contains(memo)) { // 临时这样过滤，后续有更好办法时再修改
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String contract_account = params.getString("contract_account");
		if (!account.equalsIgnoreCase(contract_account)) {
			return null;
		}
		
		String code = params.getString("code");
		if(null != code && !code.isEmpty()) {
			if(data.getString("quantity").indexOf(code)<0) {
				return null;
			}
		}
		
		String[] quantityArray = data.getString("quantity").split(" ");
		String uom = "";
		if(quantityArray.length == 2) {
			uom = quantityArray[1];
		}

		from = data.getString("from");
		to = data.getString("to");
		quantity = data.getString("quantity").replace(" " + uom, "");
		memo = data.getString("memo");

		String type = EosAction.EosActionEnum.TRANSFER.getType();
		if (StringUtils.isEmpty(type)) {
			if (account_name.equals(from)) {
				type = "转出";
			} else {
				type = "转入";
			}
		}

		JSONObject actionDetail = new JSONObject();
		actionDetail.put("description", description);
		actionDetail.put("type", type);
		actionDetail.put("from", from);
		actionDetail.put("to", to);
		actionDetail.put("quantity", new BigDecimal(quantity).toString() + " " + uom);
		actionDetail.put("memo", memo);
		actionDetail.put("blockNum", blockNum);
		actionDetail.put("blockTime", blockTime.replace("T", " ").substring(0, 16));
		actionDetail.put("transactionId", transactionId);
		return actionDetail;
	}
}
