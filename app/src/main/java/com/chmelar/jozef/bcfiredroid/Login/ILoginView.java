package com.chmelar.jozef.bcfiredroid.Login;


public interface ILoginView {
    void toggleLoadingAnimation();

    void setEmailFormatError();

    void displayNetworkingError();

    void goToMainScreen();

    void registerScreen();
}
