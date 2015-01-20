package com.devoxx.espresso.samples.test;

import android.app.Activity;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.internal.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.devoxx.espresso.samples.R;
import com.devoxx.espresso.samples.model.Picture;
import com.devoxx.espresso.samples.ui.LoginActivity;
import com.squareup.spoon.Spoon;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.Collection;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

public class SamplesTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private Activity currentActivity;

    static {
        Espresso.registerIdlingResources(TestUtils.espressoThreadPool);
    }


    public SamplesTest() {
        super(LoginActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getActivity();
        TestUtils.injectPicturesApi();
    }

    public void testEspressoFeatures() throws Exception {
        Spoon.screenshot(getCurrentActivity(), "login_page");
        onView(withId(R.id.login_btn)).perform(click());

        DrawerActions.closeDrawer(R.id.drawer_layout);

        Spoon.screenshot(getCurrentActivity(), "landscapes");
        onData(withPictureTitle(is("Landscape 1")))
                .inAdapterView(withId(R.id.pictures_grid_view))
                .onChildView(withText("Landscape 1"))
                .perform(click());

        Spoon.screenshot(getCurrentActivity(), "scoll");
        onView(withId(R.id.scroll_to_me)).perform(scrollTo(), click());

        Espresso.pressBack();
        Thread.sleep(500);

        DrawerActions.openDrawer(R.id.drawer_layout);
        Spoon.screenshot(getCurrentActivity(), "drawer_opened");

        onView(withText(equalToIgnoringCase("Kittens"))).perform(click());

        Spoon.screenshot(getCurrentActivity(), "kittens");
        onData(withPictureTitle(is("Kittens 3")))
                .inAdapterView(withId(R.id.pictures_grid_view))
                .onChildView(withText("Kittens 3"))
                .perform(click());
    }

    private static Matcher<Object> withPictureTitle(final Matcher<String> stringMatcher) {
        return new BoundedMatcher<Object, Picture>(Picture.class) {
            @Override
            protected boolean matchesSafely(Picture picture) {
                return stringMatcher.matches(picture.title);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("for picture title ").appendDescriptionOf(stringMatcher);
            }
        };
    }

    public Activity getCurrentActivity(){
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                Collection<Activity> resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                for (Activity act : resumedActivities) {
                    Log.d("Your current activity: ", act.getClass().getName());
                    currentActivity = act;
                    break;
                }
            }
        });

        return currentActivity;
    }
}
