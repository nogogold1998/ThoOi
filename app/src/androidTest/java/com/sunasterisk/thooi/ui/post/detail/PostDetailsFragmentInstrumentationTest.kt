package com.sunasterisk.thooi.ui.post.detail

// import androidx.arch.core.executor.testing.InstantTaskExecutorRule
// import androidx.fragment.app.testing.launchFragmentInContainer
// import androidx.test.espresso.Espresso.onView
// import androidx.test.espresso.IdlingRegistry
// import androidx.test.espresso.assertion.ViewAssertions.matches
// import androidx.test.espresso.matcher.ViewMatchers.withId
// import androidx.test.espresso.matcher.ViewMatchers.withText
// import androidx.test.ext.junit.runners.AndroidJUnit4
// import androidx.test.filters.MediumTest
// import com.sunasterisk.thooi.R
// import com.sunasterisk.thooi.data.model.PostDetail
// import com.sunasterisk.thooi.data.repository.FakePostDetailRepo
// import com.sunasterisk.thooi.util.DataBindingIdlingResource
// import com.sunasterisk.thooi.util.DummyPostDetails
// import com.sunasterisk.thooi.util.format
// import com.sunasterisk.thooi.util.idleTestThenRun
// import com.sunasterisk.thooi.util.monitorFragment
// import org.junit.After
// import org.junit.Before
// import org.junit.Rule
// import org.junit.Test
// import org.junit.runner.RunWith
//
// /**
//  * Created by Cong Vu Chi on 04/09/20 11:06.
//  */
// @MediumTest
// @RunWith(AndroidJUnit4::class)
// class PostDetailsFragmentInstrumentationTest {
//
//     @get:Rule
//     val instantTaskExecutor = InstantTaskExecutorRule()
//
//     // An Idling Resource that waits for Data Binding to have no pending bindings
//     private val dataBindingIdlingResource = DataBindingIdlingResource()
//
//     private lateinit var fakePostDetailRepo: FakePostDetailRepo
//
//     private val postDetails = DummyPostDetails.postDetails
//
//     // Subject
//     private lateinit var fragment: PostDetailsFragment
//
//     @Before
//     fun setUp() {
//         fakePostDetailRepo = FakePostDetailRepo()
//         fragment = PostDetailsFragment().apply {
//             // viewModelFactory = PostDetailsVM.Factory(fakePostDetailRepo, this)
//         }
//     }
//
//     /**
//      * Idling resources tell Espresso that the app is idle or busy. This is needed when operations
//      * are not scheduled in the main Looper (for example when executed on a different thread).
//      */
//     @Before
//     fun registerIdlingResource() {
//         IdlingRegistry.getInstance().register(dataBindingIdlingResource)
//     }
//
//     /**
//      * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
//      */
//     @After
//     fun unregisterIdlingResource() {
//         IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
//     }
//
//     // @Test
//     fun launchFragment() {
//         val givenId = postDetails.random().id
//         val args = PostDetailsFragmentArgs(givenId).toBundle()
//         val scenario = launchFragmentInContainer(args, R.style.AppTheme) { fragment }
//         dataBindingIdlingResource.monitorFragment(scenario)
//
//         onView(withId(R.id.textTitleDetail)).check(matches(withText(R.string.label_detail)))
//
//         val assertViews = { expected: PostDetail ->
//             onView(withId(R.id.textJobTitle))
//                 .check(matches(withText(expected.title)))
//             onView(withId(R.id.textPostedDate))
//                 .check(matches(withText(expected.postedDateTime.format())))
//             onView(withId(R.id.textUserFullName))
//                 .check(matches(withText(expected.customer.fullName)))
//             onView(withId(R.id.textUserLastActive))
//                 .check(matches(withText(expected.customer.lastActive.format())))
//             onView(withId(R.id.textJobDescription))
//                 .check(matches(withText(expected.description)))
//             onView(withId(R.id.textJobLocation))
//                 .check(matches(withText(expected.address)))
//         }
//
//         postDetails.map { it.copy(id = givenId) }
//             .forEach { given ->
//                 fakePostDetailRepo.channel.offer(given)
//                 assertViews(given)
//             }
//     }
//
//     @Test
//     fun launchFragmentIdle() {
//         val given = postDetails.random()
//         val args = PostDetailsFragmentArgs(given.id).toBundle()
//         val scenario = launchFragmentInContainer(args, R.style.AppTheme) { fragment }
//         dataBindingIdlingResource.monitorFragment(scenario)
//
//         fakePostDetailRepo.channel.offer(given)
//
//         idleTestThenRun(1_200_000) {
//             onView(withId(R.id.textJobLocation)).check(matches(withText(given.address)))
//         }
//     }
// }
