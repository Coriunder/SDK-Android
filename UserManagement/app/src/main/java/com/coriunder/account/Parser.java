package com.coriunder.account;

import com.coriunder.account.models.CookieDecodeResult;
import com.coriunder.account.models.LoginResult;
import com.coriunder.base.utils.BaseParser;

import org.json.JSONObject;

/**
 * Parser methods for Account services
 */
public class Parser extends BaseParser {
    /**
     * Method to parse JSONObject to CookieDecodeResult object
     * @param response JSONObject to parse
     * @return CookieDecodeResult object
     */
    protected static CookieDecodeResult parseCookieDecoder(JSONObject response) {
        CookieDecodeResult result = new CookieDecodeResult();
        JSONObject jsonResult = Parser.getJsonObject(response, "d");
        result.setCode(Parser.getInt(jsonResult, "Code"));
        result.setSuccess(Parser.getBoolean(jsonResult, "IsSuccess"));
        result.setKey(Parser.getString(jsonResult, "Key"));
        result.setMessage(Parser.getString(jsonResult, "Message"));
        result.setNumber(Parser.getString(jsonResult, "Number"));
        result.setEmail(Parser.getString(jsonResult, "Email"));
        result.setFullName(Parser.getString(jsonResult, "FullName"));
        return result;
    }

    /**
     * Method to parse JSONObject to LoginResult object
     * @param response JSONObject to parse
     * @return LoginResult object
     */
    protected static LoginResult parseLoginResult(JSONObject response) {
        LoginResult result = new LoginResult();
        JSONObject jsonResult = Parser.getJsonObject(response, "d");
        result.setCode(Parser.getInt(jsonResult, "Code"));
        result.setSuccess(Parser.getBoolean(jsonResult, "IsSuccess"));
        result.setKey(Parser.getString(jsonResult, "Key"));
        result.setMessage(Parser.getString(jsonResult, "Message"));
        result.setNumber(Parser.getString(jsonResult, "Number"));
        result.setCredentialsHeaderName(Parser.getString(jsonResult, "CredentialsHeaderName"));
        result.setCredentialsToken(Parser.getString(jsonResult, "CredentialsToken"));
        result.setEncodedCookie(Parser.getString(jsonResult, "EncodedCookie"));
        result.setDeviceActivated(Parser.getBoolean(jsonResult, "IsDeviceActivated"));
        result.setDeviceBlocked(Parser.getBoolean(jsonResult, "IsDeviceBlocked"));
        result.setDeviceRegistered(Parser.getBoolean(jsonResult, "IsDeviceRegistered"));
        result.setDeviceRegistrationRequired(Parser.getBoolean(jsonResult, "IsDeviceRegistrationRequired"));
        result.setFirstLogin(Parser.getBoolean(jsonResult, "IsFirstLogin"));
        result.setLastLogin(Parser.getReadableDate(jsonResult, "LastLogin"));
        result.setVersionUpdateRequired(Parser.getBoolean(jsonResult, "VersionUpdateRequired"));
        return result;
    }
}