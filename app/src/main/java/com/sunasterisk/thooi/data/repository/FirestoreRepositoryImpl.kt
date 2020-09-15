package com.sunasterisk.thooi.data.repository

import com.sunasterisk.thooi.data.source.PostDataSource
import com.sunasterisk.thooi.data.source.entity.Post

class FirestoreRepositoryImpl(private val remote: PostDataSource.Remote) : FirestoreRepository {

    override suspend fun newPost(post: Post) = remote.addNewPost(post)

}
