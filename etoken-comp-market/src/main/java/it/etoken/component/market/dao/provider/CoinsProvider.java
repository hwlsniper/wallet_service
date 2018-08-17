package it.etoken.component.market.dao.provider;

import org.apache.ibatis.jdbc.SQL;

import it.etoken.base.model.market.entity.Coins;

public class CoinsProvider {
	
	public String update(final Coins coins) {
        return new SQL() {
            {
                UPDATE("coins");
                if (coins.getName() != null) {
                    SET("name = #{name}");
                }
                if (coins.getIntr() != null) {
                    SET("intr = #{intr}");
                }
                if (coins.getSite() != null) {
                    SET("site = #{site}");
                }
                if (coins.getImg() != null) {
                    SET("img = #{img}");
                }
                if (coins.getTag() != null) {
                    SET("tag = #{tag}");
                }
                if (coins.getTotal() != null) {
                    SET("total = #{total}");
                }
                if (coins.getMarke() != null) {
                    SET("marke = #{marke}");
                }
                if (coins.getCode() != null) {
                    SET("code = #{code}");
                }
                if (coins.getSymble() != null) {
                    SET("symble = #{symble}");
                }
                if (coins.getContractAccount() != null) {
                    SET("symble = #{contractAccount}");
                }
                if (coins.getIsSupportMarket() != null) {
                    SET("symble = #{isSupportMarket}");
                }
                WHERE("id = #{id}");
            }
        }.toString();
    }
}
