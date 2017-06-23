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

import fr.paris.lutece.plugins.emailcertifier.util.TokenService;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.sql.Timestamp;
import org.hibernate.validator.constraints.Email;

/**
 * This is the business class for the object CertifiableEmail
 */
public class CertifiableEmail implements Serializable
{
    private static final long serialVersionUID = 1L;

    // Variables declarations
    private int _nId;

    @Size( max = 255, message = "#i18n{emailcertifier.validation.certifiableemail.Email.size}" )
    private String _strEmail;

    private String _strToken;

    private Timestamp _tDateCreation;

    @Size( max = 255, message = "#i18n{emailcertifier.validation.certifiableemail.Guid.size}" )
    private String _strGuid;

    /**
     * Default constructor
     */
    public CertifiableEmail( )
    {
    }

    /**
     * Get a certifiable Email object with given
     * 
     * @param strGuid
     * @param strEmail
     */
    public CertifiableEmail( String strGuid, String strEmail )
    {
        _strEmail = strEmail;
        _strGuid = strGuid;
        _strToken = TokenService.getInstance( ).generateToken( );
        _tDateCreation = new Timestamp( System.currentTimeMillis( ) );
    }

    /**
     * Returns the Id
     * 
     * @return The Id
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Sets the Id
     * 
     * @param nId
     *            The Id
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the Email
     * 
     * @return The Email
     */
    public String getEmail( )
    {
        return _strEmail;
    }

    /**
     * Sets the Email
     * 
     * @param strEmail
     *            The Email
     */
    public void setEmail( String strEmail )
    {
        _strEmail = strEmail;
    }

    /**
     * Returns the Token
     * 
     * @return The Token
     */
    public String getToken( )
    {
        return _strToken;
    }

    /**
     * Sets the Token
     * 
     * @param strToken
     *            The Token
     */
    public void setToken( String strToken )
    {
        _strToken = strToken;
    }

    /**
     * Returns the DateCreation
     * 
     * @return The DateCreation
     */
    public Timestamp getDateCreation( )
    {
        return _tDateCreation;
    }

    /**
     * Sets the DateCreation
     * 
     * @param tDateCreation
     *            The DateCreation
     */
    public void setDateCreation( Timestamp tDateCreation )
    {
        _tDateCreation = tDateCreation;
    }

    /**
     * Returns the Guid
     * 
     * @return The Guid
     */
    public String getGuid( )
    {
        return _strGuid;
    }

    /**
     * Sets the Guid
     * 
     * @param strGuid
     *            The Guid
     */
    public void setGuid( String strGuid )
    {
        _strGuid = strGuid;
    }
}
