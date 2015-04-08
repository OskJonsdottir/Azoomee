package com.azoomee.activities.errorhandling;

import android.app.Activity;
import android.widget.Toast;

import com.azoomee.R;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.api.rest.RestErrorHandler;
import org.springframework.core.NestedRuntimeException;

@EBean
public class LoginActivityRestErrorHandler implements RestErrorHandler {

    @RootContext
    Activity context;

    @StringRes(R.string.loginFailure)
    String errorMessage;

    @Override
    public void onRestClientExceptionThrown(NestedRuntimeException e) {
        reportExceptionOnUi(e);
    }

    @UiThread
    void reportExceptionOnUi(NestedRuntimeException e) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
    }
}
