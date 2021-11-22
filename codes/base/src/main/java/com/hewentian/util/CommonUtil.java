package com.hewentian.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * <p>
 * <b>CommonUtil</b> 是 常用工具类，一些常用的，通用的方法可以写在这里
 * </p>
 * 
 * @author <a href="mailto:wentian.he@qq.com">hewentian</a>
 * @date 2016年6月15日 下午4:17:02
 * @since JDK 1.7
 */
public class CommonUtil {
	private static Logger log = Logger.getLogger(CommonUtil.class);
	public static final String TIMEZONE = "fwt|acst|awst|cast|wetdst|hmt|kst|wat|akdt|pdt|nst|mvt|cat|gst|bst|idlw|wet|cct|ist|est|cdt|nzst|mut|hst|ligt|east|nt|act|irt|sast|bdst|adt|iot|aft|cadt|fnt|sat|pst|mst|edt|eet|bt|hdt|jst|almst|eat|cetdst|zulu|met|dnt|jt|nzt|eetdst|sadt|ast|cest|akst|swt|idle|yst|fnst|nor|sst|ahst|nzdt|aest|sct|cst|acsst|mart|brst|mht|mmt|brt|ret|almt|tft|aesst|cxt|metdst|cet|ut|acst|ndt|klt|fst|wst|mdt|gmt|utc|mest|hkt";

	/**
	 * 全角字符串转换为半角字符串 <br />
	 * 全角字符与半角字符的关系 <br />
	 * 可以通过下面的程序看看Java中所有字符以及对应编码的值 <br />
	 * for (int i = Character.MIN_VALUE; i <= Character.MAX_VALUE; ++i) { <br />
	 * System.out.println(i + " " + (char)i); <br />
	 * } <br />
	 * 从输出可以看到 <br />
	 * 1.半角字符是从33开始到126结束 <br />
	 * 2.与半角字符对应的全角字符是从65281开始到65374结束 <br />
	 * 3.其中半角的空格是32.对应的全角空格是12288 <br />
	 * 4.半角和全角的关系很明显,除空格外的字符偏移量是65248(65281-33 = 65248)
	 * 
	 * @date 2016年6月15日 下午4:19:20
	 * @param fullWidthStr
	 *            非空的全角字符串
	 * @return 半角字符串
	 */
	public static String fullWidth2halfWidth(String fullWidthStr) {
		if (null == fullWidthStr || fullWidthStr.length() <= 0) {
			return "";
		}

		char[] charArray = fullWidthStr.toCharArray();

		// 对全角字符转换的char数组遍历
		for (int i = 0; i < charArray.length; ++i) {
			int charIntValue = charArray[i];

			// 如果符合转换关系,将对应下标之间减掉偏移量65248;如果是空格的话,直接做转换
			if (charIntValue >= 65281 && charIntValue <= 65374) {
				charArray[i] = (char) (charIntValue - 65248);
			} else if (charIntValue == 12288) {
				charArray[i] = (char) 32;
			}
		}

		return new String(charArray);
	}

	public static boolean isEmpty(final CharSequence cs) {
		return cs == null || cs.length() == 0;
	}

	public static boolean isNotEmpty(final CharSequence cs) {
		return !isEmpty(cs);
	}

