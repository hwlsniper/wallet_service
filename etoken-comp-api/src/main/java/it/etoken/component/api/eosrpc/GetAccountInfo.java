package it.etoken.component.api.eosrpc;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Response;

import it.etoken.component.api.exception.MLApiException;

public class GetAccountInfo extends EosRpc {
	// 取得本地时间：
    private Calendar cal = Calendar.getInstance();
    // 取得时间偏移量：
    private int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
    // 取得夏令时差：
    private int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
    
	@Override
	String url() {
		return "get_account";
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
			
			JSONObject display_data = new JSONObject();
			
			//内存
			BigDecimal ram_bytes = jsonObjectInfo.getJSONObject("total_resources").getBigDecimal("ram_bytes");
			ram_bytes = ram_bytes.divide(BigDecimal.valueOf(1024), 2, BigDecimal.ROUND_HALF_UP);
			display_data.put("ram_bytes", ram_bytes.toString()+"kb");
			
			BigDecimal ram_usage = jsonObjectInfo.getBigDecimal("ram_usage");
			ram_usage = ram_usage.divide(BigDecimal.valueOf(1024), 2, BigDecimal.ROUND_HALF_UP);
			display_data.put("ram_usage", ram_usage.toString()+"kb");
			
			BigDecimal ram_left = ram_bytes.subtract(ram_usage);
			display_data.put("ram_left", ram_left.toString()+"kb");
			
			BigDecimal ram_usage_percent = ram_usage.multiply(BigDecimal.valueOf(10000)).divide(ram_bytes, 0, BigDecimal.ROUND_HALF_UP).divide(BigDecimal.valueOf(100), 0, BigDecimal.ROUND_HALF_UP);
			display_data.put("ram_usage_percent", ram_usage_percent.toString()+"%");
			
			BigDecimal ram_left_percent = ram_left.multiply(BigDecimal.valueOf(10000)).divide(ram_bytes, 0, BigDecimal.ROUND_HALF_UP).divide(BigDecimal.valueOf(100), 0, BigDecimal.ROUND_HALF_UP);
			display_data.put("ram_left_percent", ram_left_percent.toString()+"%");
			
			//CPU
			BigDecimal cpu_limit_used = jsonObjectInfo.getJSONObject("cpu_limit").getBigDecimal("used");
			cpu_limit_used = cpu_limit_used.divide(BigDecimal.valueOf(1000), 2, BigDecimal.ROUND_HALF_UP);
			
			BigDecimal cpu_limit_available = jsonObjectInfo.getJSONObject("cpu_limit").getBigDecimal("available");
			cpu_limit_available = cpu_limit_available.divide(BigDecimal.valueOf(1000), 2, BigDecimal.ROUND_HALF_UP);
			display_data.put("cpu_limit_available", cpu_limit_available.toString());
			
			BigDecimal cpu_limit_max = jsonObjectInfo.getJSONObject("cpu_limit").getBigDecimal("max");
			cpu_limit_max = cpu_limit_max.divide(BigDecimal.valueOf(1000), 2, BigDecimal.ROUND_HALF_UP);
			display_data.put("cpu_limit_max", cpu_limit_max.toString());
			
			BigDecimal cpu_limit_available_percent = BigDecimal.ZERO;
			if(cpu_limit_max.compareTo(BigDecimal.ZERO)!=0) {
				cpu_limit_available_percent = cpu_limit_available.multiply(BigDecimal.valueOf(10000)).divide(cpu_limit_max, 0, BigDecimal.ROUND_HALF_UP).divide(BigDecimal.valueOf(100), 0, BigDecimal.ROUND_HALF_UP);
			}
			
			display_data.put("cpu_limit_available_percent", cpu_limit_available_percent.toString()+"%");
			
			BigDecimal self_delegated_bandwidth_cpu_weight = BigDecimal.ZERO;
			if(null != jsonObjectInfo.getJSONObject("self_delegated_bandwidth") && null != jsonObjectInfo.getJSONObject("self_delegated_bandwidth").getString("cpu_weight")) {
				self_delegated_bandwidth_cpu_weight = new BigDecimal(jsonObjectInfo.getJSONObject("self_delegated_bandwidth").getString("cpu_weight").replace("EOS", "").trim());
			}
			BigDecimal total_resources_cpu_weight = new BigDecimal(jsonObjectInfo.getJSONObject("total_resources").getString("cpu_weight").replace("EOS", "").trim());
			
			BigDecimal self_delegated_bandwidth_cpu_weight_percent = BigDecimal.ZERO;
			if(total_resources_cpu_weight.compareTo(BigDecimal.ZERO)!=0) {
				self_delegated_bandwidth_cpu_weight_percent = self_delegated_bandwidth_cpu_weight.multiply(BigDecimal.valueOf(10000)).divide(total_resources_cpu_weight, 0, BigDecimal.ROUND_HALF_UP).divide(BigDecimal.valueOf(100), 0, BigDecimal.ROUND_HALF_UP);
			}
			display_data.put("self_delegated_bandwidth_cpu_weight_percent", self_delegated_bandwidth_cpu_weight_percent.toString()+"%");
			
			
			Date request_time = null;
			BigDecimal refund_request_cpu_left_second = BigDecimal.ZERO;
			BigDecimal refund_request_cpu_left_second_percent = BigDecimal.ZERO;
			
			// 从本地时间里扣除这些差量，即可以取得UTC时间：
	        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
	        long mills = cal.getTimeInMillis();
			
			if(null !=jsonObjectInfo.getJSONObject("refund_request") && null != jsonObjectInfo.getJSONObject("refund_request").getDate("request_time")) {
				request_time = jsonObjectInfo.getJSONObject("refund_request").getDate("request_time");
				refund_request_cpu_left_second = BigDecimal.valueOf(72*60*60*1000 - (mills - request_time.getTime())).divide(BigDecimal.valueOf(1000), 0, BigDecimal.ROUND_HALF_UP);
				refund_request_cpu_left_second_percent = BigDecimal.valueOf((mills - request_time.getTime())).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(72*60*60*1000), 0, BigDecimal.ROUND_HALF_UP);
			}
			display_data.put("refund_request_cpu_left_second", refund_request_cpu_left_second);
			display_data.put("refund_request_cpu_left_second_percent", refund_request_cpu_left_second_percent.toString()+"%");
			//网络
			BigDecimal net_limit_used = jsonObjectInfo.getJSONObject("net_limit").getBigDecimal("used");
			net_limit_used = net_limit_used.divide(BigDecimal.valueOf(1000), 2, BigDecimal.ROUND_HALF_UP);
			
