/*
 * Copyright (c) 2002-2016, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */

package fr.paris.lutece.plugins.emailcertifier.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides Data Access methods for CertifiableEmail objects
 */
public final class CertifiableEmailDAO implements ICertifiableEmailDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_certifiable_email ) FROM emailcertifier_certifiable_email";
    private static final String SQL_QUERY_SELECT = "SELECT id_certifiable_email, email, token, date_creation, guid FROM emailcertifier_certifiable_email WHERE id_certifiable_email = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO emailcertifier_certifiable_email ( id_certifiable_email, email, token, date_creation, guid ) VALUES ( ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM emailcertifier_certifiable_email WHERE id_certifiable_email = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE emailcertifier_certifiable_email SET id_certifiable_email = ?, email = ?, token = ?, date_creation = ?, guid = ? WHERE id_certifiable_email = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_certifiable_email, email, token, date_creation, guid FROM emailcertifier_certifiable_email";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_certifiable_email FROM emailcertifier_certifiable_email";
    private static final String SQL_QUERY_SELECT_BY_GUID_AND_EMAIL = "SELECT id_certifiable_email, email, token, date_creation, guid FROM emailcertifier_certifiable_email WHERE guid = ? OR email = ?";

    /**
     * Generates a new primary key
     * 
     * @param plugin
     *            The Plugin
     * @return The new primary key
     */
    public int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery( );
        int nKey = 1;

        if ( daoUtil.next( ) )
        {
            nKey = daoUtil.getInt( 1 ) + 1;
        }

        daoUtil.free( );
        return nKey;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( CertifiableEmail certifiableEmail, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        certifiableEmail.setId( newPrimaryKey( plugin ) );
        int nIndex = 1;

        daoUtil.setInt( nIndex++, certifiableEmail.getId( ) );
        daoUtil.setString( nIndex++, certifiableEmail.getEmail( ) );
        daoUtil.setString( nIndex++, certifiableEmail.getToken( ) );
        daoUtil.setTimestamp( nIndex++, certifiableEmail.getDateCreation( ) );
        daoUtil.setString( nIndex++, certifiableEmail.getGuid( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public CertifiableEmail load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery( );
        CertifiableEmail certifiableEmail = null;

        if ( daoUtil.next( ) )
        {
            certifiableEmail = new CertifiableEmail( );
            int nIndex = 1;

            certifiableEmail.setId( daoUtil.getInt( nIndex++ ) );
            certifiableEmail.setEmail( daoUtil.getString( nIndex++ ) );
            certifiableEmail.setToken( daoUtil.getString( nIndex++ ) );
            certifiableEmail.setDateCreation( daoUtil.getTimestamp( nIndex++ ) );
            certifiableEmail.setGuid( daoUtil.getString( nIndex++ ) );
        }

        daoUtil.free( );
        return certifiableEmail;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( CertifiableEmail certifiableEmail, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        int nIndex = 1;

        daoUtil.setInt( nIndex++, certifiableEmail.getId( ) );
        daoUtil.setString( nIndex++, certifiableEmail.getEmail( ) );
        daoUtil.setString( nIndex++, certifiableEmail.getToken( ) );
        daoUtil.setTimestamp( nIndex++, certifiableEmail.getDateCreation( ) );
        daoUtil.setString( nIndex++, certifiableEmail.getGuid( ) );
        daoUtil.setInt( nIndex, certifiableEmail.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<CertifiableEmail> selectCertifiableEmailsList( Plugin plugin )
    {
        List<CertifiableEmail> certifiableEmailList = new ArrayList<CertifiableEmail>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            CertifiableEmail certifiableEmail = new CertifiableEmail( );
            int nIndex = 1;

            certifiableEmail.setId( daoUtil.getInt( nIndex++ ) );
            certifiableEmail.setEmail( daoUtil.getString( nIndex++ ) );
            certifiableEmail.setToken( daoUtil.getString( nIndex++ ) );
            certifiableEmail.setDateCreation( daoUtil.getTimestamp( nIndex++ ) );
            certifiableEmail.setGuid( daoUtil.getString( nIndex++ ) );

            certifiableEmailList.add( certifiableEmail );
        }

        daoUtil.free( );
        return certifiableEmailList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdCertifiableEmailsList( Plugin plugin )
    {
        List<Integer> certifiableEmailList = new ArrayList<Integer>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            certifiableEmailList.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );
        return certifiableEmailList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ReferenceList selectCertifiableEmailsReferenceList( Plugin plugin )
    {
        ReferenceList certifiableEmailList = new ReferenceList( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            certifiableEmailList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free( );
        return certifiableEmailList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<CertifiableEmail> selectCertifiableEmailByGuidOrEmail( String strGuid, String strEmail, Plugin plugin )
    {
        List<CertifiableEmail> certifiableEmailList = new ArrayList( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_GUID_AND_EMAIL, plugin );
        daoUtil.setString( 1, strGuid );
        daoUtil.setString( 2, strEmail );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            CertifiableEmail certifiableEmail = new CertifiableEmail( );
            int nIndex = 1;

            certifiableEmail.setId( daoUtil.getInt( nIndex++ ) );
            certifiableEmail.setEmail( daoUtil.getString( nIndex++ ) );
            certifiableEmail.setToken( daoUtil.getString( nIndex++ ) );
            certifiableEmail.setDateCreation( daoUtil.getTimestamp( nIndex++ ) );
            certifiableEmail.setGuid( daoUtil.getString( nIndex++ ) );

            certifiableEmailList.add( certifiableEmail );
        }

        daoUtil.free( );
        return certifiableEmailList;
    }
}
