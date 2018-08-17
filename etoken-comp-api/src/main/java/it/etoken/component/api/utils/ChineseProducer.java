package it.etoken.component.api.utils;

import java.util.Random;

import com.google.code.kaptcha.text.TextProducer;

public class ChineseProducer implements TextProducer{

	private String[] simplifiedChineseTexts = new String[]{
			"五光十色","欢声雷动","欣喜若狂","载歌载舞","灯火辉煌","春暖花开","春色满园","春光明媚","春意盎然","春回大地","兴致勃勃","精卫填海","愚公移山","百折不回","勇往直前","骨肉之情"," 痛痒相关 ","一唱一和","一呼百应","一干二净","一举两得","一落千丈","一模一样","一暴十寒","一日千里","一五一十","一心一意","两面三刀","三长两短","三番五次","三三两两","三头六臂","三心二意","三言两语","四分五裂","四面八方","四通八达","四平八稳","五光十色","五湖四海","五花八门",
			"淅淅沥沥","雨声沙沙","细雨淅沥","春雨连绵","雨过天晴","细雨如丝","春雨阵阵","含苞欲放","风和日丽","和风细雨","柳绿花红","气象万千","欣欣向荣","莺歌燕舞","春光明媚","春花秋月","春山如笑","春深似海","春生秋杀","春笋怒发"
	};

	/**
	 * @return random Chinese text
	 */
	public String getText()
	{
		return simplifiedChineseTexts[new Random().nextInt(simplifiedChineseTexts.length)];
	}
}
