package com.example.upen.donner;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

/**
 * @author Upendra Dhakal on 10/17/15.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "e4zSvaDk4Lwk65KwPtdnIFwA63aNjvGp0uNfHJXS", "QBFe3b3HeMouEwkv1gSSSJVFatKInG55piacpMfV");
    }
}
