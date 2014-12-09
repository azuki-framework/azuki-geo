package org.azkfw.geo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

public class SokuchiAPI {

	/** 文字コード取得用パターン */
	private static final Pattern PTN_CHARSET = Pattern.compile("charset\\s*=\\s*([^\\s]+)\\s*;");

	/**
	 * このEnumは、測地系列挙したEnumです。
	 * 
	 * @author kawakicchi
	 */
	public static enum SokuchiKei {

		/** 日本測地系 - 1 */
		Japan("日本測地系", 1),
		/** 世界測地系 - 2 */
		World("世界測地系", 2);

		private String name;
		private int value;

		private SokuchiKei(final String aName, final int aValue) {
			name = aName;
			value = aValue;
		}

		public String getName() {
			return name;
		}

		public int getValue() {
			return value;
		}
	}

	/**
	 * このクラスは、緯度・経度情報を保持するクラスです。
	 * 
	 * @author kawakicchi
	 */
	public static class Xy2blEntity {
		/** 緯度[degree] */
		public double latitude;
		/** 経度[degree] */
		public double longitude;
		/** 真北方向角[degree] */
		public double gridConv;
		/** 縮尺係数 */
		public double scaleFactor;
	}

	/**
	 * このクラスは、平面直角座標情報を保持するクラスです。
	 * 
	 * @author kawakicchi
	 */
	public static class Bl2xyEntity {
		/** 平面直角座標系のX座標[m] */
		public double publicX;
		/** 平面直角座標系のY座標[m] */
		public double publicY;
		/** 真北方向角[degree] */
		public double gridConv;
		/** 縮尺係数 */
		public double scaleFactor;
	}

	private String url = "http://vldb.gsi.go.jp/sokuchi/surveycalc/surveycalc/";

	/** 測地系 */
	private SokuchiKei sokuchiKei;

	private HttpClient httpClient;

