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
 * この列挙型は、測地系を定義した列挙型です。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/12/09
 * @author kawakicchi
 */
public enum SokuchiKei {

	/** GRS80 - GRS80楕円体 */
	GRS80(6378137.0, 298.257222101),
	/** WGS84 */
	WGS84(6378137.0, 298.257223563),
	/** Bessel 1841 - ベッセル楕円体 */
	TokyoJapan(6377397.155, 299.152813);

	/** 楕円体の長半径[m] */
	public final double a;
	/** 逆扁平率 */
	public final double F;

	/**
	 * コンストラクタ
	 * 
	 * @param a 楕円体の長半径[m]
	 * @param F 逆扁平率
	 */
	private SokuchiKei(final double a, final double F) {
		this.a = a;
		this.F = F;
	}
}
