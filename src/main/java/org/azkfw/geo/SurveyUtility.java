package org.azkfw.geo;

/**
 * このクラスは、測量のユーティリティクラスです。
 * 
 * @author N.Kawakita
 */
public class SurveyUtility {

	/** 測地系 */
	private SokuchiKei sokuchiKei;

	/** 縮尺係数 */
	private double sbyS = 0.9999;

	public SurveyUtility() {
		this(SokuchiKei.GRS80);
	}

	public SurveyUtility(final SokuchiKei sokuchiKei) {
		this.sokuchiKei = sokuchiKei;
	}

	public void setSokuchiKei(final SokuchiKei sokuchiKei) {
		this.sokuchiKei = sokuchiKei;
	}

	/**
	 * 緯度、経度から平面直角座標を求める
	 * 
	 * @param latitude 緯度[degree]
	 * @param longitude 経度[degree]
	 * @param zone 系番号
	 * @return x, y
	 */
	public double[] bl2xy(final double latitude, final double longitude, final Zone zone) {
		DMS lat = DMS.parseDegree(latitude);
		DMS lon = DMS.parseDegree(longitude);
		return bl2xy(new Coordinate(lat, lon), zone);
	}

	private double[] bl2xy(final Coordinate coordinate, final Zone zone) {
		double phi0 = zone.getLatitude().toRadian();
		double lamda0 = zone.getLongitude().toRadian();
		double phi1 = coordinate.getLatitude().toRadian();
		double lamda1 = coordinate.getLongitude().toRadian();

		double e = Math.sqrt(2.0 * sokuchiKei.F - 1.0) / sokuchiKei.F;
		double s0 = kocyou(phi0, e);
		double s1 = kocyou(phi1, e);

		double ut = sokuchiKei.a / Math.sqrt(1.0 - Math.pow(e, 2.0) * Math.pow(Math.sin(phi1), 2.0));
		double conp = Math.cos(phi1);
		double t1 = Math.tan(phi1);

		double dlamda = lamda1 - lamda0;
		double eta2 = (Math.pow(e, 2.0) / (1.0 - Math.pow(e, 2.0))) * Math.pow(conp, 2.0);

		double x1 = 5.0 - Math.pow(t1, 2.0) + 9.0 * eta2 + 4.0 * Math.pow(eta2, 2.0);
		double x2 = -61.0 + 58.0 * Math.pow(t1, 2.0) - Math.pow(t1, 4.0) - 270 * eta2 + 330.0 * Math.pow(t1, 2.0) * eta2;
		double x3 = -1385.0 + 3111.0 * Math.pow(t1, 2.0) - 543.0 * Math.pow(t1, 4.0) + Math.pow(t1, 6.0);

		double x = ((s1 - s0) + ut * Math.pow(conp, 2.0) * t1 * Math.pow(dlamda, 2.0) / 2.0 + ut * Math.pow(conp, 4.0) * t1 * x1
				* Math.pow(dlamda, 4.0) / 24.0 - ut * Math.pow(conp, 6.0) * t1 * x2 * Math.pow(dlamda, 6.0) / 720.0 - ut * Math.pow(conp, 8.0) * t1
				* x3 * Math.pow(dlamda, 8.0) / 40320.0)
				* sbyS;

		double y1 = -1.0 + Math.pow(t1, 2.0) - eta2;
		double y2 = -5.0 + 18.0 * Math.pow(t1, 2.0) - Math.pow(t1, 4.0) - 14.0 * eta2 + 58.0 * Math.pow(t1, 2.0) * eta2;
		double y3 = -61.0 + 479.0 * Math.pow(t1, 2.0) - 179.0 * Math.pow(t1, 4.0) + Math.pow(t1, 6.0);

		double y = (ut * conp * dlamda - ut * Math.pow(conp, 3.0) * y1 * Math.pow(dlamda, 3.0) / 6.0 - ut * Math.pow(conp, 5.0) * y2
				* Math.pow(dlamda, 5.0) / 120.0 - ut * Math.pow(conp, 7.0) * y3 * Math.pow(dlamda, 7.0) / 5040.0)
				* sbyS;

		return new double[] { x, y };
	}

