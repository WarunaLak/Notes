package com.waruna.notes2.ui.auth;

import com.waruna.notes2.data.network.responses.AuthResponse;

public interface AuthListener {
    void onError(Throwable t);

    void onSuccess(AuthResponse login);
}
