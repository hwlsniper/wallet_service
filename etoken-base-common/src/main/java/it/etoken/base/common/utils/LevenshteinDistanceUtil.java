package it.etoken.base.common.utils;
/**
 * 相似度对比工具
 * @author admin
 *
 */
public class LevenshteinDistanceUtil {
	private static int minimum(int a, int b, int c) {
		return Math.min(Math.min(a, b), c);
	}

	public static int computeLevenshteinDistance(CharSequence lhs, CharSequence rhs) {
		int[][] distance = new int[lhs.length() + 1][rhs.length() + 1];

		for (int i = 0; i <= lhs.length(); i++)
			distance[i][0] = i;
		for (int j = 1; j <= rhs.length(); j++)
			distance[0][j] = j;

		for (int i = 1; i <= lhs.length(); i++)
			for (int j = 1; j <= rhs.length(); j++)
				distance[i][j] = minimum(distance[i - 1][j] + 1, distance[i][j - 1] + 1,
						distance[i - 1][j - 1] + ((lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1));

		return distance[lhs.length()][rhs.length()];
	}
	
	public static double similarity(CharSequence lhs, CharSequence rhs) {
		int cld = LevenshteinDistanceUtil.computeLevenshteinDistance(lhs, rhs);
		double result = 1 - (double)cld / Math.max(lhs.length(), rhs.length());
		return result;
	}
	
	public static void main(String[] argc) {
		LevenshteinDistanceUtil a=new LevenshteinDistanceUtil();
//		String a1="据《币 世 界》行情显示，主流币种EOS处于领涨地位，现价12.11美元，涨幅0.98%。EOS在24小时资金净流出6598美元，24小时内涨幅30.80%。据悉EOS主网在6月2日上线。";
//		String a2="据《币 世 界》行情显示，主流币种EOS处于领涨地位，现价12.11美元，涨幅0.98%。EOS在24小时资金净流出6598美元，24小时内涨幅30.80%。";
//		String a3="bbbbbbbb";
//		String a4="aaaaaaab";
		String a5="BM 回应为何更倾向于 BP 们做决策而不是仲裁员";
		String a6="BM 回应为何更倾向于 BP 们做决策而不是仲裁员";
		String a7="据 IMEOS 报道，BM 今日发表的《The “Intent of Code” is Law（代码即法律）》一文在 EOS 治理群中引发大量讨论，有人认为虽说同意 BM 文中对决策人的决策范围进行细化，但是不能理解为何 BM 更倾向于 BP 们做决策而不是仲裁员。BM 回应：因为BP 们是具有技术能力来执行变更并核实代币持有者账户余额的人。";
		String a8="据 IMEOS 报道，BM 今日发表的《The “Intent of Code” is Law（代码即法律）》一文在 EOS 治理群中引发大量讨论，有人认为虽说同意 BM 文中对决策人的决策范围进行细化，但是不能理解为何 BM 更倾向于 BP 们做决策而不是仲裁员。BM 回应：因为BP 们是具有技术能力来执行变更并核实代币持有者账户余额的人。";
//		double r1 = a.similarity(a1, a2);
//		double r2 = a.similarity(a1, a3);
//		double r3 = a.similarity(a1, a4);
//		double r4 = a.similarity(a4, a3);
		double r5 = a.similarity(a5, a6);
		double r6 = a.similarity(a7, a8);
	
		
//		System.out.println("result1:"+ r1);
//		System.out.println("result2:"+ r2);
//		System.out.println("result1:"+ r3);
//		System.out.println("result2:"+ r4);
		System.out.println("result5:"+ r5);
		System.out.println("result6:"+ r6);
		if(r5>0.9&&r6>0.8) {
			System.out.println("是的相似度高");
		}else {
			System.out.println("不是的相似度低");
		}
		
//		String content = "<p><span style=\"font-size: 24px;\"><strong>EOS将大量ETH转移至Bitfinex,引发市场担忧</strong></span></p><p><br/></p><p>据bitcoinist消息，以太坊公有链证实EOS最近向Bitfinex发送了20万个ETH，致使EOS的ICO智能合约价值超100万美元的ETH。短时大规模的转移，引发市场担忧。</p><p><br/></p><p>截至发稿时，EOS价值￥93.9（来源：Gate.io）</p><p><a href=\"http://static.eostoken.im/invite/down.html\" target=\"_blank\"><img src=\"http://static.eostoken.im/ran/update/down.jpg\" width=\"100%\"/></a></p>";
//		System.out.println(content);
//		content = content.replaceAll("<a href=\"(.*?)\".*?>(.*?)</a>", "");
//		content = content.replaceAll("<img src=\"(.*?)\".*?>(.*?)</img>", "");
//		
//		System.out.println(content);
		
		long x = 1529431800;
		
	}

}
