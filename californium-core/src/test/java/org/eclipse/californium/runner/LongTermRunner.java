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
 *    Bosch Software Innovations - initial implementation
 ******************************************************************************/
package org.eclipse.californium.runner;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.californium.category.LongTerm;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * Runner for tests under debugging.
 * 
 * @see LongTerm
 */
public class LongTermRunner extends BlockJUnit4ClassRunner {

	public static final Logger LOGGER = Logger.getLogger(LongTermRunner.class.getName());

	private static final int MEGA_BYTE = 1024 * 1024;

	/**
	 * Default maximum number of repeats.
	 * 
	 * @see #maxiumRepeates
	 */
	private final int DEFAULT_MAXIUM_REPEATS = 100;
	/**
	 * Maximum number of repeats.
	 * 
	 * May be set via property
	 * "org.eclipse.californium.runner.LongTermRunner.repeats". Default
	 * {@link #DEFAULT_MAXIUM_REPEATS}.
	 */
	private final long maxiumRepeates;

	public LongTermRunner(Class<?> klass) throws InitializationError {
		super(klass);
		int repeats = DEFAULT_MAXIUM_REPEATS;
		String name = LongTermRunner.class.getName() + ".repeats";
		String value = System.getProperty(name);
		if (null != value) {
			try {
				repeats = Integer.parseInt(value);
			} catch (NumberFormatException ex) {
				LOGGER.log(Level.SEVERE, "value for ''{0}'' := ''{1}''", new Object[] { name, value });
			}
		}
		this.maxiumRepeates = repeats;
	}

	@Override
	public void run(final RunNotifier notifier) {
		if (0 == maxiumRepeates) {
			LOGGER.log(Level.CONFIG, "repeat until error!");
		} else {
			LOGGER.log(Level.CONFIG, "maximum repeats: {0}", maxiumRepeates);
		}
		final AtomicInteger failed = new AtomicInteger();
		notifier.addListener(new RunListener() {

			public void testFailure(Failure failure) throws Exception {
				failed.incrementAndGet();
			}

			public void testAssumptionFailure(Failure failure) {
				failed.incrementAndGet();
			}

		});
		for (int loop = 1; 0 == maxiumRepeates || loop <= maxiumRepeates; ++loop) {
			Runtime runtime = Runtime.getRuntime();
			LOGGER.log(Level.INFO, "loop: {0}", loop);
			LOGGER.log(Level.INFO, "mem: free {0} MByte, total {1} MByte, max {2} MByte",
					new Object[] { runtime.freeMemory() / MEGA_BYTE, runtime.totalMemory() / MEGA_BYTE,
							runtime.maxMemory() / MEGA_BYTE });
			super.run(notifier);
			if (0 < failed.get()) {
				break;
			}
		}
	}
}
