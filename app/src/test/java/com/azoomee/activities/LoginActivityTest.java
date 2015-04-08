package com.azoomee.activities;

import android.content.Intent;

import com.azoomee.R;
import com.azoomee.helpers.SynchronousExecutor;
import com.azoomee.rest.LoginApi;
import com.azoomee.transport.Credentials;
import com.azoomee.transport.Token;

import org.androidannotations.api.BackgroundExecutor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.robolectric.Robolectric.*;

@Config(emulateSdk = 18, manifest = "app/src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class LoginActivityTest {
    private static final String TOKEN_STR = "c34nc7n3w47cnw9f8nhwf";
    private LoginActivity_ activity;

    @Before
    public void setup() {
        BackgroundExecutor.setExecutor(new SynchronousExecutor());
        activity = setupActivity(LoginActivity_.class);
    }


    @Test
    public void staysOnLoginScreenWhenApiFails() {
        //Given a failing attempt to talk to the REST API...
        LoginApi loginApiMock = mock(LoginApi.class);
        activity.loginApi = loginApiMock;
        when(loginApiMock.login(any(Credentials.class))).thenReturn(null);

        //When
        activity.userNameEditText.setText("some user");
        activity.pwdEditText.setText("some pwd");
        clickSubmit();

        //Then
        staysOnLoginScreen();
    }


    @Test
    public void staysOnLoginScreenWhenPwdIsEmpty() {
        //Given
        LoginApi loginApiMock = mock(LoginApi.class);
        activity.loginApi = loginApiMock;
        when(loginApiMock.login(any(Credentials.class))).thenReturn(new Token(TOKEN_STR));

        //When password is missing
        activity.userNameEditText.setText("some user");
        clickSubmit();

        //Then
        staysOnLoginScreen();

        //When
        activity.pwdEditText.setText("some pwd");
        clickSubmit();

        //Then
        switchesToProfileScreenPassingToken();
    }

    @Test
    public void staysOnLoginScreenWhenNameIsEmpty() {
        //Given
        LoginApi loginApiMock = mock(LoginApi.class);
        activity.loginApi = loginApiMock;
        when(loginApiMock.login(any(Credentials.class))).thenReturn(new Token(TOKEN_STR));

        //When password is missing
        activity.pwdEditText.setText("some pwd");
        clickSubmit();

        //Then
        staysOnLoginScreen();

        //When
        activity.userNameEditText.setText("some user");
        clickSubmit();

        //Then
        switchesToProfileScreenPassingToken();
    }


    @Test
    public void credentialsArePassedToApi() {
        //Given
        LoginApi loginApiMock = mock(LoginApi.class);
        activity.loginApi = loginApiMock;
        String expectedUser = "some user";
        String expectedPwd = "some pwd";

        //When
        activity.userNameEditText.setText(expectedUser);
        activity.pwdEditText.setText(expectedPwd);
        clickSubmit();


        //Then
        ArgumentCaptor<Credentials> credentialsCaptor = ArgumentCaptor.forClass(Credentials.class);
        verify(loginApiMock).login(credentialsCaptor.capture());
        Credentials apiCredentials = credentialsCaptor.getValue();
        assertEquals(expectedUser, apiCredentials.getUsername());
        assertEquals(expectedPwd, apiCredentials.getPassword());
    }

    @Test
    public void failureMsgIsDisplayedAsToast() {
        //Given
        Robolectric.addPendingHttpResponse(403, "Not authorized");

        //When
        activity.userNameEditText.setText("some user");
        activity.pwdEditText.setText("some pwd");
        clickSubmit();

        //Then
        assertEquals("Failure to login", ShadowToast.getTextOfLatestToast());
    }


    private void clickSubmit() {
        activity.findViewById(R.id.submitButton).performClick();
    }

    private void switchesToProfileScreenPassingToken() {
        Intent expectedIntent = new Intent(activity, ProfileActivity_.class);
        Intent launchedIntent = shadowOf(activity).getNextStartedActivity();
        assertEquals(expectedIntent.getAction(), launchedIntent.getAction());
        assertEquals(TOKEN_STR, launchedIntent.getExtras().getString(ProfileActivity_.TOKEN_EXTRA));
    }

    private void staysOnLoginScreen() {
        Intent launchedIntent = shadowOf(activity).getNextStartedActivity();
        assertNull(launchedIntent);
    }
}