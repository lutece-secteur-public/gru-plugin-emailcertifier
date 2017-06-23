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

package fr.paris.lutece.plugins.emailcertifier.web;

import fr.paris.lutece.plugins.emailcertifier.business.CertifiableEmail;
import fr.paris.lutece.plugins.emailcertifier.service.CertifiableEmailService;
import fr.paris.lutece.plugins.emailcertifier.util.CertifierConstants;
import fr.paris.lutece.plugins.emailcertifier.util.MailValidator;
import fr.paris.lutece.plugins.workflowcore.service.workflow.WorkflowService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.xpages.XPage;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

@Controller( xpageName = "emailcertifier", pageTitleI18nKey = "emailcertifier.xpage.emailcertifier.pageTitle", pagePathI18nKey = "emailcertifier.xpage.emailcertifier.pagePathLabel" )
public class EmailCertifierApp extends MVCApplication
{

    WorkflowService _workflowService = (WorkflowService) SpringContextService.getBean( CertifierConstants.BEAN_WORKFLOW_SERVICE );

    /**
     * Default constructor
     */
    public EmailCertifierApp( )
    {
    }

    /**
     * Get the home default view
     * 
     * @param request
     *            the request
     * @return the XPage home page
     * @throws UserNotSignedException
     */
    @View( value = CertifierConstants.VIEW_HOME, defaultView = true )
    public XPage viewHome( HttpServletRequest request ) throws UserNotSignedException
    {
        Map<String, Object> model = getModel( );

        if ( SecurityService.isAuthenticationEnable( ) )
        {
            LuteceUser user = checkUserAuthentication( request );
            String strEmail = CertifiableEmailService.getInstance( ).getEmailFromGuid( user.getName( ) );
            model.put( CertifierConstants.MARK_USER_EMAIL, strEmail );
        }

        return getXPage( CertifierConstants.TEMPLATE_ASK_FOR_EMAIL_CERTIFICATION, request.getLocale( ), model );
    }

    /**
     * Perform the action of starting certification
     * 
     * @param request
     * @return the confirm starting email certification page
     * @throws UserNotSignedException
     */
    @Action( value = CertifierConstants.ACTION_START_CERTIFICATION )
    public XPage doStartEmailCertification( HttpServletRequest request ) throws UserNotSignedException
    {
        if ( SecurityService.isAuthenticationEnable( ) )
        {
            LuteceUser user = checkUserAuthentication( request );

            if ( !MailValidator.isValidEmail( request.getParameter( CertifierConstants.PARAMETER_EMAIL ) ) )
            {
                Map<String,Object> model = getModel( );
                model.put( CertifierConstants.MARK_USER_EMAIL, request.getParameter( CertifierConstants.PARAMETER_EMAIL ) );
                addError( CertifierConstants.MESSAGE_ERROR_INVALID_EMAIL, request.getLocale( ) );
                return getXPage( CertifierConstants.TEMPLATE_ASK_FOR_EMAIL_CERTIFICATION, request.getLocale( ), model );
            }

            CertifiableEmailService.getInstance( ).invalidPreviousCertificationTries( user.getName( ), request );

            CertifiableEmail certifiableEmail = CertifiableEmailService.getInstance( ).generateEmailCertifiable( user.getName( ), request );
            _workflowService.getState( certifiableEmail.getId( ), CertifierConstants.WORKFLOW_EMAIL_CERTIFIABLE_RESOURCE_TYPE,
                    CertifierConstants.PROPERTY_WORKFLOW_EMAIL_CERTIFICATION_ID, -1 );

            return redirectView( request, CertifierConstants.VIEW_CONFIRM_START_CERTIFICATION );
        }

        return redirectView( request, CertifierConstants.VIEW_ENABLE_FRONT_AUTHENTICATION );
    }

