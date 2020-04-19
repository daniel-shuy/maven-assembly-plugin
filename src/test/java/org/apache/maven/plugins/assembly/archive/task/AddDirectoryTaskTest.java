package org.apache.maven.plugins.assembly.archive.task;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.FileSet;
import org.easymock.classextension.EasyMockSupport;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.Collections;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.fail;

public class AddDirectoryTaskTest
{
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private EasyMockSupport mockManager;

    private Archiver archiver;

    @Before
    public void setUp()
    {
        mockManager = new EasyMockSupport();

        archiver = mockManager.createMock( Archiver.class );
    }

    @Test
    public void testAddDirectory_ShouldNotAddDirectoryIfNonExistent()
        throws Exception
    {
        File dir = new File( temporaryFolder.getRoot(), "non-existent." + System.currentTimeMillis() );

        configureModeExpectations( -1, -1, -1, -1, false );

        mockManager.replayAll();

        AddDirectoryTask task = new AddDirectoryTask( dir );

        task.execute( archiver );

        mockManager.verifyAll();
    }

    @Test

    public void testAddDirectory_ShouldAddDirectory()
        throws Exception
    {
        File dir = temporaryFolder.getRoot();

        try
        {
            archiver.addFileSet( (FileSet) anyObject() );
        }
        catch ( ArchiverException e )
        {
            fail( "Should never happen." );
        }

        configureModeExpectations( -1, -1, -1, -1, false );

        mockManager.replayAll();

        AddDirectoryTask task = new AddDirectoryTask( dir );

        task.setOutputDirectory( "dir" );

        task.execute( archiver );

        mockManager.verifyAll();
    }

    @Test
    public void testAddDirectory_ShouldAddDirectoryWithDirMode()
        throws Exception
    {
        File dir = temporaryFolder.getRoot();

        try
        {
            archiver.addFileSet( (FileSet) anyObject() );
        }
        catch ( ArchiverException e )
        {
            fail( "Should never happen." );
        }

        int dirMode = Integer.parseInt( "777", 8 );
        int fileMode = Integer.parseInt( "777", 8 );

        configureModeExpectations( -1, -1, dirMode, fileMode, true );

        mockManager.replayAll();

        AddDirectoryTask task = new AddDirectoryTask( dir );

        task.setDirectoryMode( dirMode );
        task.setFileMode( fileMode );
        task.setOutputDirectory( "dir" );

        task.execute( archiver );

        mockManager.verifyAll();
    }

    @Test
    public void testAddDirectory_ShouldAddDirectoryWithIncludesAndExcludes()
        throws Exception
    {
        File dir = temporaryFolder.getRoot();

        try
        {
            archiver.addFileSet( (FileSet) anyObject() );
        }
        catch ( ArchiverException e )
        {
            fail( "Should never happen." );
        }

        configureModeExpectations( -1, -1, -1, -1, false );

        mockManager.replayAll();

        AddDirectoryTask task = new AddDirectoryTask( dir );

        task.setIncludes( Collections.singletonList( "**/*.txt" ) );
        task.setExcludes( Collections.singletonList( "**/README.txt" ) );
        task.setOutputDirectory( "dir" );

        task.execute( archiver );

        mockManager.verifyAll();
    }

    private void configureModeExpectations( int defaultDirMode, int defaultFileMode, int dirMode, int fileMode,
                                            boolean expectTwoSets )
    {
        expect( archiver.getOverrideDirectoryMode() ).andReturn( defaultDirMode );
        expect( archiver.getOverrideFileMode() ).andReturn( defaultFileMode );

        if ( expectTwoSets )
        {
            if ( dirMode > -1 )
            {
                archiver.setDirectoryMode( dirMode );
            }

            if ( fileMode > -1 )
            {
                archiver.setFileMode( fileMode );
            }
        }

        if ( dirMode > -1 )
        {
            archiver.setDirectoryMode( defaultDirMode );
        }

        if ( fileMode > -1 )
        {
            archiver.setFileMode( defaultFileMode );
        }
    }

}
