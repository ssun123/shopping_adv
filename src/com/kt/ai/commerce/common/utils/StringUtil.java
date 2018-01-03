package com.kt.ai.commerce.common.utils;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 문자열 처리 class.
 * 
 * @author 
 * @vaersion 
 */
public class StringUtil extends StringUtils {

	/** The log. */
	private static Log log = LogFactory.getLog(StringUtil.class);

	/**
	 * Instantiates a new string util.
	 */
	private StringUtil() {
	};

	/**
	 * Test Main Function.
	 * 
	 * @throws ParseException
	 * 
	 */
	public static void main(String[] args) throws ParseException {

	}

	/**
	 * String Required Validate Check.
	 * 
	 */
	public static boolean validateRequired(String value) {
		return (value != null && value.trim().length() > 0);
	}

	/**
	 * String Validate Check.
	 * 
	 */
	public static boolean validateString(String str) {
		return validateRequired(str);
	}

	/**
	 * String Validate Check.
	 * 
	 */
	public static boolean validateInt(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 부적합 문자 포함여부 체크
	 * 
	 */
	public static boolean CheckValidateString(String str) {
		try {
			CharSequence s = "|&;$%@'\"<>()+,\\";
			for (int i = 0; i < s.length(); i++) {
				// System.out.println(s.charAt(i));
				if (str.indexOf(s.charAt(i)) > -1)
					return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Email 내 부적합 문자 포함여부 체크
	 * 
	 */
	public static boolean CheckValidateEmail(String str) {
		try {
			CharSequence s = "|&;$%'\"<>()+,\\";
			for (int i = 0; i < s.length(); i++) {
				// System.out.println(s.charAt(i));
				if (str.indexOf(s.charAt(i)) > -1)
					return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * String is String Check.
	 * 
	 * @param realFilePath
	 *            the real file path
	 * @return the extension
	 */
	public static String strIsString(String str, String replace) {
		if (validateString(str) && CheckValidateString(str))
			return str;
		else
			return replace;
	}

	/**
	 * String is String Check.
	 * 
	 * @param realFilePath
	 *            the real file path
	 * @return the extension
	 */
	public static String strIsString(Object str, String replace) {
		if (str != null && validateString(str.toString()) && CheckValidateString(str.toString()))
			return str.toString();
		else
			return replace;
	}

	/**
	 * String is Number Check.
	 * 
	 * @param realFilePath
	 *            the real file path
	 * @return the extension
	 */
	public static String strIsNumber(String str, String replace) {
		if (validateInt(str))
			return str;
		else
			return replace;
	}

	/**
	 * String is String Check.
	 * 
	 * @param realFilePath
	 *            the real file path
	 * @return the extension
	 */
	public static String strIsEmail(String str, String replace) {
		if (validateString(str) && CheckValidateEmail(str))
			return str;
		else
			return replace;
	}

	/**
	 * int 숫자변환.
	 * 
	 * @param str
	 *            the str
	 * @return the int
	 */
	public static int parseInt(String str) {
		try {
			if (str == null) {
				return 0;
			}
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * int 숫자변환.
	 * 
	 * @param obj
	 *            the obj
	 * @return the int
	 */
	public static int parseInt(Object obj) {
		try {
			if (obj == null) {
				return 0;
			}
			return Integer.parseInt(String.valueOf(obj));
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * long 숫자변환.
	 * 
	 * @param str
	 *            the str
	 * @return the long
	 */
	public static long parseLong(String str) {
		try {
			if (str == null) {
				return 0L;
			}
			return Long.parseLong(str);
		} catch (NumberFormatException e) {
			return 0L;
		}
	}

	/**
	 * long 숫자변환.
	 * 
	 * @param obj
	 *            the obj
	 * @return the long
	 */
	public static long parseLong(Object obj) {
		try {
			if (obj == null) {
				return 0L;
			}
			return Long.parseLong(String.valueOf(obj));
		} catch (NumberFormatException e) {
			return 0L;
		}
	}

	/**
	 * double 숫자변환.
	 * 
	 * @param str
	 *            the str
	 * @return the double
	 */
	public static double parseDouble(String str) {
		try {
			if (str == null) {
				return 0.0d;
			}
			return Double.parseDouble(str);
		} catch (NumberFormatException e) {
			return 0.0d;
		}
	}

	/**
	 * 숫자형인지 판단한다. 소숫점도 숫자형으로 판단한다.
	 * 
	 * @param str
	 *            the str
	 * @return true, if is number
	 */
	public static boolean isNumber(String str) {
		if (str == null)
			return false;
		Pattern p = Pattern.compile("([\\p{Digit}]+)(([.]?)([\\p{Digit}]+))?");
		Matcher m = p.matcher(str);
		return m.matches();
	}

	/**
	 * float 숫자변환.
	 * 
	 * @param str
	 *            the str
	 * @return the float
	 */
	public static float parseFloat(String str) {
		try {
			if (str == null) {
				return 0.0f;
			}
			return Float.parseFloat(str);
		} catch (NumberFormatException e) {
			return 0.0f;
		}
	}

	/**
	 * int 형을 문자형으로 변환.
	 * 
	 * @param src
	 *            the src
	 * @return the string
	 */
	public static String toString(int src) {
		String rst = "";
		try {
			rst = String.valueOf(src);
		} catch (Exception e) {
			return "";
		}
		return rst;
	}

	/**
	 * long 형을 문자열으로 변환.
	 * 
	 * @param src
	 *            the src
	 * @return the string
	 */
	public static String toString(long src) {
		String rst = "";
		try {
			rst = String.valueOf(src);
		} catch (Exception e) {
			return "";
		}
		return rst;
	}

	/**
	 * double 형을 문자형으로 변환.
	 * 
	 * @param src
	 *            the src
	 * @return the string
	 */
	public static String toString(double src) {
		String rst = "";
		try {
			rst = String.valueOf(src);
		} catch (Exception e) {
			return "";
		}
		return rst;
	}

	/**
	 * float형을 문자형으로 변환.
	 * 
	 * @param src
	 *            the src
	 * @return the string
	 */
	public static String toString(float src) {
		String rst = "";
		try {
			rst = String.valueOf(src);
		} catch (Exception e) {
			return "";
		}
		return rst;
	}

	/**
	 * URL encode.
	 * 
	 * @param src
	 *            the src
	 * @return the string
	 */
	public static String URLEncode(String src) {
		try {
			return URLEncoder.encode(src, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return src;
		}
	}

	/**
	 * URL decode.
	 * 
	 * @param src
	 *            the src
	 * @return the string
	 */
	public static String URLDecode(String src) {
		try {
			return URLDecoder.decode(src, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return src;
		}
	}

	/**
	 * 스트링내의 임의의 문자열을 새로운 문자열로 대치하는 메소드.
	 * 
	 * @param source
	 *            스트링
	 * @param before
	 *            바꾸고자하는 문자열
	 * @param after
	 *            바뀌는 문자열
	 * @return 변경된 문자열
	 */
	public static String change(String source, String before, String after) {
		int i = 0;
		int j = 0;
		if (source == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer();

		while ((j = source.indexOf(before, i)) >= 0) {
			sb.append(source.substring(i, j));
			sb.append(after);
			i = j + before.length();
		}

		sb.append(source.substring(i));
		return sb.toString();
	}

	/**
	 * Gets the string no tag.
	 * 
	 * @param src
	 *            the src
	 * @param len
	 *            the len
	 * @param tail
	 *            the tail
	 * @return the string no tag
	 */
	public static String getStringNoTag(String src, int len, String tail) {
		String rst = getStringNoTag(src, len);
		return rst += tail;
	}

	/**
	 * Tag 제거 후 자르기.
	 * 
	 * @param src
	 *            the src
	 * @param len
	 *            the len
	 * @return the string no tag
	 */
	public static String getStringNoTag(String src, int len) {
		float rstLen = 0;
		String rst = "";
		char b[] = src.toCharArray();
		int i = 0;
		float tmplen = 0;
		String tmpRst = "";
		boolean tmpb = true;
		for (i = 0; i < b.length; i++) {
			if (b[i] == '<') { /* < 시작하는거 체크 */
				tmplen = rstLen;
				tmpRst = rst;
				tmpb = false;
			} else if (b[i] == '>') { /* >끝나는거 체크 */
				rstLen = tmplen;
				rst = tmpRst;
				tmpb = true;
			} else if (src.charAt(i) > 255) { /* 한글로 시작하는 부분 체크 */
				rstLen += 1.21;
				rst += src.substring(i, i + 1);

			} else if ((byte) b[i] >= 97 && (byte) b[i] <= 122) { /*
																 * 영문(소문자)으로
																 * 시작하는 부분 체크
																 */
				rstLen += 0.71;
				rst += src.substring(i, i + 1);
			} else if ((byte) b[i] >= 65 && (byte) b[i] <= 90) { /*
																 * 영문(대문자)으로
																 * 시작하는 부분 체크
																 */
				rstLen += 0.82;
				rst += src.substring(i, i + 1);
			} else if ((byte) b[i] >= 48 && (byte) b[i] <= 57) { /* 숫자 인지 체크 */
				rstLen += 0.61;
				rst += src.substring(i, i + 1);
			} else { /* 특수 문자 기호값 */
				rstLen += 0.71;
				rst += src.substring(i, i + 1);
			}
			if ((int) rstLen >= len) {
				if (tmpb) {
					rst += "..";
					break;
				}
			}
		}
		return rst;
	}

	/**
	 * Adds the br.
	 * 
	 * @param src
	 *            the src
	 * @return the string
	 */
	public static String addBr(String src) {
		return change(src, "\n", "<BR>");
	}

	/**
	 * 문자열에 있는 태그를 제거하여 반환한다.
	 * 
	 * @param str
	 *            the str
	 * @return the string
	 */
	public static String removeTagScript(String str) {
		return str.replaceAll("\\<.*?\\>", "");
	}

	/**
	 * 스트링 잘라내기.
	 * 
	 * @param s
	 *            잘라낼 문자열
	 * @param len
	 *            잘라낼 길이
	 * @param tail
	 *            잘라낸 문자열 뒤에 붙일 문자열
	 * @return the string
	 */
	public static String cutString(String s, int len, String tail) {

		if (s == null) {
			return null;
		}

		int srcLen = realLength(s);
		if (srcLen < len) {
			return s;
		}

		String tmpTail = tail;
		if (tail == null) {
			tmpTail = "";
		}

		int tailLen = realLength(tmpTail);
		if (tailLen > len) {
			return "";
		}

		char a;
		int i = 0;
		int realLen = 0;
		for (i = 0; i < len - tailLen && realLen < len - tailLen; i++) {
			a = s.charAt(i);

			if ((a & 0xFF00) == 0) {
				realLen += 1;
			} else {
				realLen += 2;
			}
		}

		while (realLength(s.substring(0, i)) > len - tailLen) {
			i--;
		}

		return s.substring(0, i) + tmpTail;
	}

	/**
	 * 핸드폰 번호, 전화번호 ('-') 삽입
	 */

	public static String fullPhoneNumber(String fir, String mid, String lat) {

		String number = fir + "-" + mid + "-" + lat;
		return number;
	}

	/**
	 * 문자열의 바이트를 반환한다.
	 * 
	 * @param s
	 *            the s
	 * @return the int
	 */
	public static int realLength(String s) {
		// return s.getBytes().length;
		return s.length();
	}

	/**
	 * 메일 유효성 검증.
	 * 
	 * @param email
	 *            the email
	 * @return true, if is email
	 */
	public static boolean isEmail(String email) {
		if (email == null) {
			return false;
		}

		boolean b = Pattern.matches("[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+", email.trim());

		return b;
	}

	/**
	 * 핸드폰 번호 Validate.
	 * 
	 * @param mobile
	 *            the mobile
	 * @return true, if is mobile
	 */
	public static boolean isMobile(String mobile) {
		if (mobile == null) {
			return false;
		}

		boolean b = Pattern.matches("01[016789]\\-\\d{2,4}\\-\\d{3,4}", mobile.trim());

		return b;
	}

	/**
	 * 특수 문자 와 영문 한글 체크 해서 길이 를 가지고 온다.
	 * 
	 * @param src
	 *            the src
	 * @param len
	 *            the len
	 * @param tail
	 *            the tail
	 * @return the string
	 */
	public static String getString(String src, int len, String tail) {
		if (src == null) {
			return "";
		}
		float rstLen = 0;
		String rst = "";
		char c[] = src.toCharArray();
		int i = 0;
		for (i = 0; i < c.length; i++) {
			if (c[i] == 60) { /* < 시작하는거 체크 */
				rstLen += 1;
				rst += src.substring(i, i + 1);
			} else if ((byte) c[i] == 62) { /* >끝나는거 체크 */
				rstLen += 1;
				rst += src.substring(i, i + 1);
			} else if (src.charAt(i) > 255) { /* 한글로 시작하는 부분 체크 */
				rstLen += 1.21;
				rst += src.substring(i, i + 1);
			} else if ((byte) c[i] >= 97 && (byte) c[i] <= 122) { /*
																 * 영문(소문자)으로
																 * 시작하는 부분 체크
																 */
				rstLen += 0.71;
				rst += src.substring(i, i + 1);
			} else if ((byte) c[i] >= 65 && (byte) c[i] <= 90) { /*
																 * 영문(대문자)으로
																 * 시작하는 부분 체크
																 */
				rstLen += 0.82;
				rst += src.substring(i, i + 1);
			} else if ((byte) c[i] >= 48 && (byte) c[i] <= 57) { /* 숫자 인지 체크 */
				rstLen += 0.61;
				rst += src.substring(i, i + 1);
			} else { /* 특수 문자 기호값 */
				rstLen += 0.71;
				rst += src.substring(i, i + 1);
			}
			// System.out.println((int) src.charAt(i));
			if (rstLen >= len) {
				rst += tail;
				break;
			}
		}
		return rst;
	}

	/**
	 * 특수 문자 와 영문 한글 체크 해서 길이 를 가지고 온다.
	 * 
	 * @param src
	 *            the src
	 * @return the string length
	 */
	public static int getStringLength(String src) {
		if (src == null) {
			return 0;
		}
		float rstLen = 0;
		String rst = "";
		char c[] = src.toCharArray();
		int i = 0;
		for (i = 0; i < c.length; i++) {
			if (c[i] == 60) { /* < 시작하는거 체크 */
				rstLen += 1;
				rst += src.substring(i, i + 1);
			} else if ((byte) c[i] == 62) { /* >끝나는거 체크 */
				rstLen += 1;
				rst += src.substring(i, i + 1);
			} else if (src.charAt(i) > 255) { /* 한글로 시작하는 부분 체크 */
				rstLen += 1.21;
				rst += src.substring(i, i + 1);
			} else if ((byte) c[i] >= 97 && (byte) c[i] <= 122) { /*
																 * 영문(소문자)으로
																 * 시작하는 부분 체크
																 */
				rstLen += 0.71;
				rst += src.substring(i, i + 1);
			} else if ((byte) c[i] >= 65 && (byte) c[i] <= 90) { /*
																 * 영문(대문자)으로
																 * 시작하는 부분 체크
																 */
				rstLen += 0.82;
				rst += src.substring(i, i + 1);
			} else if ((byte) c[i] >= 48 && (byte) c[i] <= 57) { /* 숫자 인지 체크 */
				rstLen += 0.61;
				rst += src.substring(i, i + 1);
			} else { /* 특수 문자 기호값 */
				rstLen += 0.71;
				rst += src.substring(i, i + 1);
			}
		}
		return Math.round(rstLen);
	}

	/**
	 * 문자열이 서로 같은지 확인한다.
	 * 
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 * @return true, if is equals
	 */
	public static boolean isEquals(String a, String b) {
		if (a == null)
			return false;
		if (b == null)
			return false;
		return a.equals(b);
	}

	/**
	 * 같으문자면 지정한 문자열(success) 반환 문자가 서로 같지 않으면 문자열(fail) 반환.
	 * 
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 * @param success
	 *            the success
	 * @param fail
	 *            the fail
	 * @return the string
	 */
	public static String isEquals(String a, String b, String success, String fail) {
		if (StringUtil.isEquals(a, b)) {
			return success;
		} else {
			return fail;
		}
	}

	/**
	 * a , b 문자가 서로 같으면 selected 를 반환.
	 * 
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 * @return the string
	 */
	public static String isSelected(String a, String b) {
		return StringUtil.isEquals(a, b, "selected", "");
	}

	/**
	 * Checks if is selected.
	 * 
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 * @param c
	 *            the c
	 * @return the string
	 */
	public static String isSelected(String a, String b, String c) {
		return StringUtil.isEquals(a, b, "selected", c);
	}

	/**
	 * a , b 문자가 서로 같으면 checked 를 반환.
	 * 
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 * @return the string
	 */
	public static String isChecked(String a, String b) {
		return StringUtil.isEquals(a, b, "checked", "");
	}

	/**
	 * 3자리마다 콤바찍기.
	 * 
	 * @param n
	 *            the n
	 * @return the string
	 */
	public static String format(long n) {
		NumberFormat nf = NumberFormat.getNumberInstance();
		try {
			return nf.format(n);
		} catch (Exception e) {
			return "0";
		}
	}

	/**
	 * 3자리마다 콤바찍기.
	 * 
	 * @param n
	 *            the n
	 * @return the string
	 */
	public static String format(String n) {
		n = isEmpty(n, "0");
		NumberFormat nf = NumberFormat.getNumberInstance();
		long l = parseLong(n);
		try {
			return nf.format(l);
		} catch (Exception e) {
			return "0";
		}
	}

	/**
	 * chkStr 문자열이 Null이거나 빈문자열이면 rst를 반환한다.
	 * 
	 * @param chkStr
	 *            the chk str
	 * @param rst
	 *            the rst
	 * @return the string
	 */
	public static String isEmpty(String chkStr, String rst) {
		if (chkStr == null)
			return rst;
		if (isEmpty(chkStr)) {
			return rst;
		} else {
			return chkStr;
		}
	}

	/**
	 * Checks if is empty.
	 * 
	 * @param chkObj
	 *            the chk obj
	 * @param rst
	 *            the rst
	 * @return the string
	 */
	public static String isEmpty(Object chkObj, String rst) {
		if (chkObj == null)
			return rst;
		String chkStr = (String) chkObj;
		if (isEmpty(chkStr)) {
			return rst;
		} else {
			return chkStr;
		}
	}

	/**
	 * chkStr 문자열이 Null이거나 빈문자열이면 rst를 반환하고 빈문자열이 아니면 dst를 반환한다.
	 * 
	 * @param chkStr
	 *            null 값인지 체크할 문자열
	 * @param rst
	 *            null일경우 반환될 문자열
	 * @param dst
	 *            변경된 문자열
	 * @return the string
	 */
	public static String isEmpty(String chkStr, String rst, String dst) {
		if (isEmpty(chkStr)) {
			return rst;
		} else {
			return dst;
		}
	}

	/**
	 * str.lastIndexOf(div)
	 * 
	 * @param str
	 *            the str
	 * @param div
	 *            the div
	 * @return the string
	 */
	public static String lastWord(String str, String div) {
		if (isEmpty(str) || isEmpty(div))
			return "";
		int idx = str.lastIndexOf(div);
		if (idx == -1) {
			return "";
		} else {
			String s = str.substring(idx + 1);
			return s;
		}
	}

	/**
	 * Last word.
	 * 
	 * @param str
	 *            the str
	 * @return the string
	 */
	public static String lastWord(String str) {
		return lastWord(str, ".");
	}

	/**
	 * First word.
	 * 
	 * @param str
	 *            the str
	 * @param div
	 *            the div
	 * @return the string
	 */
	public static String firstWord(String str, String div) {
		if (isEmpty(str) || isEmpty(div))
			return "";
		int idx = str.indexOf(div);
		if (idx == -1) {
			return "";
		} else {
			String s = str.substring(idx + 1);
			return s;
		}
	}

	/**
	 * Last word left.
	 * 
	 * @param str
	 *            the str
	 * @param div
	 *            the div
	 * @return the string
	 */
	public static String lastWordLeft(String str, String div) {
		if (isEmpty(str) || isEmpty(div))
			return "";
		int idx = str.lastIndexOf(div);
		if (idx == -1) {
			return "";
		} else {
			String s = str.substring(0, idx);
			return s;
		}
	}

	/**
	 * First word left.
	 * 
	 * @param str
	 *            the str
	 * @param div
	 *            the div
	 * @return the string
	 */
	public static String firstWordLeft(String str, String div) {
		if (isEmpty(str) || isEmpty(div))
			return "";
		int idx = str.indexOf(div);
		if (idx == -1) {
			return "";
		} else {
			String s = str.substring(0, idx);
			return s;
		}
	}

	/**
	 * 배열에 해당 문자열이 있는지 확인 있으면 True를 반환.
	 * 
	 * @param src
	 *            the src
	 * @param dest
	 *            the dest
	 * @return true, if successful
	 */
	public static boolean matchArray(String src[], String dest) {
		for (int i = 0; i < src.length; i++) {
			if (src[i] != null) {
				if (src[i].equalsIgnoreCase(dest)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * PHP의 explode 기능의 메소드.
	 * 
	 * @param src
	 *            the src
	 * @param delim
	 *            the delim
	 * @return : 배열
	 */
	public static String[] explode(String src, String delim) {
		if (src == null || src.length() == 0)
			return new String[0];
		if (delim.length() >= 2) {
			src = change(src, delim, "\27");
			delim = "\27";
		}
		StringTokenizer stk = new StringTokenizer(src, delim);
		int size = stk.countTokens();
		String rst[] = new String[size];
		int i = 0;
		while (stk.hasMoreTokens()) {
			rst[i] = stk.nextToken();
			i++;
		}
		return rst;
	}

	/**
	 * PHP의 implode 기능의 메소드.
	 * 
	 * @param src
	 *            the src
	 * @param delim
	 *            the delim
	 * @return : 문자열
	 */
	public static String implode(String src[], String delim) {
		if (src == null || src.length == 0)
			return "";
		int size = src.length;
		String rst = "";
		for (int i = 0; i < size; i++) {
			if (i != size - 1) {
				rst += src[i] + delim;
			} else {
				rst += src[i];
			}
		}
		return rst;
	}

	/**
	 * 문자열 자르기.
	 * 
	 * @param str
	 *            the str
	 * @param st
	 *            the st
	 * @param end
	 *            the end
	 * @return the string
	 */
	public static String substring(String str, int st, int end) {
		if (str == null)
			return "";
		if (str.length() < end) {
			return StringUtils.substring(str, st);
		}
		return StringUtils.substring(str, st, end);
	}

	/**
	 * 문자열 자르기.
	 * 
	 * @param str
	 *            the str
	 * @param st
	 *            the st
	 * @return the string
	 */
	public static String substring(String str, int st) {
		if (str == null)
			return "";
		if (str.length() < st) {
			return "";
		}
		return StringUtils.substring(str, st);
	}

	/**
	 * javascript에 들어가는 문자열을 변환해준다.
	 * 
	 * @param src
	 *            the src
	 * @return the string
	 */
	public static String toJS(String src) {
		if (isEmpty(src))
			return "";
		return src.replace("\\", "\\\\").replace("\'", "\\\'").replace("\"", "").replace("\r\n", "\\n").replace("\n", "\\n");
	}

	/**
	 * source문자열에서 '\n'(개행)문자를 '<br>
	 * '로 치환 부가적으로 transSpaceNbsp, transTabNbsp 메서드 호출.
	 * 
	 * @param str
	 *            the str
	 * @return String
	 */
	public static String replaceEnterBr(String str) {
		if (str == null)
			return str;
		if (str != null && "".equals(str))
			return str;
		String returnString = str;
		String[] argsCode = { "\n" };
		String[] argsHtml = { "<br />" };
		for (int i = 0; i < argsCode.length; i++) {
			returnString = returnString.replaceAll("(?i)" + argsCode[i], argsHtml[i]);
		}
		return returnString;
	}

	/**
	 * 설정된 표출 정보를 Document 형태의 값으로 리턴한다.
	 * 
	 * @param str
	 *            the str
	 * @return the document
	 */
	public static Document toDocument(String str) {
		if (isEmpty(str))
			return null;
		Document document = null;
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			document = builder.parse(new InputSource(new StringReader(str)));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return document;
	}

	/**
	 * Convert document.
	 * 
	 * @param requestXML
	 *            the request xml
	 * @return the document
	 */
	public static Document convertDocument(String requestXML) {
		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Document document = null;

		try {
			// String을 XML 형태로 변환하자
			factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(true);

			builder = factory.newDocumentBuilder();

			document = builder.parse(new InputSource(new StringReader(requestXML)));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return document;
	}

	/**
	 * Format str.
	 * 
	 * @param date
	 *            the date
	 * @return the string
	 */
	public static String formatStr(String date) {
		if (date.length() < 8)
			return "-";

		return date.substring(0, 4) + "." + date.substring(4, 6) + "." + date.substring(6, 8);
	}

	/**
	 * FormatTime str.
	 * 
	 * @param date
	 *            the date
	 * @return the string
	 */
	public static String formatTimeStr(String date) {
		if (date.length() < 14)
			return "-";

		return date.substring(0, 4) + "." + date.substring(4, 6) + "." + date.substring(6, 8) + " " + date.substring(8, 10) + ":" + date.substring(10, 12)
						+ ":" + date.substring(12, 14);
	}

	/**
	 * Format str kor.
	 * 
	 * @param date
	 *            the date
	 * @return the string
	 * @throws UnsupportedEncodingException
	 *             the unsupported encoding exception
	 */
	public static String formatStrKor(String date) throws UnsupportedEncodingException {
		if (date.length() < 8)
			return "-";
		// return new String((date.substring(0,
		// 4)+"년 "+date.substring(4,6)+"월 "+date.substring(6,
		// 8)+"일").getBytes(), "UTF-8");
		return (date.substring(0, 4) + "년 " + date.substring(4, 6) + "월 " + date.substring(6, 8) + "일");
	}

	/**
	 * 확장자만 return.
	 * 
	 * @param realFilePath
	 *            the real file path
	 * @return the extension
	 */
	public static final String getExtension(String realFilePath) {
		if (null == realFilePath)
			return "";

		return realFilePath.substring(realFilePath.length() - 3, realFilePath.length());
	}

	/**
	 * String Null Check.
	 * 
	 * @param realFilePath
	 *            the real file path
	 * @return the extension
	 */
	public static String StrNull(Object obj, String replace) {
		if (obj == null || "".equals(obj.toString())) {
			return replace;
		}

		return obj.toString();
	}

	/**
	 * String Null Check.
	 * 
	 * @param realFilePath
	 *            the real file path
	 * @return the extension
	 */
	public static String StrNull(Object obj) {
		if (obj == null || "".equals(obj.toString())) {
			return "";
		}

		return obj.toString();
	}

	/**
	 * String Date Check.
	 * 
	 * @param realFilePath
	 *            the real file path
	 * @return the extension
	 */
	public static String getDateFormat(String str) {

		if (str.length() >= 8) {
			return str.substring(0, 4) + "." + str.substring(4, 6) + "." + str.substring(6, 8);
		}
		return str;

	}

	/**
	 * business_num체크
	 * 
	 * @param realFilePath
	 *            the real file path
	 * @return the extension
	 */
	public static String getBusinessNum(String str) {

		if (str.length() >= 8) {
			return str.substring(0, 3) + "-" + str.substring(3, 5) + "-" + str.substring(5, 10);
		}
		return str;

	}

	/**
	 * 현재날짜 가져오기
	 * 
	 * @return 현재날짜
	 */
	public static String getToDate() {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMdd");
		Date today = new Date();
		String day = formatter.format(today);
		return day;
	}

	/**
	 * 현재날짜 가져오기
	 * 
	 * @return 현재날짜
	 */
	public static String getNowDate() {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yy/MM/dd");
		Date today = new Date();
		String day = formatter.format(today);
		return day;
	}

	/**
	 * 현재날짜 가져오기
	 * 
	 * @param String
	 *            날짜형식포맷
	 * @return 현재날짜
	 */
	public static String getToDate(String format) {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format);
		Date today = new Date();
		String day = formatter.format(today);
		return day;
	}

	/* 해당날짜의 요일 가져오기 */
	public static String getDayOfWeek(String date) {
		Calendar calendar = Calendar.getInstance();
		int yyyy = 0, mm = 0, dd = 0;
		if (date != null && !"".equals(date)) {
			yyyy = Integer.parseInt(date.substring(0, 4));
			mm = Integer.parseInt(date.substring(4, 6));
			if (date.length() == 8)
				dd = Integer.parseInt(date.substring(6, 8));
		}

		calendar.set(yyyy, (mm - 1), dd);
		int day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
		String[] days = new String[] { "일", "월", "화", "수", "목", "금", "토" };
		return days[day_of_week - 1];
	}

	/* 해당날짜의 주 가져오기 */
	public static int getDayOfWeekInMonth(String date) {
		Calendar calendar = Calendar.getInstance();
		int yyyy = 0, mm = 0, dd = 0;
		if (date != null && !"".equals(date)) {
			yyyy = Integer.parseInt(date.substring(0, 4));
			mm = Integer.parseInt(date.substring(4, 6));
			if (date.length() == 8)
				dd = Integer.parseInt(date.substring(6, 8));
		}

		calendar.set(yyyy, (mm - 1), dd);
		int day_of_week_month = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
		return day_of_week_month;
	}

	public static int getDayOfWeekNo(String date) {
		Calendar calendar = Calendar.getInstance();
		int yyyy = 0, mm = 0, dd = 0;
		if (date != null && !"".equals(date)) {
			yyyy = Integer.parseInt(date.substring(0, 4));
			mm = Integer.parseInt(date.substring(4, 6));
			if (date.length() == 8)
				dd = Integer.parseInt(date.substring(6, 8));
		}

		calendar.set(yyyy, (mm - 1), dd);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	public static int getLastDayOfMonth() {
		Calendar now = Calendar.getInstance();
		int yyyy = 0, mm = 0, dd = 0;
		return now.getActualMaximum(Calendar.DATE);
	}

	public static int getLastDayOfMonth(String date) {
		Calendar now = Calendar.getInstance();
		int yyyy = 0, mm = 0, dd = 0;

		if (date != null && !"".equals(date)) {
			yyyy = Integer.parseInt(date.substring(0, 4));
			mm = Integer.parseInt(date.substring(4, 6));
			if (date.length() == 8)
				dd = Integer.parseInt(date.substring(6, 8));
		}

		now.set(yyyy, (mm - 1), dd);
		return now.getActualMaximum(Calendar.DATE);
	}

	/**
	 * 날짜문자열(yyyymmdd)에 날짜를 더한(혹은 뺀) 일자를 구함 <br>
	 * 
	 * @param str
	 *            yyyyMMdd 형식의 날짜문자열
	 * @param days
	 *            더할, 혹은 뺄 날수
	 * @return yyyyyMMdd 형식의 8자리 날짜
	 * @exception 날짜문자열
	 *                형식이 잘못되었을 경우 ParseException return
	 */

	public static String addDays(String str, int days, String formatString) throws ParseException {
		SimpleDateFormat fmt = new SimpleDateFormat(formatString);
		Date date = fmt.parse(str);
		date.setTime(date.getTime() + (long) days * 1000L * 60L * 60L * 24L);
		return fmt.format(date);
	}

	public static String getAddMonth(String date, int month, String format) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Calendar calendar = Calendar.getInstance();
		int yyyy = 0, mm = 0, dd = 0;
		if (date != null && !"".equals(date)) {
			yyyy = Integer.parseInt(date.substring(0, 4));
			mm = Integer.parseInt(date.substring(4, 6));
			if (date.length() == 8)
				dd = Integer.parseInt(date.substring(6, 8));
		}
		calendar.set(yyyy, (mm - 1) + month, dd);

		return formatter.format(calendar.getTime());
	}

	public static String getAddYear(String date, int year, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Calendar calendar = Calendar.getInstance();
		int yyyy = 0, mm = 0, dd = 1;
		if (date != null && !"".equals(date)) {
			yyyy = Integer.parseInt(date.substring(0, 4));
			if (date.length() >= 6)
				mm = Integer.parseInt(date.substring(4, 6)) - 1;
			if (date.length() == 8)
				dd = Integer.parseInt(date.substring(6, 8));
		}

		calendar.set(yyyy + year, mm, dd);

		return formatter.format(calendar.getTime());
	}

	public static String getComma(int number) {
		NumberFormat nf = NumberFormat.getInstance();

		return nf.format(number);
	}

	public static int getNumber(String number) {
		number = StrNull(number, "0");
		number = number.replaceAll(",", "");
		int num = 0;
		try {
			num = Integer.parseInt(number);
		} catch (Exception e) {
		}
		return num;
	}

	public static String getEncodeSHA256(String val) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(val.getBytes());

		byte byteData[] = md.digest();

		// convert the byte to hex format method 1
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}

		return sb.toString();
	}
}
