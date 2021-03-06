package my.demo.app6

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withHint
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Single
import my.demo.app6.model.Business
import my.demo.app6.model.BusinessRepo
import my.demo.app6.model.BusinessSearchResponse
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExternalResource
import org.junit.rules.RuleChain
import org.mockito.ArgumentMatchers.anyString

class MainActivityTest {

    val testBusiness = Business("Test Works!", "")
    @get:Rule
    val activityRule: RuleChain = RuleChain.emptyRuleChain()
            .around(object : ExternalResource() {
                override fun before() {
                    BusinessRepo.mock = mock {
                        on { search(anyString()) }.doReturn(
                                Single.just(BusinessSearchResponse(1, listOf(testBusiness))))
                    }
                }

                override fun after() {
                    BusinessRepo.mock = null
                }
            })
            .around(ActivityTestRule(MainActivity::class.java))

    @Test
    fun whenStarted_showsHelloWorld() {
        onView(withText(R.string.greeting)).check(matches(isDisplayed()))
    }

    @Test
    fun whenSearching_showsFirstResult() {
        onView(withHint(R.string.query_hint))
                .perform(typeText("Restaurant"))
                .perform(pressImeActionButton())
        onView(withText(testBusiness.name)).check(matches(isDisplayed()))
    }
}
