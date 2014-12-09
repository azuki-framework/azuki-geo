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
 * このクラスは、座標情報を保持するクラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/12/09
 * @author kawakicchi
 */
public class Coordinate {

	/** 緯度情報 */
	private DMS latitude;
	/** 経度情報 */
	private DMS longitude;

	/**
	 * コンストラクタ
	 * 
	 * @param aLatitude 緯度[degree]
	 * @param aLongitude 経度[degree]
	 */
	public Coordinate(final double aLatitude, final double aLongitude) {
		latitude = DMS.parseDegree(aLatitude);
		longitude = DMS.parseDegree(aLongitude);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param aLatitude 緯度情報
	 * @param aLongitude 経度情報
	 */
	public Coordinate(final DMS aLatitude, final DMS aLongitude) {
		latitude = aLatitude;
		longitude = aLongitude;
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