			BigDecimal net_limit_available = jsonObjectInfo.getJSONObject("net_limit").getBigDecimal("available");
			net_limit_available = net_limit_available.divide(BigDecimal.valueOf(1024), 2, BigDecimal.ROUND_HALF_UP);
			display_data.put("net_limit_available", net_limit_available.toString());
			
			BigDecimal net_limit_max = jsonObjectInfo.getJSONObject("net_limit").getBigDecimal("max");
			net_limit_max = net_limit_max.divide(BigDecimal.valueOf(1024), 2, BigDecimal.ROUND_HALF_UP);
			display_data.put("net_limit_max", net_limit_max.toString());
			
			BigDecimal net_limit_available_percent = BigDecimal.ZERO;
			if(net_limit_max.compareTo(BigDecimal.ZERO) != 0) {
				net_limit_available_percent = net_limit_available.multiply(BigDecimal.valueOf(10000)).divide(net_limit_max, 0, BigDecimal.ROUND_HALF_UP).divide(BigDecimal.valueOf(100), 0, BigDecimal.ROUND_HALF_UP);
			}
			display_data.put("net_limit_available_percent", net_limit_available_percent.toString()+"%");
			
			BigDecimal self_delegated_bandwidth_net_weight = BigDecimal.ZERO;
			if(null != jsonObjectInfo.getJSONObject("self_delegated_bandwidth") && null != jsonObjectInfo.getJSONObject("self_delegated_bandwidth").getString("net_weight")) {
				self_delegated_bandwidth_net_weight = new BigDecimal(jsonObjectInfo.getJSONObject("self_delegated_bandwidth").getString("net_weight").replace("EOS", "").trim());
			}
			
			BigDecimal total_resources_net_weight = new BigDecimal(jsonObjectInfo.getJSONObject("total_resources").getString("net_weight").replace("EOS", "").trim());
			
