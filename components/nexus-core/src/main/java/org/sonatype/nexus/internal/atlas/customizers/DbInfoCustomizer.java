/*
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2008-present Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package org.sonatype.nexus.internal.atlas.customizers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.internal.support.DbDiagnostics;
import org.sonatype.nexus.supportzip.GeneratedContentSourceSupport;
import org.sonatype.nexus.supportzip.SupportBundle;
import org.sonatype.nexus.supportzip.SupportBundleCustomizer;

import org.apache.commons.io.FileUtils;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.sonatype.nexus.supportzip.SupportBundle.ContentSource.Priority.HIGH;
import static org.sonatype.nexus.supportzip.SupportBundle.ContentSource.Type.DBINFO;
/**
 * Creates and adds db info file to support bundle.
 *
 */
@Named
@Singleton
public class DbInfoCustomizer
    extends ComponentSupport
    implements SupportBundleCustomizer
{
  private final DbDiagnostics dbDiagnostics;

  @Inject
  public DbInfoCustomizer(final DbDiagnostics dbDiagnostics) {
    this.dbDiagnostics = checkNotNull(dbDiagnostics);
  }

  @Override
  public void customize(final SupportBundle supportBundle) {
    supportBundle.add(new GeneratedContentSourceSupport(DBINFO, "info/dbFileInfo.txt", HIGH)
    {
      @Override
      protected void generate(final File file) throws IOException {
        String dbInfoOutput = dbDiagnostics.getDbFileInfo();
        FileUtils.write(file, dbInfoOutput, StandardCharsets.UTF_8);
      }
    });
  }
}
