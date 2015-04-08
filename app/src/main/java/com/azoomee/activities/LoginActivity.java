package com.azoomee.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.azoomee.R;
import com.azoomee.activities.errorhandling.LoginActivityRestErrorHandler;
import com.azoomee.rest.LoginApi;
import com.azoomee.transport.Credentials;
import com.azoomee.transport.Token;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

@EActivity(R.layout.activity_login)
public class LoginActivity extends Activity {

    @ViewById
    EditText userNameEditText;

    @ViewById
    EditText pwdEditText;

    @RestService
    LoginApi loginApi;

    @Bean
    LoginActivityRestErrorHandler errorHandler; //ActivityRestErrorHandler would be more informative

    @AfterInject
    void setup() {
        loginApi.setRestErrorHandler(errorHandler);
    }

    @Click(R.id.submitButton)
    void submitButtonClick() {
        if (validateFieldNotEmpty(userNameEditText) & validateFieldNotEmpty(pwdEditText)) {
            String userName = userNameEditText.getText().toString();
            String pwd = pwdEditText.getText().toString();
            //TODO: prevent multiple login attempts from running in parallel, to save bandwidth
            login(userName, pwd);
        }
    }

    @Background(id = "login")
    void login(String userName, String pwd) {
        Credentials credentials = new Credentials(userName, pwd);
        Token token = loginApi.login(credentials);
        if (token != null) {
            navigateToProfileActivity(token);
        }
    }

    @UiThread
    void navigateToProfileActivity(Token token) {
        ProfileActivity_.intent(this)
                .token(token.getToken())
                .flags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT) //To prevent the activity from being created if it already exists
                .start();
    }

    private boolean validateFieldNotEmpty(EditText editText) {
        if (editText.getText().toString().isEmpty()) {
            editText.setBackgroundColor(Color.RED);
            return false;
        } else {
            editText.setBackgroundColor(Color.TRANSPARENT);
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
