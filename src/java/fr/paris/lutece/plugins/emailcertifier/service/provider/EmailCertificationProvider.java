/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
package fr.paris.lutece.plugins.emailcertifier.service.provider;

import fr.paris.lutece.plugins.emailcertifier.business.CertifiableEmail;
import fr.paris.lutece.plugins.emailcertifier.business.CertifiableEmailHome;
import fr.paris.lutece.plugins.emailcertifier.service.CertifiableEmailService;
import fr.paris.lutece.plugins.emailcertifier.util.CertifierConstants;
import fr.paris.lutece.plugins.identitystore.web.rs.dto.AttributeDto;
import fr.paris.lutece.plugins.identitystore.web.rs.dto.IdentityDto;
import fr.paris.lutece.plugins.identitystore.web.service.IdentityService;
import fr.paris.lutece.plugins.workflow.modules.notifygru.service.provider.IProvider;
import fr.paris.lutece.plugins.workflow.modules.notifygru.service.provider.NotifyGruMarker;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.lang.StringUtils;

public class EmailCertificationProvider implements IProvider
{
	// PROPERTY KEY
	private static final String PROPERTY_SMS_SENDER_NAME = "emailcertifier.gruprovider.sms.sendername";
	
    private final CertifiableEmail _certifiableEmail;

    private final IdentityService _identityService;
    private final IdentityDto _identity;

