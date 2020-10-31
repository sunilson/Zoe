package at.sunilson.testcore

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.reflect.full.createInstance

/**
 * Helper class for device Fragment tests
 */
@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
@HiltAndroidTest
abstract class BaseFragmentTest {

    /* TODO
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

     */

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @MockK
    lateinit var navController: NavController

    @Before
    fun mainBefore() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Intents.init()
        setupMocks()
    }

    private fun setupMocks() {
        every { navController.currentDestination } returns mockk {
            every { getAction(any()) } returns mockk {
                every { destinationId } returns 2
            }
            every { id } returns 1
        }
        every { navController.navigate(any<NavDirections>()) } returns Unit
        every { navController.navigate(any<Int>()) } returns Unit
        every { navController.navigateUp() } returns true
    }

    @After
    fun mainAfter() {
        //Intents.release()
    }

    /**
     * Use this to verify an Intent has been called
     *
     * @param block Do your clicks and stuff here and then return the validation matcher of the Intent
     */
    fun checkIntent(block: () -> Matcher<Intent>) {
        val intent = Intent()
        val intentResult = ActivityResult(Activity.RESULT_OK, intent)
        intending(anyIntent()).respondWith(intentResult)
        intended(block())
    }

    /**
     * Simple wrapper around [launchFragmentInContainer]
     *
     * @param Pass the App theme here so everything is styled correctly
     * @param argsBlock Put arguments inside this block so the fragment navArgs are set
     */
    inline fun <reified F : Fragment> launchScenario(
        theme: Int,
        argsBlock: Bundle.() -> Unit = {}
    ): FragmentScenario<F> {
        val args = Bundle().apply { argsBlock() }
        return launchFragmentInContainer(themeResId = theme, fragmentArgs = args) {
            F::class.createInstance().also {
                it.viewLifecycleOwnerLiveData.observeForever { lifecycleOwner ->
                    if (lifecycleOwner != null) {
                        Navigation.setViewNavController(it.requireView(), navController)
                    }
                }
            }
        }.also { it.moveToState(Lifecycle.State.RESUMED) }
    }
}
