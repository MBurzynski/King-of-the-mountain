package com.example.marcin.kingofthemountain;

import android.content.Intent;

import com.samsandberg.stravaauthenticator.StravaAuthenticateActivity;
import com.samsandberg.stravaauthenticator.StravaScopes;

import java.util.Arrays;
import java.util.Collection;


public class AuthActivity extends StravaAuthenticateActivity {

    /*****************************************
     * Methods override START
     */

    /**
     * Client ID
     */
    protected String getStravaClientId() {
        return "24468";
    }

    /**
     * Client Secret
     */
    protected String getStravaClientSecret() {
        return "4b683e6eb514e4a4fec54a06ce0f519a155fcb80";
    }

    /**
     * Scopes to auth for
     * (default public)
     */
    protected Collection<String> getStravaScopes() {
        return Arrays.asList(StravaScopes.SCOPE_VIEW_PRIVATE_AND_WRITE);
    }

    /**
     * Should we use the local cache?
     * (default true)
     */
    protected boolean getStravaUseCache() {
        return true;
    }

    /**
     * Should we check a token (against Strava's API) or should we just assume it's good?
     * (default true)
     */
    protected boolean getStravaCheckToken() {
        return true;
    }

    /**
     * What intent should we kick off, given OK auth
     */
    protected Intent getStravaActivityIntent() {
        return new Intent(this, MainActivity.class);
    }

    /**
     * Should we finish this activity after successful auth + kicking off next activity?
     * (default true)
     */
    protected boolean getStravaFinishOnComplete() {
        return true;
    }

    /**
     * Methods override END
     ****************************************/
}