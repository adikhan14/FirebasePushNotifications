package com.app.firebasepushnotifications;

import android.support.annotation.NonNull;

/**
 * Created by Adil khan on 2/12/2018.
 */

public class UserId {

    public String userId;

    public <T extends UserId> T withId(@NonNull final String id) {
        this.userId = id;
        return (T) this;
    }
}