    /**
     * Perform the certification process
     * 
     * @param request
     * @return the success of failure page for email certification process
     * @throws UserNotSignedException
     */
    @Action( CertifierConstants.ACTION_DO_CERTIFY_EMAIL )
    public XPage doCertifyEmail( HttpServletRequest request ) throws UserNotSignedException
    {
        if ( SecurityService.isAuthenticationEnable( ) )
        {
            LuteceUser user = checkUserAuthentication( request );
            int nIdCertifiableEmail = Integer.parseInt( request.getParameter( CertifierConstants.PARAMETER_ID_CERTIFIABLE_EMAIL ) );

            if ( CertifiableEmailService.getInstance( ).isValidCertification( user.getName( ), request ) )
            {
                try
                {
                    _workflowService.doProcessAction( nIdCertifiableEmail, CertifierConstants.WORKFLOW_EMAIL_CERTIFIABLE_RESOURCE_TYPE,
                            CertifierConstants.PROPERTY_WORKFLOW_ACTION_CERTIFY_ID, -1, request, getLocale( request ), false, CertifierConstants.CONSTANT_USER );

                }
                catch( Exception e )
                {
                    return redirectView( request, CertifierConstants.VIEW_CERTIFICATION_ERROR );
                }

                return redirectView( request, CertifierConstants.VIEW_EMAIL_CERTIFIED );
            }

            _workflowService.doProcessAction( nIdCertifiableEmail, CertifierConstants.WORKFLOW_EMAIL_CERTIFIABLE_RESOURCE_TYPE,
                    CertifierConstants.PROPERTY_WORKFLOW_ACTION_INVALID_CERTIFICATION_ID, -1, request, getLocale( request ), false,
                    CertifierConstants.CONSTANT_USER );

            return redirectView( request, CertifierConstants.VIEW_CERTIFICATION_ERROR );
        }

        return redirectView( request, CertifierConstants.VIEW_ENABLE_FRONT_AUTHENTICATION );
    }

    /**
     * Check if FrontOffice user is signed, throw exception otherwise.
     * 
     * @param request
     * @return the LuteceUser
     * @throws UserNotSignedException
     */
    private LuteceUser checkUserAuthentication( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser luteceUser = SecurityService.isAuthenticationEnable( ) ? SecurityService.getInstance( ).getRegisteredUser( request ) : null;

        if ( luteceUser == null )
        {
            throw new UserNotSignedException( );
        }
        return luteceUser;
    }

    /**
     * Get the Enable Front Auth page
     * 
     * @param request
     * @return the XPage for enabling front authentication
     */
    @View( CertifierConstants.VIEW_ENABLE_FRONT_AUTHENTICATION )
    public XPage getEnableFrontAuth( HttpServletRequest request )
    {
        return getXPage( CertifierConstants.TEMPLATE_ENABLE_FRONT_AUTHENTICATION, request.getLocale( ) );
    }

    /**
     * Get the Confirm Start Email certification page
     * 
     * @param request
     * @return the Confirm start email certification page
     */
    @View( CertifierConstants.VIEW_CONFIRM_START_CERTIFICATION )
    public XPage getConfirmStartCertification( HttpServletRequest request )
    {
        return getXPage( CertifierConstants.TEMPLATE_CONFIRM_START_CERTIFICATION, request.getLocale( ) );
    }

    /**
     * Get the certification error page
     * 
     * @param request
     * @return the certification error page
     */
    @View( CertifierConstants.VIEW_CERTIFICATION_ERROR )
    public XPage getCertificationError( HttpServletRequest request )
    {
        return getXPage( CertifierConstants.TEMPLATE_CERTIFICATION_ERROR, request.getLocale( ) );
    }

    /**
     * Get the certified email page.
     * 
     * @param request
     * @return the certified email page.
     */
    @View( CertifierConstants.VIEW_EMAIL_CERTIFIED )
    public XPage getEmailCertified( HttpServletRequest request )
    {
        return getXPage( CertifierConstants.TEMPLATE_EMAIL_CERTIFIED, request.getLocale( ) );
    }
}
