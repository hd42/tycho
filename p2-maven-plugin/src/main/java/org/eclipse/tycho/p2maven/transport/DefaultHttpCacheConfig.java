/*******************************************************************************
 * Copyright (c) 2022 Christoph Läubrich and others.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Christoph Läubrich - initial API and implementation
 *******************************************************************************/
package org.eclipse.tycho.p2maven.transport;

import java.io.File;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.LegacySupport;
import org.apache.maven.repository.RepositorySystem;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

@Component(role = HttpCacheConfig.class)
public class DefaultHttpCacheConfig implements HttpCacheConfig, Initializable {

	private boolean offline;
	private boolean update;
	private boolean interactive;

	@Requirement
	private LegacySupport legacySupport;
	private File cacheLocation;

	@Override
	public void initialize() throws InitializationException {
		File repoDir;
		MavenSession session = legacySupport.getSession();
		if (session == null) {
			repoDir = RepositorySystem.defaultUserLocalRepository;
			offline = false;
			update = false;
			interactive = false;
		} else {
			offline = session.isOffline();
			repoDir = new File(session.getLocalRepository().getBasedir());
			update = session.getRequest().isUpdateSnapshots();
			interactive = session.getRequest().isInteractiveMode();
		}

		cacheLocation = new File(repoDir, ".cache/tycho");
		cacheLocation.mkdirs();
	}

	@Override
	public boolean isOffline() {
		return offline;
	}

	@Override
	public boolean isUpdate() {
		return update;
	}

	@Override
	public boolean isInteractive() {
		return interactive;
	}

	@Override
	public File getCacheLocation() {
		return cacheLocation;
	}

}