			BigDecimal self_delegated_bandwidth_net_weight_percent = BigDecimal.ZERO;
			if(total_resources_net_weight.compareTo(BigDecimal.ZERO) != 0) {
				self_delegated_bandwidth_net_weight_percent = self_delegated_bandwidth_net_weight.multiply(BigDecimal.valueOf(10000)).divide(total_resources_net_weight, 0, BigDecimal.ROUND_HALF_UP).divide(BigDecimal.valueOf(100), 0, BigDecimal.ROUND_HALF_UP);
			}
			display_data.put("self_delegated_bandwidth_net_weight_percent", self_delegated_bandwidth_net_weight_percent.toString()+"%");
			
			BigDecimal refund_request_net_left_second = BigDecimal.ZERO;
			BigDecimal refund_request_net_left_second_percent = BigDecimal.ZERO;
			if(null !=jsonObjectInfo.getJSONObject("refund_request") && null != jsonObjectInfo.getJSONObject("refund_request").getDate("request_time")) {
				refund_request_net_left_second = BigDecimal.valueOf(72*60*60*1000 - (mills - request_time.getTime())).divide(BigDecimal.valueOf(1000), 0, BigDecimal.ROUND_HALF_UP);
				refund_request_net_left_second_percent = BigDecimal.valueOf((mills - request_time.getTime())).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(72*60*60*1000), 0, BigDecimal.ROUND_HALF_UP);
			}
			display_data.put("refund_request_net_left_second", refund_request_net_left_second);
			display_data.put("refund_request_net_left_second_percent", refund_request_net_left_second_percent.toString()+"%");
			
			jsonObjectInfo.put("display_data", display_data);

//			display_data.put("ram_bytes", value)
			
//			内存总量：(this.props.Resources.total_resources.ram_bytes / 1024)(小数点以后取两位，然后加上kb)
//			内存已用：（total_resources.ram_bytes - ram_usage) / 1024)(小数点以后取两位，然后加上kb)
//			内存占用：this.props.Resources.ram_usage / 1024(小数点以后取两位，然后加上kb)
//			内存占用百分比：ram_usage/total_resources.ram_bytes*10000/100(小数点以后舍掉，然后加上%)
//			内存可用百分比：（total_resources.ram_bytes - ram_usage)/total_resources.ram_bytes*10000/100(小数点以后舍掉，然后加上%)
//			{
//				"account_name": "marcol521313",
//				"head_block_num": 5631866,
//				"head_block_time": "2018-07-13T07:35:03.500",
//				"privileged": false,
//				"last_code_update": "1970-01-01T00:00:00.000",
//				"created": "2018-06-11T13:13:13.000",
//				"core_liquid_balance": "1.6405 EOS",
//				"ram_quota": 7298,
//				"net_weight": 102511,
//				"cpu_weight": 102501,
//				"net_limit": {
//					"used": 466,
//					"available": 5589765,
//					"max": 5590231
//				},
//				"cpu_limit": {
//					"used": 8695,
//					"available": 1055666,
//					"max": 1064361
//				},
//				"ram_usage": 6958,
//				"total_resources": {
//					"owner": "marcol521313",
//					"net_weight": "10.2511 EOS",
//					"cpu_weight": "10.2501 EOS",
//					"ram_bytes": 7298
//				},
//				"self_delegated_bandwidth": {
//					"from": "marcol521313",
//					"to": "marcol521313",
//					"net_weight": "0.0010 EOS",
//					"cpu_weight": "0.0000 EOS"
//				},
//				"refund_request": {
//					"owner": "marcol521313",
//					"request_time": "2018-07-12T13:31:44",
//					"net_amount": "0.0000 EOS",
//					"cpu_amount": "0.5000 EOS"
//				},
//				"voter_info": {
//					"owner": "marcol521313",
//					"proxy": "",
//					"producers": ["eoscannonchn", "eoshuobipool", "eosisgravity", "eosstorebest"],
//					"staked": 10,
//					"last_vote_weight": "3910308.96195378061383963",
//					"proxied_vote_weight": "0.00000000000000000",
//					"is_proxy": 0
//				}
//			}
			return new EosResult(MLApiException.SUCCESS, jsonObjectInfo.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new EosResult(MLApiException.GET_ACCOUNT_ERR, null);
	}
	
}
