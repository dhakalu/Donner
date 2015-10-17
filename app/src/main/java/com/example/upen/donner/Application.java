package com.example.upen.donner;

import com.parse.Parse;

/**
 * @author Upendra Dhakal on 10/17/15.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "WToFqNTJTcPVvGWKOvwtGxt4XEx2IzVgYaw6tIZF", "u9Dr3P3bIV9Soyxmhw93MtZfKqaiZWzQKO4RTlt3");
    }
}
