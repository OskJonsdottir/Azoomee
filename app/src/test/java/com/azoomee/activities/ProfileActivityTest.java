package com.azoomee.activities;

import com.azoomee.helpers.SynchronousExecutor;
import com.azoomee.rest.ProfileApi;
import com.azoomee.transport.Sibling;

import org.androidannotations.api.BackgroundExecutor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.robolectric.Robolectric.*;

@Config(emulateSdk = 18, manifest = "app/src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class ProfileActivityTest {

    public static final String TOKEN_STR = "894bv34tocgnso7gxn";
    private ActivityController<ProfileActivity_> activityController;

    @Before
    public void setup() {
        BackgroundExecutor.setExecutor(new SynchronousExecutor());
        activityController = buildActivity(ProfileActivity_.class);
    }

    @Test
    public void profileNamesAreDisplayed() {
        //Given
        Sibling sibling1 = new Sibling("John Doe");
        Sibling sibling2 = new Sibling("Amy Winehouse");

        ProfileApi profileApiMock = mock(ProfileApi.class);
        when(profileApiMock.profile(anyString())).thenReturn(Arrays.asList(sibling1, sibling2));

        activityController.create(); //This injects dependencies

        ProfileActivity_ activity = activityController.get();
        activity.profileApi = profileApiMock;
        activity.token = TOKEN_STR;

        //When
        activityController.start().postCreate(null).resume().visible();

        //Then
        verify(profileApiMock).profile(TOKEN_STR);
        assertEquals(2, activity.listView.getAdapter().getCount());
        assertEquals(sibling1.getName(), activity.listView.getAdapter().getItem(0));
        assertEquals(sibling2.getName(), activity.listView.getAdapter().getItem(1));
    }
}
