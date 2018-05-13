package com.org.apnanews.helper;

import java.io.IOException;

/**
 * Created by POOJA on 1/30/2018.
 */

public class NoConnectivityException extends IOException {

    @Override
    public String getMessage() {
        return "No connectivity exception";
    }

}