	public Coordinate xy2bl(final double x, final double y, final Zone zone) {
		double phi0 = zone.getLatitude().toRadian();
		double lamda0 = zone.getLongitude().toRadian();

		double e = Math.sqrt(2.0 * sokuchiKei.F - 1.0) / sokuchiKei.F;
		double e2j = Math.pow(e, 2.0);

		double phi1 = suisen(x, phi0, e);

		double ut = sokuchiKei.a / Math.sqrt(1.0 - e2j * Math.pow(Math.sin(phi1), 2.0));
		double ut2j = Math.pow(ut, 2.0);
		double ut4j = Math.pow(ut, 4.0);
		double ut6j = Math.pow(ut, 6.0);
		double ut8j = Math.pow(ut, 8.0);

		double t = Math.tan(phi1);
		double t2j = Math.pow(t, 2.0);
		double t4j = Math.pow(t, 4.0);
		double t6j = Math.pow(t, 6.0);

		double conp = Math.cos(phi1);
		double eta2 = (e2j / (1.0 - e2j)) * Math.pow(conp, 2.0);

		double yy = y / sbyS;

		double phir1 = 1.0 + eta2;
		double phir2 = (5.0) + (3.0 * t2j) + (6.0 * eta2) - (6.0 * t2j * eta2) - (3.0 * Math.pow(eta2, 2.0)) - (9.0 * t2j * Math.pow(eta2, 2.0));
		double phir3 = (61.0) + (90.0 * t2j) + (45.0 * t4j) + (107.0 * eta2) - (162.0 * t2j * eta2) - (45.0 * t4j * eta2);
		double phir4 = (1385.0) + (3633.0 * t2j) + (4095.0 * t4j) + (1575.0 * t6j);

		double phir = -(phir1 / (2.0 * ut2j)) * Math.pow(yy, 2.0);
		phir += (phir2 / (24.0 * ut4j)) * Math.pow(yy, 4.0);
		phir -= (phir3 / (720.0 * ut6j)) * Math.pow(yy, 6.0);
		phir += (phir4 / (40320.0 * ut8j)) * Math.pow(yy, 8.0);
		phir *= t;
		phir += phi1;

		double lamdar1 = ut * conp;
		double lamdar2 = 1.0 + 2.0 * t2j + eta2;
		double lamdar3 = 5.0 + 28.0 * t2j + 24.0 * t4j + 6.0 * eta2 + 8.0 * t2j * eta2;
		double lamdar4 = 61.0 + 662.0 * t2j + 1320.0 * t4j + 720.0 * t6j;

		double lamdar = (1.0 / lamdar1) * yy;
		lamdar -= (lamdar2 / (6.0 * ut2j * lamdar1)) * Math.pow(yy, 3.0);
		lamdar += (lamdar3 / (120.0 * ut4j * lamdar1)) * Math.pow(yy, 5.0);
		lamdar -= (lamdar4 / (5040.0 * ut6j * lamdar1)) * Math.pow(yy, 7.0);
		lamdar += lamda0;

		Coordinate coordinate = new Coordinate(DMS.parseRadian(phir), DMS.parseRadian(lamdar));
		return coordinate;
	}

