package com.sdr.lib.support.fingerprint;

import android.os.CancellationSignal;
import android.support.annotation.NonNull;

interface IBiometricPromptImpl {
    void authenticate(@NonNull CancellationSignal cancel, @NonNull BiometricPromptManager.OnBiometricIdentifyCallback callback);
}