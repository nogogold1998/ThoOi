package com.sunasterisk.thooi.util

import com.google.android.gms.maps.model.LatLng
import com.sunasterisk.thooi.data.source.entity.Post
import com.sunasterisk.thooi.data.source.entity.PostStatus
import com.sunasterisk.thooi.data.source.entity.User
import com.sunasterisk.thooi.data.source.entity.UserType
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

/**
 * Created by Cong Vu Chi on 04/09/20 14:09.
 */
object DummyPosts {
    val users = setOf(
        userOf("Vu Chi Cong"),
        userOf("Nguyen Quoc Khanh"),
        userOf("Dang Van Toan"),
        userOf("Nguyen Minh Tuan"),
        userOf("Dang Thanh Dat"),
        userOf("Tran Quang Huy"),
        userOf("Phuong Anh Tuan"),
        userOf("Nguyen Chinh Tho"),
    )

    private val titles = setOf(
        "Sua ong nuoc",
        "Sua tu lanh",
        "Sua dien thoai",
        "Sua may giat",
        "Trat tuong",
        "Trong tre",
        "Hack facebook",
        "Bao ve",
    )

    private val cachedDescriptions = List(users.size) { randomString(1000, 100) }

    private val cachedLocations = listOf(
        "Ha noi",
        "Me tri ha",
        "Khuong thuong",
        "Ha dong, Ha noi",
        "Me linh",
        "Dai hoc thuy loi 175 tay son dong da ha noi"
    )

    private val landscapes = listOf(
        "https://i.ytimg.com/vi/c7oV1T2j5mc/maxresdefault.jpg",
        "https://images.unsplash.com/photo-1506744038136-46273834b3fb?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjI0MX0&w=1000&q=80",
        "https://www.tom-archer.com/wp-content/uploads/2018/06/milford-sound-night-fine-art-photography-new-zealand.jpg",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRM77t9_VN4L_roxi2Ivdo0aLLmLicoop-VCA&usqp=CAU",
    )

    val posts = List(10) {
        val title = titles.random()
        Post(
            id = title.hashCode().toString(),
            address = cachedLocations.random(),
            appointment = LocalDateTime.now(),
            categoryRef = "asdf123",
            customerRef = users.random().id,
            description = cachedDescriptions.random(),
            fixerId = null,
            appliedFixerIds = users.map(User::id),
            landscapes,
            LatLng(0.0, 0.0),
            100000,
            PostStatus.NEW,
            null,
            title
        )
    }

    private fun userOf(name: String) = User(
        name.hashCode().toString(),
        "Ha noi",
        "Dep trai",
        LocalDateTime.now(),
        LocalDate.now(),
        "email@example.com",
        name,
        "https://www.w3schools.com/w3images/avatar2.png",
        LatLng(0.0, 0.0),
        "Vietnam Education Unit",
        "+841234",
        emptyList(),
        UserType.CUSTOMER
    )
}

fun randomString(length: Long, sample: Int) =
    (0..length / sample)
        .asSequence()
        .map { "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".random() }
        .joinToString(separator = "")
        .repeat(sample)
