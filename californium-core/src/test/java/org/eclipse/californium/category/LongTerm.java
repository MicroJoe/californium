/*******************************************************************************
 * Copyright (c) 2017 Bosch Software Innovations GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 * 
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 *    http://www.eclipse.org/org/documents/edl-v10.html.
 * 
 * Contributors:
 *    Bosch Software Innovations GmbH - Initial creation
 ******************************************************************************/
package org.eclipse.californium.category;

import org.eclipse.californium.runner.LongTermRunner;

/**
 * A marker interface for test cases that are under debugging.
 * 
 * Typical tests using this category are used with {@link LongTermRunner}.
 * 
 * <code>
 * &#64;RunWith(LongTermRunner.class)
 * &#64;Category(LongTerm.class)
 * public class AbCdTest {
 * </code>
 */
public interface LongTerm {
}