	public static boolean isBlank(final CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNotBlank(final CharSequence cs) {
		return !isBlank(cs);
	}

	/**
	 * 这是一个简单的方法，判断传入的字符串是否为日期, 传入的日期已经经过了日期正则表达式的判断，这里只是判断它的月和日的范围
	 * 
	 * @date 2016年9月27日 下午3:41:18
	 * @param date
	 *            2016-12-12 12:32:30
	 * @return true/false
	 */
	public static boolean isDate(String date) {
		if (null == date) {
			return false;
		}

		date = date.trim();
		if (isBlank(date)) {
			return false;
		}

		try {
			String month = "";
			String day = "";

			if (date.contains("-")) {
				String[] dateA = date.split("-");
				if (dateA.length == 3) {
					month = dateA[1];
					day = dateA[2];
				} else if (dateA.length == 2) {
					month = dateA[0];
					day = dateA[1];
				}
			} else {
				if (date.length() >= 8) {
					date = date.substring(0, 8);

					// 测试前8个都是数字
					Integer.valueOf(date);

					month = date.substring(4, 6);
					day = date.substring(6, 8);
				}
			}

			// 判断月是否在1-12之间
			month = month.trim();
			int monthInt = Integer.valueOf(month).intValue();
			if (monthInt < 1 || monthInt > 12) {
				return false;
			}

			day = day.trim();
			if (day.length() >= 2) {
				day = day.substring(0, 2);
			}
			day = day.trim();
			int dayInt = Integer.valueOf(day).intValue();
			if (dayInt < 1 || dayInt > 31) {
				return false;
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}

		return true;
	}

	/**
	 * 处理时区问题, 目前暂时不处理时区
	 * 
	 * @date 2016年6月17日 下午5:16:46
	 * @param date
	 *            日期
	 * @param timeZone
	 *            时区值，为目标网站所在的时区。东半球为正，西半球为负，如中国为：8，美国为-7
	 * @return
	 */
	public static Date handleTimeZone(Date date, int timeZone) {
		if (null == date) {
			return null;
		}

		Calendar c = Calendar.getInstance();
		int currTimeZone = c.getTimeZone().getRawOffset() / 3600000;

		// 处理时差
		c.setTime(date);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		c.set(Calendar.HOUR_OF_DAY, hour + currTimeZone - timeZone);
		date = c.getTime();

		return date;
	}

	/**
	 * 获取中文日期评分
	 * 
	 * @date 2016年9月27日 下午6:42:03
	 * @param str
	 * @param datePtn
	 * @return
	 */
	public static int getScoreCN(String str, String datePtn) {
		str = str.toLowerCase().trim();

		// 部分， 繁体-简体
		str = str.replaceAll("發", "发");
		str = str.replaceAll("怖", "布");
		str = str.replaceAll("閱", "阅");
		str = str.replaceAll("讀", "读");
		str = str.replaceAll("評", "评");
		str = str.replaceAll("論", "论");
		str = str.replaceAll("舉", "举");
		str = str.replaceAll("報", "报");
		str = str.replaceAll("時", "时");
		str = str.replaceAll("鍾", "钟");
		str = str.replaceAll("間", "间");
		str = str.replaceAll("轉", "转");
		str = str.replaceAll("載", "载");
		str = str.replaceAll("來", "来");
		str = str.replaceAll("網", "网");
		str = str.replaceAll("樓", "楼");

		int strLen = str.length();

		if (!str.contains(" ") && strLen > 30) {
			return 1;
		}

		int score = 1; // 一开始为1分
		boolean isDateStr = isDateStrCN(str);
		if (isDateStr) { // 如果字符串里面全部都是日期
			score += 1;
		}

		if (str.contains("日期") || str.contains("发布") || str.contains("发表") || str.contains("来源") || str.contains("时间")) {
			score += 3;
		}

		if (str.contains("作者")) {
			score += 1;
		}

		if (str.contains("评论")) {
			score += 1;
		}

		if (str.contains("阅读")) {
			score += 1;
		}

		if (str.contains("收藏")) {
			score += 1;
		}

		if (str.contains("举报")) {
			score += 1;
		}

		if (str.contains("转载")) {
			score += 1;
		}

		if (str.contains("分享")) {
			score += 1;
		}

		if (str.contains("网") || str.contains("报") || str.contains("秒前") || str.contains("秒钟前") || str.contains("分前") || str.contains("分钟前") || str.contains("刻")
				|| str.contains("小时前") || ((str.contains("前") || str.contains("昨")) && str.contains("天"))) {
			score += 2;
		}

		if (strLen <= 20) {
			score += 2;
		} else if (strLen <= 50) {
			score += 1;
		} else if (strLen >= 60) {
			score--;
		}

		boolean ymd = datePtn.contains("MM") && datePtn.contains("dd") && datePtn.contains("yy");
		boolean md = datePtn.contains("MM") && datePtn.contains("dd");
		boolean hms = (datePtn.contains("h") || datePtn.contains("H")) && datePtn.contains("mm") && datePtn.contains("ss");
		boolean hm = (datePtn.contains("h") || datePtn.contains("H")) && datePtn.contains("mm");
		boolean a = datePtn.contains("a"); // 日期格式包含：上午、下午
		boolean z = datePtn.contains("z"); // 日期格式包含：时区

		// max 6
		if (ymd && hm && (a || z)) {
			score += 3;
		} else if (ymd && hms) {
			score += 2;
		} else if (ymd && hm || md && hm && (a || z)) {
			score += 1;
		}

		// 没有包含年的，要减一分
		if (!datePtn.contains("yy")) {
			score--;
		}

		// 包含以下字符的，可能是论坛的回复，一般也包含“发表”这样的字眼，为区别文章真正的发表时间，在这里要减分
		if (str.contains("楼主")) {
			score++;
		} else if (str.contains("楼")) {
			score--;
		}
		if (str.contains("回复")) {
			score--;
		}

		return score;
	}

	/**
	 * 获取英文日期评分
	 * 
	 * @date 2016年9月28日 上午10:39:59
	 * @param str
	 * @param datePtn
	 * @return
	 */
	public static int getScoreEN(String str, String datePtn) {
		str = str.toLowerCase().trim();
		int strLen = str.length();

		if (!str.contains(" ") && strLen > 30) {
			return 1;
		}

		int score = 1; // 一开始为1分
		boolean isDateStr = isDateStrEN(str);
		if (isDateStr) { // 如果字符串里面全部都是日期
			score += 1;
		}

		if (str.contains("update")) { // update要比publish高1分
			score += 3;
		}

		if (str.contains("publish") || str.contains("post") || str.contains("release") || str.contains("report") || str.contains("edit")) {
			score += 2;
		}

		if (str.contains("author")) {
			score += 1;
		}

		if (str.contains("share")) {
			score += 1;
		}

		if (str.contains("follow")) {
			score += 1;
		}

		if (str.contains("by")) {
			score += 1;
		}

		if (str.contains("@")) {
			score += 1;
		}

		if (str.contains("at") || str.contains("on")) {
			score += 1;
		}

		if (str.contains("@")) {
			score += 1;
		}

		if (str.contains("|") || str.contains("/")) {
			score += 1;
		}

		if (str.contains("year") || str.contains("year ago") || str.contains("years ago") || str.contains("month") || str.contains("month ago") || str.contains("months ago")
				|| str.contains("day") || str.contains("day ago") || str.contains("days ago") || str.contains("hour") || str.contains("hour ago") || str.contains("hours ago")
				|| str.contains("second") || str.contains("second ago") || str.contains("seconds ago") || str.contains("minute") || str.contains("minute ago")
				|| str.contains("minutes ago")) {
			score += 3;
		}

		if (strLen <= 50) {
			score++;
		} else if (strLen >= 80) {
			score--;
		}

		boolean ymd = datePtn.contains("MM") && datePtn.contains("dd") && datePtn.contains("yy");
		boolean md = datePtn.contains("MM") && datePtn.contains("dd");
		boolean hm = (datePtn.contains("h") || datePtn.contains("H")) && datePtn.contains("mm");
		boolean a = datePtn.contains("a"); // 日期格式包含：上午、下午
		boolean z = datePtn.contains("z"); // 日期格式包含：时区

		// max 6
		if (ymd && hm && (a || z)) {
			score += 3;
		} else if (ymd && hm || md && hm && (a || z)) {
			score += 2;
		} else if (ymd) {
			score += 1;
		}

		if (!datePtn.contains("yy")) {
			score--;
		}

		return score;
	}

	/**
	 * 判断是否都是中文日期字符串
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isDateStrCN(String s) {
		if (null == s) {
			return false;
		}

		s = s.trim();
		if ("".equals(s)) {
			return false;
		}

		boolean res = false;
		char[] ca = s.toCharArray();
		for (int i = 0, len = ca.length; i < len; i++) {
			switch (ca[i]) {
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case '0':
			case ' ':
			case ':':
			case '-':
			case '.':
			case '/':
			case '年':
			case '月':
			case '日':
			case '时':
			case '分':
			case '秒':
			case '上':
			case '下':
			case '午':
			case '星':
			case '期':
			case '一':
			case '二':
			case '三':
			case '四':
			case '五':
			case '六':
				res = true;
				break;
			default:
				res = false;
			}

			if (!res) {
				break;
			}
		}

		return res;
	}

	/**
	 * 判断是否都是英文日期字符串
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isDateStrEN(String s) {
		if (null == s) {
			return false;
		}

		s = s.toLowerCase().trim();
		if ("".equals(s)) {
			return false;
		}

		boolean res = false;
		char[] ca = s.toCharArray();
		for (int i = 0, len = ca.length; i < len; i++) {
			switch (ca[i]) {
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case '0':
			case ' ':
			case ':':
			case '+':
			case '-':
			case '/':
			case ',':
			case '.':
				res = true;
				break;
			default:
				res = false;
			}

			if (!res) {
				break;
			}
		}

		// 如果发现都是日期马上返回
		if (res) {
			return res;
		}

		// 进一步判断日期
		// Wednesday, August 24, 2016 0:16
		String[] sa = s.split("[ .,:+-/]");
		if (sa.length == 1) {
			return false;
		}

		for (String ss : sa) {
			if ("".equals(ss)) {
				res = true;
				continue;
			}

			char c = ss.charAt(0);
			if (c >= '0' && c <= '9') {
				res = true;
				continue;
			}

			if (c >= 'a' && c <= 'z') {
				if (ss.startsWith("mon") || ss.startsWith("tues") || ss.startsWith("wed") || ss.startsWith("thur") || ss.startsWith("fri") || ss.startsWith("sat")
						|| ss.startsWith("sun")
						// 月份
						|| ss.startsWith("jan") || ss.startsWith("feb") || ss.startsWith("mar") || ss.startsWith("apr") || ss.startsWith("may") || ss.startsWith("jun")
						|| ss.startsWith("jul") || ss.startsWith("aug") || ss.startsWith("sep") || ss.startsWith("oct") || ss.startsWith("nov") || ss.startsWith("dec")
						// 国家
						|| ss.equals("uk") || ss.equals("us") || ss.equals("ca")
						// timeZone && am pm
						|| ss.equals("et") || ss.equals("at") || ss.equals("am") || ss.equals("pm") || ss.equals("a") || ss.equals("p") || ss.equals("m") || isTimeZoneStr(ss)
						// utc
						|| ss.equals("t") || ss.equals("z")) {
					res = true;
				} else {
					res = false;
				}
			}

			if (!res) {
				break;
			}
		}

		return res;
	}

	/**
	 * 将et等非标准的时区转换成标准的est
	 * 
	 * @date 2016年10月21日 上午11:47:40
	 * @param dateStr
	 *            要求都是小写
	 * @return
	 */
	public static String transformTimeZone(String dateStr) {
		// 将et转换成标准的est
		if (null == dateStr) {
			return null;
		}

		dateStr = dateStr.trim();
		int etIndex = dateStr.indexOf("et");
		if (etIndex == 0) {
			if (dateStr.equals("et")) {
				return "est";
			}

			char afterChar = dateStr.charAt(2);
			if (afterChar == ' ' || afterChar == ',' || afterChar == '.') {
				dateStr = dateStr.replace("et", "est");
			}
		} else if ((etIndex > 0)) {
			char beforeChar = dateStr.charAt(etIndex - 1);
			if (beforeChar == ' ') {
				dateStr = dateStr.replace("et", "est");
			}
		}

		return dateStr;
	}

	/**
	 * 判断传入的字符串是否是时区的简写
	 * 
	 * @date 2016年10月21日 下午4:38:36
	 * @param str
	 *            时区的str
	 * @return true/false
	 */
	public static boolean isTimeZoneStr(String str) {
		if (null == str) {
			return false;
		}

		str = str.trim().toLowerCase();

		boolean res = false;
		for (String s : TIMEZONE.split("\\|")) {
			if (str.equals(s)) {
				res = true;
				break;
			}
		}

		return res;
	}

	/**
	 * 获取靠近标题的加分值
	 * 
	 * @date 2016年10月26日 上午11:40:43
	 * @param country
	 *            国家的简写，如中国为CN, 台湾地区为TW， 香港地区为HK, 要求：not null
	 * @param language
	 *            国家所使用的语言, 如中国大陆、香港、台湾为zh, 英语为：en, 要求：not null
	 * @param level
	 *            权值，越小越高
	 * @return int 整型值
	 */
	public static int getNearTitleScore(String country, String language, int level) {
		if (null == country || null == language) {
			return 0;
		}

		country = country.trim();
		language = language.trim();
		int score = 1;

		// 首先按语系来分, 再按国家来分
		// 目前都返回1分，到时按需调整
		if ("zh".equals(language)) {
			if ("CN".equals(country) || "TW".equals(country) || "HK".equals(country)) {
				score = 1;
			}
		} else if ("en".equals(language)) {
			if ("US".equals(country) || "GB".equals(country)) {
				score = 1;
			}
		}

		return score;
	}

	/**
	 * 计算传入的字符串中字的个数, 可以处理中英文混合的情况。
	 * <ul>
	 * <li>A.对于中文，一个汉字算一个;</li>
	 * <li>B.对于英文，一个单词算一个.</li>
	 * </ul>
	 * 
	 * @date 2016年10月28日 上午11:47:10
	 * @param str
	 *            要计算的字符串
	 * @return int
	 */
	public static int countWords(String str) {
		if (null == str) {
			return 0;
		}

		str = str.trim();
		if ("".equals(str)) {
			return 0;
		}

		// 首先转换为半角并转换成小写
		str = fullWidth2halfWidth(str);
		str = str.toLowerCase();

		// 将str中的多个空格替换为一个
		str = str.replaceAll("\\s+", " ");

		int count = 0; // 字的个数

		boolean preIsLetter = false; // 前一个字符是否为英文
		for (int i = 0, len = str.length(); i < len; i++) {
			char c = str.charAt(i);

			if (c == 39 || c == 45 || c >= 48 && c <= 57 || c == 95 || c >= 97 && c <= 122) { // 标识英文单词开始,
																								// '-[0-9]_
				preIsLetter = true;
			} else if (c >= 32 && c <= 34 || c == 44 || c == 46 || c == 58 || c == 59 || c == 63) { // 如果是分隔符,
																									// 空格!",.:;?
				if (preIsLetter) {
					preIsLetter = false;
					count++;
				}
			} else { // 是汉字或其他字符, 有可能英文后面直接跟了中文而不跟分隔符
				if (preIsLetter) {
					preIsLetter = false;
					count++;
				}
				count++;
			}
		}

		if (preIsLetter) {
			count++;
		}

		return count;
	}
	
	/**
	 * 检测网络资源是否存在
	 *
	 * @param strUrl
	 * @return
	 */
	public static boolean isNetResourceAvailable(String strUrl) {
		InputStream netFileInputStream = null;

		try {
			URL url = new URL(strUrl);
			URLConnection urlConn = url.openConnection();
			netFileInputStream = urlConn.getInputStream();
			if (null != netFileInputStream) {
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			return false;
		} finally {
			try {
				if (netFileInputStream != null)
					netFileInputStream.close();
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * trim无法去掉 ascii 160 的空格
	 * @param s
	 * @return
	 */
	public static String trim(String s) {
		if (null == s || s.length() == 0) {
			return s;
		}

		s = s.replaceAll("[\\u00A0]+", "");
		s = s.trim();
		return s;
	}

	// 将字节转成16进制数的表达方式。
	public static String byte2hex(byte[] b) {
		String hs = " ";
		String stmp = " ";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0 " + stmp;
			else
				hs = hs + stmp;
			if (n < b.length - 1)
				hs = hs + ": ";
		}
		return hs.toUpperCase();
	}

	/**
	 * Regex for converting CamelCase to camel_case
	 *
	 * @param s
	 * @return
	 */
	public static String camelCase2camelCaseUnderscore(String s) {
		String regex = "([a-z])([A-Z]+)";
		String replacement = "$1_$2";
		s = s.replaceAll(regex, replacement).toLowerCase();

		return s;
	}

	public static void main(String[] args) {
		// System.out.println(fullWidth2halfWidth("a９；ａｂｃａｂ")); // output:
		// a9;abcab
		// System.out.println(isDateStr("2016-09-18 人17:30"));

		// System.out.println(getScoreCN("发表于：2016-10-26 08:34:48 作者:霍元甲回归 发短信 加关注 更多作品 级别：元帅 积分：166882","yyyy-MM-dd HH:mm:ss"));
		// System.out.println(getScoreCN("ETtoday五周年生日趴!超強第二彈~天天喝7-11冰咖啡，再抽西堤牛排餐券，迎接幸福的秋天!",
		// "MM-dd"));
		// System.out.println(getScoreCN("X3.1", "MM-dd"));
		// System.out.println(getScoreCN("© 2001-2013 Comsenz Inc.",
		// "MM-ddHH"));
		// System.out.println(getScoreCN("(1.12 KB, 下载次数: 0)", "MM-dd"));
		// System.out.println(getScoreCN("发表于 2016-7-17 11:09:00",
		// "yyyy-MM-ddHH:mm:ss"));
		// System.out.println(getScoreEN("Wednesday, August 24, 2016 0:16",
		// "MMM dd, yyyy HH:mm"));
		// System.out.println(isDateStrEN("Wednesday, August 24, 2016 0:16"));
		// System.out.println(isDateStrEN("2016-07-26T23:54:34+0900"));
		// System.out.println(transformTimeZone("et"));
		// System.out.println(transformTimeZone("oct 19, 2016, 3:15 pm ea"));
		// System.out.println(transformTimeZone("Oct 19, 2016, 3:15 PM wetdst"));
		// System.out.println(transformTimeZone("Oct 19, 2016, 3:15 et2"));
		// System.out.println(isDateStrEN("Oct 7, 2016, 08.58 AM IST"));
		// System.out.println(isDateStrEN("Jan. 11, 2006 at 12:50 PM "));
		// System.out.println(isDateStrEN("8/27/2012 8:06 AM"));
		// System.out.println(isDateStrEN("Wednesday 5 October 2016 14:20 BST"));
		// System.out.println(isDateStrEN("Thursday, Mar. 22, 2012s 7:59AM EDT "));
		// System.out.println(getNearTitleScore(null, ""));
		// System.out.println(getNearTitleScore("CN", "zh"));
		// System.out.println(getNearTitleScore("US", "en"));

		System.out.println(countWords("张三feng")); // 3
		System.out.println(countWords("zhang三feng")); // 3
		System.out.println(countWords("中华人民共和国!")); // 7
		System.out.println(countWords("sun yat sen university.")); //
		System.out.println(countWords("I'm a software engineer."));
		System.out.println(countWords("Putin is Russian president."));
		System.out.println(countWords("我今天和Putin一起去Tokyo吃饭"));
		System.out.println(countWords("你好"));
		System.out.println(countWords("hello word ?"));
		System.out.println(countWords("one's word"));
		System.out.println(countWords("100-50"));
		System.out.println("-----------------------");
		System.out.println(countWords("发表于 2016-7-17 11:09:00"));
		System.out.println(countWords("By SHAUN WATERMAN, UPI Homeland and National Security Editor   |  	Dec. 7, 2006 at 7:32 AM	Follow @upi"));
		System.out.println(countWords("2015年03月18日 05:38 来源：中国证券报 作者：张玉洁 用微信扫描二维码 分享至好友和朋友圈 人参与 评论"));
		System.out.println("2015年03月18日 05:38 来源：中国证券报 作者：张玉洁 用微信扫描二维码 分享至好友和朋友圈 人参与 评论".length());
		System.out.println(countWords("2015年03月18日 05:38"));
	}
}