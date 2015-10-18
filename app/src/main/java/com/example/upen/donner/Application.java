package com.example.upen.donner;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.paypal.android.MEP.PayPal;

/**
 * @author Upendra Dhakal on 10/17/15.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Organization.class);
        //Parse.initialize(this, "e4zSvaDk4Lwk65KwPtdnIFwA63aNjvGp0uNfHJXS", "QBFe3b3HeMouEwkv1gSSSJVFatKInG55piacpMfV");
        Parse.initialize(this, "QvZyNN8pgKy6XU8PteP6qoXu5V7sP7G5ZVZ4BBCo", "RJKaftWPPn2P4MHYKIQ8zUvsdPuQznqwSJUMfKKE");

    }
}
