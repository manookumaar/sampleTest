package com.signuptest


import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MyProfileActivityTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MyProfileActivity::class.java)

    @Test
    fun signUpActivityTest() {
        val appCompatEditText = onView(
            allOf(
                withId(R.id.userNameInput),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    2
                )
            )
        )
        appCompatEditText.perform(scrollTo(), replaceText("Manojkumar M"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.emailInput),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    6
                )
            )
        )
        appCompatEditText2.perform(scrollTo(), replaceText("manookumaar@glo"), closeSoftKeyboard())

        val editText = onView(
            allOf(
                withId(R.id.emailInput), withText("manookumaar@glo"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.ScrollView::class.java))),
                isDisplayed()
            )
        )
        editText.check(matches(withText("manookumaar@glo")))
    }

    @Test
    fun performSaveButtonClickWithInValidEmailId() {
        val editText = onView(
            allOf(
                withId(R.id.emailInput), withText("manookumaar@glo"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.ScrollView::class.java))),
                isDisplayed()
            )
        )


        onView(withText("SAVE")).perform(click())

        onView(withId(R.id.emailInput)).check { view, _ ->
            assert((view as EditText).error.equals("Invalid email"))
        }
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
