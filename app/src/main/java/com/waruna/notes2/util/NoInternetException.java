package com.waruna.notes2.util;

import java.io.IOException;

public class NoInternetException extends IOException {
    public NoInternetException(String s) {
        super(s);
    }
}
