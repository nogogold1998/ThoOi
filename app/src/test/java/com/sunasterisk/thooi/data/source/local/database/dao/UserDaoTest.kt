package com.sunasterisk.thooi.data.source.local.database.dao

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.sunasterisk.thooi.BuildConfig
import com.sunasterisk.thooi.data.source.entity.UserType
import com.sunasterisk.thooi.data.source.local.database.AppDataBase
import com.sunasterisk.thooi.util.Dummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.time.ExperimentalTime

@FlowPreview
@ExperimentalTime
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
// robolectric does not support api > 28
@Config(minSdk = BuildConfig.MIN_SDK_VERSION, maxSdk = Build.VERSION_CODES.P)
class UserDaoTest {
    private lateinit var db: AppDataBase

    // Subject
    private lateinit var userDao: UserDao

    @get:Rule
    val instanceExecutorRule = InstantTaskExecutorRule()

    private val users = Dummy.users

    @Before
    fun setUp() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().context.applicationContext
        db = AppDataBase.buildTestAppDataBase(context)
        userDao = db.userDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insert() = runBlockingTest {

        users.first()
            .also {
                userDao.insert(it)
                val actual = userDao.getAllUsers()
                assertThat(actual, contains(it))
            }
            .copy(fullName = "Vladimir Putin")
            // replace case
            .also {
                userDao.insert(it)
                val actual = userDao.getAllUsers()
                assertThat(actual, contains(it))
            }

        users.let {
            userDao.insert(*it)
            val actual = userDao.getAllUsers()
            assertThat(actual, containsInAnyOrder(*it))
        }
    }

    @Test
    fun update() = runBlockingTest {
        users.first()
            .also { userDao.insert(it) }
            .copy(fullName = "Vladimir Putin", phone = "+123123")
            .also { userDao.update(it) }
            .let {
                val actual = userDao.getAllUsers()
                assertThat(actual, contains(it))
            }

        users[1].let {
            userDao.update(it)
            val actual = userDao.getAllUsers()
            assertThat(actual, not(hasItem(it)))
        }
    }

    @Test
    fun getAllUsers() = Unit

    @Test
    fun getAllUsersFlow() = runBlockingTest {
        val flow = userDao.getAllUsersFlow()
        assertThat(flow.first().size, `is`(0))

        users.first()
            .also {
                userDao.insert(it)
                val actual = flow.first()
                assertThat(actual, contains(it))
            }
            .copy(fullName = "Vladimir Putin", phone = "+123123")
            .let {
                userDao.insert(it)
                val actual = flow.first()
                assertThat(actual, contains(it))
            }

        users[1].also {
            userDao.insert(it)
        }
            .copy(fullName = "Trinh Trung Kien", bio = "ca si", userType = UserType.CUSTOMER)
            .let {
                userDao.update(it)
                val actual = flow.first()
                assertThat(actual, hasItem(it))
            }

        users.let {
            userDao.insert(*it)
            val actual = flow.first()
            assertThat(actual.size, equalTo(it.size))
            assertThat(actual, containsInAnyOrder(*it))
        }
    }

    @Test
    fun delete() = runBlockingTest {
        users
            .also { userDao.insert(*it) }
            .first()
            .let {
                userDao.delete(it)
                val actual = userDao.getAllUsers()
                assertThat(actual, not(hasItem(it)))
            }

        users.let {
            userDao.delete(*it)
            val actual = userDao.getAllUsers()
            assertThat(actual, `is`(emptyList()))
        }
    }

    @Test
    fun deleteAllUsers() = runBlockingTest {
        users.let {
            userDao.insert(*it)
            userDao.deleteAllUsers()
            val actual = userDao.getAllUsers()
            assertThat(actual, `is`(emptyList()))
        }
    }

    @Test
    fun findUserById() = runBlockingTest {
        users.first().let {
            userDao.insert(it)
            val actual = userDao.findUserById(it.id)
            assertThat(actual, `is`(it))
        }

        assertThat(userDao.findUserById("asdasd"), `is`(nullValue()))
    }
}