	private double kocyou(final double ido, final double aE) {
		double e2j = Math.pow(aE, 2.0);
		double e4j = Math.pow(aE, 4.0);
		double e6j = Math.pow(aE, 6.0);
		double e8j = Math.pow(aE, 8.0);
		double e10j = Math.pow(aE, 10.0);
		double e12j = Math.pow(aE, 12.0);
		double e14j = Math.pow(aE, 14.0);
		double e16j = Math.pow(aE, 16.0);

		double a1 = 1.0;
		double a2 = 3.0 / 4.0 * e2j;
		double a3 = 45.0 / 64.0 * e4j;
		double a4 = 175.0 / 256.0 * e6j;
		double a5 = 11025.0 / 16384.0 * e8j;
		double a6 = 43659.0 / 65536.0 * e10j;
		double a7 = 693693.0 / 1048576.0 * e12j;
		double a8 = 19324305.0 / 29360128.0 * e14j;
		double a9 = 4927697775.0 / 7516192768.0 * e16j;
		double a = a1 + a2 + a3 + a4 + a5 + a6 + a7 + a8 + a9;

		double b1 = 3.0 / 4.0 * e2j;
		double b2 = 15.0 / 16.0 * e4j;
		double b3 = 525.0 / 512.0 * e6j;
		double b4 = 2205.0 / 2048.0 * e8j;
		double b5 = 72765.0 / 65536.0 * e10j;
		double b6 = 297297.0 / 262144.0 * e12j;
		double b7 = 135270135.0 / 117440512.0 * e14j;
		double b8 = 547521975.0 / 469762048.0 * e16j;
		double b = b1 + b2 + b3 + b4 + b5 + b6 + b7 + b8;

		double c1 = 15.0 / 64.0 * e4j;
		double c2 = 105.0 / 256.0 * e6j;
		double c3 = 2205.0 / 4096.0 * e8j;
		double c4 = 10395.0 / 16384.0 * e10j;
		double c5 = 1486485.0 / 2097152.0 * e12j;
		double c6 = 45090045.0 / 58720256.0 * e14j;
		double c7 = 766530765.0 / 939524096.0 * e16j;
		double c = c1 + c2 + c3 + c4 + c5 + c6 + c7;

		double d1 = 35.0 / 512.0 * e6j;
		double d2 = 315.0 / 2048.0 * e8j;
		double d3 = 31185.0 / 131072.0 * e10j;
		double d4 = 165165.0 / 524288.0 * e12j;
		double d5 = 45090045.0 / 117440512.0 * e14j;
		double d6 = 209053845.0 / 469762048.0 * e16j;
		double d = d1 + d2 + d3 + d4 + d5 + d6;

		double e1 = 315.0 / 16384.0 * e8j;
		double e2 = 3465.0 / 65536.0 * e10j;
		double e3 = 99099.0 / 1048576.0 * e12j;
		double e4 = 4099095.0 / 29360128.0 * e14j;
		double e5 = 348423075.0 / 1879048192.0 * e16j;
		double e = e1 + e2 + e3 + e4 + e5;

		double f1 = 693.0 / 131072 * e10j;
		double f2 = 9009.0 / 524288.0 * e12j;
		double f3 = 4099095.0 / 117440512.0 * e14j;
		double f4 = 26801775.0 / 469762048.0 * e16j;
		double f = f1 + f2 + f3 + f4;

		double g1 = 3003 / 2097152.0 * e12j;
		double g2 = 315315.0 / 58720256.0 * e14j;
		double g3 = 11486475.0 / 939524096.0 * e16j;
		double g = g1 + g2 + g3;

		double h1 = 45045.0 / 117440512.0 * e14j;
		double h2 = 765765.0 / 469762048.0 * e16j;
		double h = h1 + h2;

		double i1 = 765765.0 / 7516192768.0 * e16j;
		double i = i1;

		double sigosen = sokuchiKei.a
				* (1.0 - e2j)
				* (a * ido - b * Math.sin(ido * 2.0) / 2.0 + c * Math.sin(ido * 4.0) / 4.0 - d * Math.sin(ido * 6.0) / 6.0 + e * Math.sin(ido * 8.0)
						/ 8.0 - f * Math.sin(ido * 10.0) / 10.0 + g * Math.sin(ido * 12.0) / 12.0 - h * Math.sin(ido * 14.0) / 14.0 + i
						* Math.sin(ido * 16.0) / 16.0);

		return sigosen;
	}

	private double suisen(final double x, final double ido, final double aE) {
		double s0 = kocyou(ido, aE);
		double m = s0 + (x / sbyS);

		double phin = ido;
		double phi0 = phin;
		double e2j = Math.pow(aE, 2.0);

		int cnt = 0;
		while (true) {
			cnt++;
			phi0 = phin;
			double sn = kocyou(phin, aE);
			double v1 = 2.0 * (sn - m) * Math.pow(1.0 - e2j * Math.pow(Math.sin(phin), 2.0), 1.5);
			double v2 = 3.0 * e2j * (sn - m) * Math.sin(phin) * Math.cos(phin) * Math.sqrt(1.0 - e2j * Math.pow(Math.sin(phin), 2.0)) - 2.0
					* sokuchiKei.a * (1.0 - e2j);
			phin = phin + (v1 / v2);
			if (Math.abs(phin - phi0) < 0.00000000000001 || 100 < cnt) {
				break;
			}
		}
		return phin;
	}
}
