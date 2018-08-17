package it.etoken.component.api.eosaction;

import com.alibaba.fastjson.JSONObject;

public abstract class EosAction {

	public static EosAction getEosAction(String func) {
		if (EosActionEnum.TRANSFER.func.equals(func)) {
			return new Transfer();
		} else if (EosActionEnum.DELEGATEBW.func.equals(func)) {
			return new Delegatebw();
		} else if (EosActionEnum.UNDELEGATEBW.func.equals(func)) {
			return new UnDelegatebw();
		} else if (EosActionEnum.BUY_RAM.func.equals(func)) {
			return new BuyRam();
		} else if (EosActionEnum.SELL_RAM.func.equals(func)) {
			return new SellRam();
		}
		return null;
	}

	public abstract JSONObject getDetail(JSONObject action, JSONObject params);

	public enum EosActionEnum {
		TRANSFER("::transfer", "转账", ""), 
		DELEGATEBW("eosio::delegatebw", "抵押", "转出"), 
		UNDELEGATEBW("eosio::undelegatebw", "赎回", "转入"), 
		BUY_RAM("eosio::buyram", "内存购买", "转出"), 
		SELL_RAM("eosio::sellram", "内存出售", "转入");

		private EosActionEnum(String func, String description, String type) {
			this.func = func;
			this.description = description;
			this.type = type;
		}

		private String func;
		private String description;
		private String type;

		public String getFunc() {
			return func;
		}

		public void setFunc(String func) {
			this.func = func;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
	}
}