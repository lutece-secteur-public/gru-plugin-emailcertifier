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
package fr.paris.lutece.plugins.emailcertifier.service;

import fr.paris.lutece.plugins.emailcertifier.business.CertifiableEmail;
import fr.paris.lutece.plugins.emailcertifier.business.CertifiableEmailHome;
import fr.paris.lutece.plugins.emailcertifier.util.CertifierConstants;
import fr.paris.lutece.plugins.identitystore.web.rs.dto.AttributeDto;
import fr.paris.lutece.plugins.identitystore.web.rs.dto.AuthorDto;
import fr.paris.lutece.plugins.identitystore.web.rs.dto.IdentityChangeDto;
import fr.paris.lutece.plugins.identitystore.web.rs.dto.IdentityDto;
import fr.paris.lutece.plugins.identitystore.web.service.IdentityService;
import fr.paris.lutece.plugins.workflowcore.service.workflow.WorkflowService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.httpaccess.HttpAccessException;
import fr.paris.lutece.util.url.UrlItem;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class CertifiableEmailService
{

    private static CertifiableEmailService _instance;
    WorkflowService _workflowService = (WorkflowService) SpringContextService.getBean( CertifierConstants.BEAN_WORKFLOW_SERVICE );
    private static IdentityService _identityService;

    /**
     * Default constructor
     */
    private CertifiableEmailService( )
    {
    }

    /**
     * Get the instance of the service
     * @return the instance of the service
     */
    public static CertifiableEmailService getInstance( )
    {
        if ( _instance == null )
        {
            _identityService = (IdentityService) SpringContextService.getBean( CertifierConstants.BEAN_IDENTITYSTORE_SERVICE );
            return new CertifiableEmailService( );
        }
        return _instance;
    }

    /**
     * Get the Email user form the Guid of the user
     * @param strGuid
     * @return the email
     */
    public String getEmailFromGuid( String strGuid )
    {
        if ( strGuid != null )
        {
            String strEmail = null;
            try
            {
                IdentityDto identity = _identityService.getIdentityByConnectionId( strGuid, CertifierConstants.PROPERTY_EMAIL_CERTIFIER_APP_CODE );
                strEmail = identity.getAttributes( ).get( CertifierConstants.PROPERTY_ATTRIBUTE_IDENTITYSTORE_EMAIL ).getValue( );
            }
            catch( AppException e )
            {
                AppLogService.error( "Unable to find email attribute on given guid", e );
            }
            return strEmail;
        }
        return null;
    }

    /**
     * Generate a certifiable email object
     * @param strGuid the guid of the user
     * @param request the request
     * @return the certifiable email object created
     * @throws UserNotSignedException 
     */
    public CertifiableEmail generateEmailCertifiable( String strGuid, HttpServletRequest request ) throws UserNotSignedException
    {
        String strEmail = request.getParameter( CertifierConstants.PARAMETER_EMAIL );
        if ( strEmail != null )
        {
            CertifiableEmail certifEmail = new CertifiableEmail( strGuid, strEmail );
            CertifiableEmailHome.create( (CertifiableEmail) certifEmail );
            return certifEmail;
        }
        return null;
    }

    /**
     * Certify the email of the user
     * @param strGuid the guid of the user
     * @param strEmail the email of the user
     * @throws AppException 
     */
    public void certifyEmail( String strGuid, String strEmail ) throws AppException
    {
        IdentityChangeDto identityChange = new IdentityChangeDto( );
        IdentityDto identity = new IdentityDto( );
        identity.setConnectionId( strGuid );
        
        Map<String, AttributeDto> mapAttributes = new HashMap<String, AttributeDto>( );
        addAttribute( mapAttributes, "email", strEmail );
        identity.setAttributes( mapAttributes );
        
        identityChange.setIdentity( identity );
        
        AuthorDto author = new AuthorDto( );
        author.setApplicationCode( CertifierConstants.PROPERTY_EMAIL_CERTIFIER_APP_CODE );
        identityChange.setAuthor( author );
        
        String strErrorMessage = null;
        try
        {
            IdentityDto identityAfterCertification = _identityService.certifyAttributes( identityChange, CertifierConstants.PROPERTY_EMAIL_CERTIFIER_CODE );

            //Check if certification worked
            AttributeDto attributeEmailAfterCertif = (AttributeDto) identityAfterCertification.getAttributes( ).get( "email" );
            String strEmailAfterCertif = attributeEmailAfterCertif.getValue( );
            String strEmailCertificationCode = attributeEmailAfterCertif.getCertificate( ).getCertifierCode( );

            if ( !strEmailAfterCertif.equals( strEmail ) || !strEmailCertificationCode.equals( CertifierConstants.PROPERTY_EMAIL_CERTIFIER_CODE ) )
            {
                strErrorMessage = "The certification WS call is ok, but the certification has been rejected";
            }
        }
        catch ( Exception e )
        {
            strErrorMessage = "An error occured in IdentityStore service" ;
        }
        finally
        {
            if ( strErrorMessage != null )
            {
                throw new AppException ( strErrorMessage );
            }
        }
    }

    /**
     * Add an attribute of the attribute map, with key and value
     * @param map
     * @param strKey
     * @param strValue 
     */
    private void addAttribute( Map<String, AttributeDto> map, String strKey, String strValue )
    {
        AttributeDto attribute = new AttributeDto( );
        attribute.setKey( strKey );
        attribute.setValue( strValue );
        map.put( attribute.getKey( ), attribute );
    }

    /**
     * Generate a certification url (to string)
     * @param certifiableEmail
     * @return the certificiation url
     */
    public String generateCertifyLink( CertifiableEmail certifiableEmail )
    {
        UrlItem url = new UrlItem( AppPathService.getProdUrl( ) + AppPathService.getPortalUrl( ) );
        url.addParameter( CertifierConstants.PARAMETER_PAGE, CertifierConstants.XPAGE_EMAIL_CERTIFIER );
        url.addParameter( CertifierConstants.PARAMETER_ACTION, CertifierConstants.ACTION_CERTIFY_EMAIL );
        url.addParameter( CertifierConstants.PARAMETER_ID_CERTIFIABLE_EMAIL, certifiableEmail.getId( ) );
        url.addParameter( CertifierConstants.PARAMETER_TOKEN, certifiableEmail.getToken( ) );
        return url.toString( );
    }

    /**
     * Check if a certification is valid or not.
     * @param strUserGuid the user guid
     * @param request
     * @return true if valid, false otherwise
     */
    public boolean isValidCertification( String strUserGuid, HttpServletRequest request )
    {
        int nCertifiableEmail = Integer.parseInt( request.getParameter( CertifierConstants.PARAMETER_ID_CERTIFIABLE_EMAIL ) );
        CertifiableEmail certifEmail = CertifiableEmailHome.findByPrimaryKey( nCertifiableEmail );
        
        String strGuid = certifEmail.getGuid( );
        String strToken = certifEmail.getToken( );
        String strUserToken = request.getParameter( CertifierConstants.PARAMETER_TOKEN );
        Timestamp tUserDate = new Timestamp( System.currentTimeMillis( ) );
        Timestamp tDate = certifEmail.getDateCreation( );
        long tUserDelay = tUserDate.getTime( ) - tDate.getTime( );
        
        //Check the Guid
        if ( !strGuid.equals( strUserGuid ) )
        {
            return false;
        }
        //Check the token
        if ( !strToken.equals( strUserToken ) )
        {
            return false;
        }
        //Check the timestamp
        if ( CertifierConstants.PROPERTY_CERTIFICATION_DELAY >= 0 && tUserDelay * 0.001 > CertifierConstants.PROPERTY_CERTIFICATION_DELAY )
        {
            return false;
        }
        
        return true;
    }

    /**
     * Invalid the previous certification (when bad tries occur)
     * @param strGuid the user guid
     * @param request 
     */
    public void invalidPreviousCertificationTries( String strGuid, HttpServletRequest request )
    {
        String strEmail = request.getParameter( CertifierConstants.PARAMETER_EMAIL );
        List<CertifiableEmail> listCertifiableEmail = CertifiableEmailHome.getCertifiableEmailGuidOrEmail( strGuid, strEmail );
        for ( CertifiableEmail certifEmail : listCertifiableEmail )
        {
            _workflowService.doProcessAction( certifEmail.getId( ), CertifierConstants.WORKFLOW_EMAIL_CERTIFIABLE_RESOURCE_TYPE,
                    CertifierConstants.PROPERTY_WORKFLOW_ACTION_INVALID_CERTIFICATION_ID, -1, request, request.getLocale( ), false,
                    CertifierConstants.CONSTANT_AUTO );
        }
    }

}
