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
package fr.paris.lutece.plugins.emailcertifier.util;

import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import java.util.Locale;

public class CertifierConstants
{
    // Beans
    public static final String BEAN_IDENTITYSTORE_SERVICE = "emailcertifier.identitystore.service";
    public static final String BEAN_WORKFLOW_SERVICE = "workflow.workflowService";
    public static final String BEAN_WORKFLOW_RESOURCE_HISTORY = "workflow.resourceHistoryService";

    // Properties
    public static final String PROPERTY_EMAIL_CERTIFIER_APP_CODE = AppPropertiesService.getProperty( "emailcertifier.identitystore.application.code" );
    public static final String PROPERTY_ATTRIBUTE_IDENTITYSTORE_EMAIL = AppPropertiesService.getProperty( "emailcertifier.identitystore.attribute.email" );
    public static final long PROPERTY_CERTIFICATION_DELAY = AppPropertiesService.getPropertyLong( "emailcertifier.certification.expirationDelay", -1 );
    public static final int PROPERTY_WORKFLOW_ACTION_INVALID_CERTIFICATION_ID = AppPropertiesService.getPropertyInt(
            "emailcertifier.workflow.action.invalid.certification.id", 903 );
    public static final String PROPERTY_EMAIL_CERTIFIER_CODE = AppPropertiesService.getProperty( "emailcertifier.identitystore.certifier.code" );
    public static final int PROPERTY_WORKFLOW_EMAIL_CERTIFICATION_ID = AppPropertiesService.getPropertyInt( "emailcertifier.workflow.id", 700 );
    public static final int PROPERTY_WORKFLOW_ACTION_CERTIFY_ID = AppPropertiesService.getPropertyInt( "emailcertifier.workflow.action.certify.id", 902 );

    // Views
    public static final String VIEW_HOME = "home";
    public static final String VIEW_ENABLE_FRONT_AUTHENTICATION = "getEnableFrontAuthentication";
    public static final String VIEW_CONFIRM_START_CERTIFICATION = "getConfirmStartCertification";
    public static final String VIEW_EMAIL_CERTIFIED = "getEmailCertified";
    public static final String VIEW_CERTIFICATION_ERROR = "getCertificationError";

    // Templates
    public static final String TEMPLATE_ENABLE_FRONT_AUTHENTICATION = "/skin/plugins/emailcertifier/enable_front_authentication.html";
    public static final String TEMPLATE_ASK_FOR_EMAIL_CERTIFICATION = "/skin/plugins/emailcertifier/ask_for_email_certification.html";
    public static final String TEMPLATE_CONFIRM_START_CERTIFICATION = "/skin/plugins/emailcertifier/confirm_start_certification.html";
    public static final String TEMPLATE_EMAIL_CERTIFIED = "/skin/plugins/emailcertifier/email_certified.html";
    public static final String TEMPLATE_CERTIFICATION_ERROR = "/skin/plugins/emailcertifier/template_certification_error.html";

    // Actions
    public static final String ACTION_CERTIFY_EMAIL = "doCertifyEmail";
    public static final String ACTION_START_CERTIFICATION = "startEmailCertification";
    public static final String ACTION_DO_CERTIFY_EMAIL = "doCertifyEmail";

    // Parameters
    public static final String PARAMETER_ID_CERTIFIABLE_EMAIL = "id_certifiable_email";
    public static final String PARAMETER_TOKEN = "token";
    public static final String PARAMETER_EMAIL = "user_email";
    public static final String PARAMETER_PAGE = "page";
    public static final String PARAMETER_ACTION = "action";

    // Markers
    public static final String MARK_USER_EMAIL = "user_email";
    public static final String MARK_CERTIFICATION_LINK = "certification_link";
    public static final String MARK_USER_FIRSTNAME = "user_firstname";
    public static final String MARK_USER_LASTNAME = "user_lastname";

    // Workflow
    public static final String WORKFLOW_EMAIL_CERTIFIABLE_RESOURCE_TYPE = "certifiable_email";
    public static final String CONSTANT_AUTO = "auto";
    public static final String XPAGE_EMAIL_CERTIFIER = "emailcertifier";
    public static final String CONSTANT_USER = "user";

    // Providers
    public static final String EMAIL_CERTIFICATION_PROVIDER_ID = "certifiable_email_provider";

    // I18n
    public static final String MESSAGE_VALIDATE_CERTIFICATION_TASK_TITLE = "emailcertifier.task.validateCertification.title";
    public static final String MESSAGE_EMAIL_CERTIFICATION_PROVIDER = I18nService.getLocalizedString( "emailcertifier.certifiableemail.provider.label",
            I18nService.getDefaultLocale( ) );
    public static final String MESSAGE_CERTIFICATION_LINK_DESCRIPTION = I18nService.getLocalizedString(
            "emailcertifier.provider.description.certificationLink", Locale.getDefault( ) );
    public static final String MESSAGE_USER_FIRSTNAME_DESCRIPTION = I18nService.getLocalizedString( "emailcertifier.provider.description.userFirstName",
            Locale.getDefault( ) );
    public static final String MESSAGE_USER_LASTNAME_DESCRIPTION = I18nService.getLocalizedString( "emailcertifier.provider.description.userLastName",
            Locale.getDefault( ) );
    public static final String MESSAGE_USER_EMAIL_DESCRIPTION = I18nService.getLocalizedString( "emailcertifier.provider.description.userEmail",
            Locale.getDefault( ) );
    public static final String MESSAGE_ERROR_INVALID_EMAIL = "emailcertifier.validation.certifiableemail.email.isNotValid";

    // Identitystore
    public static final String ATTRIBUTE_IDENTITYSTORE_FIRSTNAME = AppPropertiesService.getProperty( "emailcertifier.identitystore.attribute.firstname" );
    public static final String ATTRIBUTE_IDENTITYSTORE_LASTNAME = AppPropertiesService.getProperty( "emailcertifier.identitystore.attribute.lastname" );
    public static final String ATTRIBUTE_IDENTITYSTORE_MOBILE_PHONE = AppPropertiesService.getProperty( "emailcertifier.identitystore.attribute.mobilePhone" );
    public static final String ATTRIBUTE_IDENTITYSTORE_EMAIL = AppPropertiesService.getProperty( "emailcertifier.identitystore.attribute.email" );
    public static final String EMAIL_CERTIFIER_APP_CODE = AppPropertiesService.getProperty( "emailcertifier.identitystore.application.code" );

    // NotifyGRU
    public static final String EMAIL_CERTIFIER_DEMAND_TYPE_ID = AppPropertiesService.getProperty( "emailcertifier.demandType.id" );
    public static final String EMAIL_CERTIFIER_PREFIX = AppPropertiesService.getProperty( "emailcertifier.demandType.prefix" );

}