    /**
     * Constructor for EmailCertificationProvider
     * 
     * @param resourceHistory
     */
    public EmailCertificationProvider( ResourceHistory resourceHistory )
    {
        _identityService = ( (IdentityService) SpringContextService.getBean( CertifierConstants.BEAN_IDENTITYSTORE_SERVICE ) );
        _certifiableEmail = CertifiableEmailHome.findByPrimaryKey( resourceHistory.getIdResource( ) );
        _identity = _identityService.getIdentityByConnectionId( _certifiableEmail.getGuid( ), CertifierConstants.EMAIL_CERTIFIER_APP_CODE );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String provideDemandId( )
    {
        return Integer.toString( _certifiableEmail.getId( ) );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String provideDemandTypeId( )
    {
        return CertifierConstants.EMAIL_CERTIFIER_DEMAND_TYPE_ID;
    }

    /**
	 * {@inheritDoc}
	 */
	@Override
	public String provideDemandSubtypeId()
	{
		return null;
	}

	/**
     * {@inheritDoc }
     */
    @Override
    public String provideDemandReference( )
    {
        return CertifierConstants.EMAIL_CERTIFIER_PREFIX + Integer.toString( _certifiableEmail.getId( ) );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String provideCustomerConnectionId( )
    {
        return _certifiableEmail.getGuid( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String provideCustomerId( )
    {
        return StringUtils.EMPTY;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String provideCustomerEmail( )
    {
        return _certifiableEmail.getEmail( );
    }

    /**
	 * {@inheritDoc}
	 */
	@Override
	public String provideSmsSender()
	{
		return AppPropertiesService.getProperty( PROPERTY_SMS_SENDER_NAME );
	}


    /**
     * {@inheritDoc }
     */
    @Override
    public String provideCustomerMobilePhone( )
    {
        AttributeDto attributeMobilePhone = _identity.getAttributes( ).get( CertifierConstants.ATTRIBUTE_IDENTITYSTORE_MOBILE_PHONE );
        if ( ( attributeMobilePhone != null ) && ( attributeMobilePhone.getValue( ) != null ) )
        {
            return attributeMobilePhone.getValue( );
        }
        return StringUtils.EMPTY;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<NotifyGruMarker> provideMarkerValues( )
    {
        Collection<NotifyGruMarker> colMarkers = new ArrayList( );

        if ( _identity != null )
        {
            addUserFistName( colMarkers, _identity );
            addUserLastName( colMarkers, _identity );
        }
        addUserEmail( colMarkers, _certifiableEmail.getEmail( ) );
        addCertifyLink( colMarkers );

        return colMarkers;
    }

    /**
     * Get the Provider markers descriptions
     * 
     * @return the provider markers description
     */
    public static Collection<NotifyGruMarker> getProviderMarkerDescription( )
    {
        Collection<NotifyGruMarker> colMarkers = new ArrayList( );

        NotifyGruMarker markerCertificationLink = new NotifyGruMarker( CertifierConstants.MARK_CERTIFICATION_LINK );
        NotifyGruMarker markerUserFirstName = new NotifyGruMarker( CertifierConstants.MARK_USER_FIRSTNAME );
        NotifyGruMarker markerUserLastName = new NotifyGruMarker( CertifierConstants.MARK_USER_LASTNAME );
        NotifyGruMarker markerUserEmail = new NotifyGruMarker( CertifierConstants.MARK_USER_EMAIL );

        markerCertificationLink.setDescription( CertifierConstants.MESSAGE_CERTIFICATION_LINK_DESCRIPTION );
        markerUserFirstName.setDescription( CertifierConstants.MESSAGE_USER_FIRSTNAME_DESCRIPTION );
        markerUserLastName.setDescription( CertifierConstants.MESSAGE_USER_LASTNAME_DESCRIPTION );
        markerUserEmail.setDescription( CertifierConstants.MESSAGE_USER_EMAIL_DESCRIPTION );

        colMarkers.add( markerCertificationLink );
        colMarkers.add( markerUserFirstName );
        colMarkers.add( markerUserLastName );
        colMarkers.add( markerUserEmail );

        return colMarkers;
    }

    /**
     * Add Certification link to markers collection
     * 
     * @param colMarkers
     *            the markers collection
     */
    private void addCertifyLink( Collection<NotifyGruMarker> colMarkers )
    {
        NotifyGruMarker certifyLinkMarker = new NotifyGruMarker( CertifierConstants.MARK_CERTIFICATION_LINK );
        certifyLinkMarker.setDescription( CertifierConstants.MESSAGE_CERTIFICATION_LINK_DESCRIPTION );
        certifyLinkMarker.setValue( CertifiableEmailService.getInstance( ).generateCertifyLink( _certifiableEmail ) );

        colMarkers.add( certifyLinkMarker );
    }

    /**
     * Add first name markers to markers collection
     * 
     * @param colMarkers
     *            the markers collection
     * @param identity
     *            the identity
     */
    private void addUserFistName( Collection<NotifyGruMarker> colMarkers, IdentityDto identity )
    {
        AttributeDto attributeFirstName = identity.getAttributes( ).get( CertifierConstants.ATTRIBUTE_IDENTITYSTORE_FIRSTNAME );
        if ( ( attributeFirstName != null ) && ( attributeFirstName.getValue( ) != null ) )
        {
            NotifyGruMarker certifyLinkMarker = new NotifyGruMarker( CertifierConstants.MARK_USER_FIRSTNAME );
            certifyLinkMarker.setDescription( CertifierConstants.MESSAGE_USER_FIRSTNAME_DESCRIPTION );
            certifyLinkMarker.setValue( attributeFirstName.getValue( ) );
            colMarkers.add( certifyLinkMarker );
        }
    }

    /**
     * Add last name markers to markers collection
     * 
     * @param colMarkers
     *            the markers collection
     * @param identity
     *            the identity
     */
    private void addUserLastName( Collection<NotifyGruMarker> colMarkers, IdentityDto identity )
    {
        AttributeDto attributeLastName = identity.getAttributes( ).get( CertifierConstants.ATTRIBUTE_IDENTITYSTORE_LASTNAME );
        if ( ( attributeLastName != null ) && ( attributeLastName.getValue( ) != null ) )
        {
            NotifyGruMarker certifyLinkMarker = new NotifyGruMarker( CertifierConstants.MARK_USER_LASTNAME );
            certifyLinkMarker.setDescription( CertifierConstants.MESSAGE_USER_LASTNAME_DESCRIPTION );
            certifyLinkMarker.setValue( attributeLastName.getValue( ) );
            colMarkers.add( certifyLinkMarker );
        }
    }

    /**
     * Add last name markers to markers collection
     * 
     * @param colMarkers
     *            the markers collection
     * @param identity
     *            the identity
     */
    private void addUserEmail( Collection<NotifyGruMarker> colMarkers, String strEmail )
    {
        NotifyGruMarker certifyLinkMarker = new NotifyGruMarker( CertifierConstants.MARK_USER_EMAIL );
        certifyLinkMarker.setDescription( CertifierConstants.MESSAGE_USER_EMAIL_DESCRIPTION );
        certifyLinkMarker.setValue( strEmail );

        colMarkers.add( certifyLinkMarker );
    }
}
