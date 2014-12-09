/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.azkfw.geo;

/**
 * このクラスは、度情報を保持するクラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/12/09
 * @author kawakicchi
 */
public class DMS {

	/** 度 */
	private int degree;
	/** 分 */
	private int minute;
	/** 秒 */
	private double second;

	/**
	 * コンストラクタ
	 */
	private DMS() {

	}

	public static DMS parseDegree(final double aDegree) {
		// degree
		int degree = (int) aDegree;
		// minute
		int minute = (int) ((aDegree - degree) * 60.0);
		if (0 > minute) {
			minute = 0;
		}
		// second
		double second = (aDegree - degree - minute / 60.0) * 3600.0;
		if (0 > second) {
			second = 0;
		}

		DMS dms = new DMS();
		dms.degree = degree;
		dms.minute = minute;
		dms.second = second;
		return dms;
	}

	public static DMS parseDMS(final double aDMS) {
		// degree
		int degree = (int) (aDMS / 10000.0);
		// minute
		int minute = (int) ((aDMS - ((double) degree * 10000.0)) / 100.0);
		// second
		double second = (aDMS - ((degree * 10000.0) + (minute * 100.0)));

		DMS dms = new DMS();
		dms.degree = degree;
		dms.minute = minute;
		dms.second = second;
		return dms;
	}

	public static DMS parseRadian(final double aRadian) {
		double rad = aRadian;

		double degbase = 180.0 / Math.PI;
		double fugou;
		if (rad < 0) {
			fugou = -1.0;
		} else {
			fugou = 1.0;
		}

		rad = Math.abs(rad);

		rad *= degbase;
		int angle = (int) rad;
		rad = (float) (rad - angle) * 60.0;
		int minute = (int) rad;
		double second = (float) (rad - minute) * 60.0;

		DMS dms = new DMS();
		dms.degree = angle * (int) (fugou);
		dms.minute = minute;
		dms.second = second;
		return dms;
	}

	/**
	 * 緯度経度を度分秒形式DMS(Degree Minute Second)。
	 * 
	 * @return DMS形式[dddmmss.s]
	 */
	public double toDMS() {
		return (degree * 10000) + (minute * 100) + (second);
	}

	/**
	 * 緯度経度を10進数(Degree)。
	 * 
	 * @return 10進数[ddd.ddddd]
	 */
	public double toDegree() {
		return ((double) degree) + ((double) minute / 60.0) + (second / 3600.0);
	}

	public double toRadian() {
		double deg = toDMS();

		double radbase = Math.PI / 180.f;
		double fugou;
		if (deg < 0) {
			fugou = -1.0;
		} else {
			fugou = 1.0;
		}

		deg = Math.abs(deg);

		int angle = (int) (deg / 10000.0f);
		float minute = (float) ((int) (deg / 100.0f) - (angle * 100.0f));
		double second = deg - (angle * 10000.0f) - (minute * 100.0);

		double rad = fugou * (angle + (minute + (second / 60.f)) / 60.f);
		rad = rad * radbase;
		return rad;
	}

	@Override
	public String toString() {
		return String.format("%d°%2d′%2f", degree, minute, second);
	}
}
