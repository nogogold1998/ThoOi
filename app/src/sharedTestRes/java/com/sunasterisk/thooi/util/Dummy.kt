package com.sunasterisk.thooi.util

import com.sunasterisk.thooi.data.source.entity.User
import com.sunasterisk.thooi.data.source.entity.UserType

object Dummy {
    val users = arrayOf(
        User.default.copy(
            id = "dqyagwub3iFWNd6sHGMf",
            address = "123, duong 5, quan 6",
            fullName = "Dang Van Toan",
            email = "dvtoan@excample.com",
            phone = "+8401234",
            userType = UserType.CUSTOMER
        ),
        User.default.copy(
            id = "iSB5RP69tDCct4V1rqnA",
            address = "123, quan 4",
            fullName = "Nguyen Quoc Khanh",
            email = "gmail@example.com",
            phone = "+8404112",
            professions = listOf(
                "categories/IB0dCMdYgoF4OJcng5iv",
                "categories/EOdq0TmYzm7ZpkvRCskA"
            ),
            userType = UserType.FIXER
        ),
        User.default.copy(
            id = "IB0dCMdYgoF4OJcng5iv",
            address = "123",
            fullName = "Vu Chi Cong",
            userType = UserType.CUSTOMER
        ),
        User.default.copy(
            "EOdq0TmYzm7ZpkvRCskA",
            fullName = "Nguyen Minh Tuan C",
            address = "123, duong 4",
            userType =  UserType.FIXER
        )
    )
}
