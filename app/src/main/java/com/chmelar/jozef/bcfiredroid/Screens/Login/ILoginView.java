package com.chmelar.jozef.bcfiredroid.Screens.Login;


import com.chmelar.jozef.bcfiredroid.API.Model.LoginResponse;

public interface ILoginView {
    void toggleLoadingAnimation();

    void setEmailFormatError();

    void displayNetworkingError();

    void goToProjects(LoginResponse user);

}
