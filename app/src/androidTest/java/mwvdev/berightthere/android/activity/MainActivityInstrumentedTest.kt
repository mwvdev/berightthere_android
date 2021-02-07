package mwvdev.berightthere.android.activity

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import mwvdev.berightthere.android.MainActivity
import mwvdev.berightthere.android.R
import mwvdev.berightthere.android.dagger.DaggerTestApplicationRule
import mwvdev.berightthere.android.repository.ITripRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentedTest {

    private lateinit var repository: ITripRepository

    @get:Rule
    val rule = DaggerTestApplicationRule()

    @Before
    fun setupDaggerComponent() {
        repository = rule.component.tripRepository
    }

    @Test
    fun startTripButtonSendsTripActivityIntent() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.startTripButton)).perform(click())

        onView(withId(R.id.trip_fragment_container)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}
