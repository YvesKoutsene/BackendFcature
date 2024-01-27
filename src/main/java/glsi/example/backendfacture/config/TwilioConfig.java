package glsi.example.backendfacture.config;

import com.twilio.Twilio;

public class TwilioConfig {
    public static final String ACCOUNT_SID = "AC37207c8fe0ce82ed9ce72d769f188abe";
    public static final String AUTH_TOKEN = "63fd3dbe8c8e10ccfaa0a6e671c6f2e1";
    public static final String TWILIO_NUMBER = "+14155238886";

    public static void init(String accountSid, String authToken) {
        Twilio.init(accountSid, authToken);
    }
}