	/**
	 * コンストラクタ
	 * <p>
	 * 測地系は世界測地系となる。
	 * </p>
	 */
	public SokuchiAPI() {
		this(SokuchiKei.World);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param aSokuchiKei 測地系
	 */
	public SokuchiAPI(final SokuchiKei aSokuchiKei) {
		sokuchiKei = aSokuchiKei;

		httpClient = new DefaultHttpClient();
	}

	/**
	 * 緯度経度を10進数から度分秒形式へ変換する。
	 * <p>
	 * DEG(Degree)からDMS(Degree Minute Second)へ変換する。
	 * </p>
	 * 
	 * @param deg 10進数
	 * @return 度分秒(60進数)
	 */
	public static double degree2dms(final double deg) {
		String txLatDeg = Integer.toString((int) deg);

		int minTemp = (int) ((deg - (int) deg) * 60);
		String txLatMin = String.format("%02d", minTemp);

		double a = BigDecimal.valueOf(deg).subtract(BigDecimal.valueOf((int) deg)).multiply(BigDecimal.valueOf(60)).doubleValue();
		double b = BigDecimal.valueOf(deg).subtract(BigDecimal.valueOf((int) deg)).multiply(BigDecimal.valueOf(60)).intValue();

		double secTemp = (a - b) * 60;
		String txLatSec = (10 > secTemp) ? "0" + Double.toString(secTemp) : Double.toString(secTemp);

		String txLat2 = txLatDeg + txLatMin + txLatSec;
		double dms = Double.parseDouble(txLat2);

		return dms;
	}

	/**
	 * 緯度経度を度分秒形式から10進数へ変換する。
	 * <p>
	 * DMS(Degree Minute Second)からDEG(Degree)へ変換する。
	 * </p>
	 * 
	 * @param dms
	 * @return
	 */
	public static double dms2degree(final double dms) {
		int deg = (int) (dms / 10000);
		int min = (int) ((dms - deg * 10000) / 100);
		BigDecimal sec = BigDecimal.valueOf(dms).subtract(BigDecimal.valueOf((int) (deg * 10000 + min * 100)));

		BigDecimal a = BigDecimal.valueOf(deg);
		BigDecimal b = BigDecimal.valueOf(min).divide(BigDecimal.valueOf(60), 12, BigDecimal.ROUND_HALF_UP);
		BigDecimal c = sec.divide(BigDecimal.valueOf(60), 12, BigDecimal.ROUND_HALF_UP).divide(BigDecimal.valueOf(60), 12, BigDecimal.ROUND_HALF_UP);

		double degree = a.add(b).add(c).doubleValue();
		return degree;
	}

	/**
	 * 測地系を設定する。
	 * 
	 * @param aSokuchiKei 測地系
	 */
	public void setSokuchiKei(final SokuchiKei aSokuchiKei) {
		sokuchiKei = aSokuchiKei;
	}

	/**
	 * 測地系を取得する。
	 * 
	 * @return 測地系
	 */
	public SokuchiKei getSokuchiKei() {
		return sokuchiKei;
	}

	/**
	 * 平面直角座標への換算
	 * 
	 * @param latitude 緯度[degree]
	 * @param longitude 経度[degree]
	 * @param zone 系番号(平面直角座標系)[1～19]
	 * @return 平面直角座標情報
	 */
	@SuppressWarnings({ "unchecked" })
	public Bl2xyEntity bl2xy(final double latitude, final double longitude, final Zone zone) {
		Bl2xyEntity result = null;

		Map<String, String> params = new HashMap<String, String>();
		params.put("outputType", "json");
		params.put("refFrame", Integer.toString(sokuchiKei.getValue()));
		params.put("zone", Integer.toString(zone.getNo()));
		params.put("latitude", Double.toString(latitude));
		params.put("longitude", Double.toString(longitude));

		Map<String, Object> map = get("bl2xy.pl", params);

		Map<String, Object> data = (Map<String, Object>) map.get("OutputData");
		if (null != data) {
			result = new Bl2xyEntity();
			result.publicX = Double.parseDouble(s(data.get("publicX")));
			result.publicY = Double.parseDouble(s(data.get("publicY")));
			result.gridConv = Double.parseDouble(s(data.get("gridConv")));
			result.scaleFactor = Double.parseDouble(s(data.get("scaleFactor")));
		}

		return result;
	}

	/**
	 * 緯度・経度への換算
	 * 
	 * @param publicX Ｘ座標[m](小数点以下３桁まで指定可)
	 * @param publicY Ｙ座標[m](小数点以下３桁まで指定可)
	 * @param zone 系番号(平面直角座標系)[1～19]
	 * @return 緯度・経度情報
	 */
	@SuppressWarnings({ "unchecked" })
	public Xy2blEntity xy2bl(final double publicX, final double publicY, final Zone zone) {
		Xy2blEntity result = null;

		Map<String, String> params = new HashMap<String, String>();
		params.put("outputType", "json");
		params.put("refFrame", Integer.toString(sokuchiKei.getValue()));
		params.put("zone", Integer.toString(zone.getNo()));
		params.put("publicX", Double.toString(publicX));
		params.put("publicY", Double.toString(publicY));

		Map<String, Object> map = get("xy2bl.pl", params);

		Map<String, Object> data = (Map<String, Object>) map.get("OutputData");
		if (null != data) {
			result = new Xy2blEntity();
			result.latitude = Double.parseDouble(s(data.get("latitude")));
			result.longitude = Double.parseDouble(s(data.get("longitude")));
			result.gridConv = Double.parseDouble(s(data.get("gridConv")));
			result.scaleFactor = Double.parseDouble(s(data.get("scaleFactor")));
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> get(final String aAreas, final Map<String, String> aParams) {
		Map<String, Object> result = null;

		try {
			HttpGet httpGet = new HttpGet(createUrl(aAreas, aParams));
			HttpResponse response = httpClient.execute(httpGet);

			String charset = "UTF-8";
			for (Header header : response.getAllHeaders()) {
				if ("Content-Type".equals(header.getName())) {
					Matcher m = PTN_CHARSET.matcher(header.getValue());
					if (m.find()) {
						charset = m.group(1);
					}
				}
			}

			Gson gson = new Gson();
			result = gson.fromJson(new InputStreamReader(response.getEntity().getContent(), charset), Map.class);

		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		} catch (ClientProtocolException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return result;
	}

	private String createUrl(final String aAlias, final Map<String, String> aParams) throws UnsupportedEncodingException {

		StringBuilder sb = new StringBuilder();
		if (null != aParams) {
			for (String key : aParams.keySet()) {
				if (0 < sb.length()) {
					sb.append("&");
				}
				sb.append(String.format("%s=%s", key, URLEncoder.encode(aParams.get(key), "UTF-8")));
			}
		}

		if (0 == sb.length()) {
			return url + aAlias;
		} else {
			return url + aAlias + "?" + sb.toString();
		}
	}

	private String s(final Object object) {
		String string = null;
		if (null != object) {
			string = object.toString();
		}
		return string;
	}

}
