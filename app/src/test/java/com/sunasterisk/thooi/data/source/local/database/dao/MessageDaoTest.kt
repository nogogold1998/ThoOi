package com.sunasterisk.thooi.data.source.local.database.dao

import org.junit.Test

/**
 * Tests that do not have body, derive "passed result" from [UserDaoTest]'s tests
 */
interface MessageDaoTest {

    @Test
    fun insert() = Unit

    @Test
    fun update() = Unit

    @Test
    fun getAllMessagesFlow() = Unit

    @Test
    fun getAllMessages() = Unit

    @Test
    fun findMessageByIdFlow() = Unit

    @Test
    fun findMessageById() = Unit

    @Test
    fun delete() = Unit

    @Test
    fun deleteAllMessages() = Unit
}
