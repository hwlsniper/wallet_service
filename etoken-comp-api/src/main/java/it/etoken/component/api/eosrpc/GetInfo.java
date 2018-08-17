package it.etoken.component.api.eosrpc;

import org.springframework.stereotype.Component;

@Component
public class GetInfo extends EosRpc {

	@Override
	String url() {
		return "get_info";
	}

}
