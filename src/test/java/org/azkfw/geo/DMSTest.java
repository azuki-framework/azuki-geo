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

import junit.framework.TestCase;

import org.junit.Test;

/**
 * @since 1.0.0
 * @version 1.0.0 2014/12/09
 * @author kawakicchi
 */
public class DMSTest extends TestCase {

	@Test
	public void test() {

		DMS dms = null;
		double degree = 0;
		
		dms = DMS.parseDegree(degree);
		assertEquals("", degree, dms.toDegree());
		assertEquals("", 0.0, dms.toDMS());
		assertEquals("", "0° 0′0.000000", dms.toString());

		degree = 36.0;
		dms = DMS.parseDegree(degree);
		assertEquals("", degree, dms.toDegree());
		assertEquals("", 360000.0, dms.toDMS());
		assertEquals("", "36° 0′0.000000", dms.toString());

		degree = 136.0;
		dms = DMS.parseDegree(degree);
		assertEquals("", degree, dms.toDegree());
		assertEquals("", 1360000.0, dms.toDMS());
		assertEquals("", "136° 0′0.000000", dms.toString());

		degree = 36.103774792;
		dms = DMS.parseDegree(degree);
		assertEquals("", degree, dms.toDegree());
		assertEquals("", 360613.5892512, dms.toDMS());
		assertEquals("", "36° 6′13.589251", dms.toString());

		degree = 140.087855042;
		dms = DMS.parseDegree(degree);
		assertEquals("", degree, dms.toDegree());
		assertEquals("", 1400516.2781512, dms.toDMS());
		assertEquals("", "140° 5′16.278151", dms.toString());
	}
}
