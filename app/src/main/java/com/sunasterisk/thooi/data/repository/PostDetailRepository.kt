package com.sunasterisk.thooi.data.repository

import com.sunasterisk.thooi.data.model.PostDetail
import kotlinx.coroutines.flow.Flow

/**
 * Created by Cong Vu Chi on 04/09/20 10:27.
 */
interface PostDetailRepository {
    fun getPostDetailById(id: String): Flow<PostDetail>
}
