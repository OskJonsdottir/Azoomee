package com.azoomee.activities;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.azoomee.R;
import com.azoomee.activities.errorhandling.ActivityRestErrorHandler;
import com.azoomee.rest.ProfileApi;
import com.azoomee.transport.Sibling;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_profile)
public class ProfileActivity extends Activity {

    @Extra
    String token;

    @RestService
    ProfileApi profileApi;

    @Bean
    ActivityRestErrorHandler errorHandler;

    @ViewById(R.id.listView)
    ListView listView;

    private ArrayList<String> siblingNames;

    @AfterInject
    void setup() {
        profileApi.setRestErrorHandler(errorHandler);
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveProfile();
    }

    @Background(id = "retrieveProfile")
    void retrieveProfile() {
        List<Sibling> siblings = profileApi.profile(token);
        if (siblings != null) {
            siblingNames = new ArrayList<String>();
            for (Sibling sibling : siblings) {
                siblingNames.add(sibling.getName());
            }
            refreshList();
        }
    }

    @UiThread
    void refreshList() {
        listView.setAdapter(
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, siblingNames));
        listView.invalidate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
