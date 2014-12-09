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
 * この列挙型は、平面直角座標系の系情報を定義した列挙型です。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/12/09
 * @author kawakicchi
 */
public enum Zone {

	/** 1系 - 360000. 1360000. */
	System01(1, DMS.parseDMS(330000.0), DMS.parseDMS(1293000.0)),
	/** 2系 - 330000. 1310000. */
	System02(2, DMS.parseDMS(330000.0), DMS.parseDMS(1310000.0)),
	/** 3系 - 360000. 1321000. */
	System03(3, DMS.parseDMS(360000.0), DMS.parseDMS(1321000.0)),
	/** 4系 - 330000. 1333000. */
	System04(4, DMS.parseDMS(330000.0), DMS.parseDMS(1333000.0)),
	/** 5系 - 360000. 1342000. */
	System05(5, DMS.parseDMS(360000.0), DMS.parseDMS(1342000.0)),
	/** 6系 - 360000. 1360000. */
	System06(6, DMS.parseDMS(360000.0), DMS.parseDMS(1360000.0)),
	/** 7系 - 360000. 1371000. */
	System07(7, DMS.parseDMS(360000.0), DMS.parseDMS(1371000.0)),
	/** 8系 - 360000. 1383000. */
	System08(8, DMS.parseDMS(360000.0), DMS.parseDMS(1383000.0)),
	/** 9系 - 360000. 1395000. */
	System09(9, DMS.parseDMS(360000.0), DMS.parseDMS(1395000.0)),
	/** 10系 - 400000. 1405000. */
	System10(10, DMS.parseDMS(400000.0), DMS.parseDMS(1405000.0)),
	/** 11系 - 440000. 1401500. */
	System11(11, DMS.parseDMS(440000.0), DMS.parseDMS(1401500.0)),
	/** 12系 - 440000. 1421500. */
	System12(12, DMS.parseDMS(440000.0), DMS.parseDMS(1421500.0)),
	/** 13系 - 440000. 1441500. */
	System13(13, DMS.parseDMS(440000.0), DMS.parseDMS(1441500.0)),
	/** 14系 - 260000. 1420000. */
	System14(14, DMS.parseDMS(260000.0), DMS.parseDMS(1420000.0)),
	/** 15系 - 260000. 1273000. */
	System15(15, DMS.parseDMS(260000.0), DMS.parseDMS(1273000.0)),
	/** 16系 - 260000. 1240000. */
	System16(16, DMS.parseDMS(260000.0), DMS.parseDMS(1240000.0)),
	/** 17系 - 260000. 1310000. */
	System17(17, DMS.parseDMS(260000.0), DMS.parseDMS(1310000.0)),
	/** 18系 - 200000. 1360000. */
	System18(18, DMS.parseDMS(200000.0), DMS.parseDMS(1360000.0)),
	/** 19系 - 260000. 1540000. */
	System19(19, DMS.parseDMS(260000.0), DMS.parseDMS(1540000.0));

	/** 系番号 */
	public final int no;
	/** 緯度 */
	public final DMS latitude;
	/** 経度 */
	public final DMS longitude;

	/**
	 * コンストラクタ
	 * 
	 * @param aNo 系番号
	 * @param aLatitude 緯度番号
	 * @param aLongitude 経度番号
	 */
	private Zone(final int aNo, final DMS aLatitude, final DMS aLongitude) {
		no = aNo;
		latitude = aLatitude;
		longitude = aLongitude;
	}

	/**
	 * 系番号を取得する。
	 * 
	 * @return 系番号
	 */
	public int getNo() {
		return no;
	}

	/**
	 * 緯度情報を取得する。
	 * 
	 * @return 緯度
	 */
	public DMS getLatitude() {
		return latitude;
	}

	/**
	 * 経度情報を取得する。
	 * 
	 * @return 経度
	 */
	public DMS getLongitude() {
		return longitude;
	}
}
