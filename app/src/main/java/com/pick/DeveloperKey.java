package com.pick;

import com.google.android.gms.common.Scopes;
import com.google.api.services.youtube.YouTubeScopes;

/**
 * Static container class for holding a reference to your YouTube Developer Key.
 */
public class DeveloperKey {

  public static final String DEVELOPER_KEY = "AIzaSyBijEOd0_xLXHc-f_dvMEIYBr0n2O16rNo";

  public static final String[] SCOPES = {Scopes.PROFILE ,
                                        YouTubeScopes.YOUTUBE, YouTubeScopes.YOUTUBE_UPLOAD};
}
