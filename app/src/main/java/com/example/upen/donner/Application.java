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
        //ParseObject.registerSubclass(Event.class);
        Parse.initialize(this, "HFqIhFYZRk05b6ibci3XwmL0ltlmLaEojZ1DDFvf", "JAtMNyevfQv7KOJLgujBs1jqqB3JYCq56ikIValo");
        //PushService.setDefaultPushCallback(this, MainActivity.class);
        //ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
