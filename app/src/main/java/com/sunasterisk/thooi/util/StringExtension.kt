package com.sunasterisk.thooi.util

import android.util.Patterns.EMAIL_ADDRESS

const val PASSWORD_MIN_LENGTH = 5
const val REGEX_SPACE = "\\s+"

val String.isEmail get() = EMAIL_ADDRESS.matcher(this).matches() || isNullOrBlank()

val String.isValidPassword get() = !contains(REGEX_SPACE) && length > PASSWORD_MIN_LENGTH || isNullOrBlank()
