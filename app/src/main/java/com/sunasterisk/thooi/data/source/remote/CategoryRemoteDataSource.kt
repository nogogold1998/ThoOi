package com.sunasterisk.thooi.data.source.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.sunasterisk.thooi.data.Result
import com.sunasterisk.thooi.data.source.CategoryDataSource
import com.sunasterisk.thooi.data.source.entity.Category
import com.sunasterisk.thooi.data.source.remote.dto.FirestoreCategory
import com.sunasterisk.thooi.util.getSnapshotFlow
import com.sunasterisk.thooi.util.toObjectWithId
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class CategoryRemoteDataSource(
    database: FirebaseFirestore
) : CategoryDataSource.Remote {

    private val categoryCollection = database.collection(RemoteConstants.CATEGORIES_COLLECTION)

    override fun getCategories(): Flow<Result<List<Category>>> = callbackFlow {
        offer(Result.loading())

        val listener = categoryCollection.addSnapshotListener { snapshot, exception ->
            val categories = ArrayList<Category>()
            snapshot?.documents?.forEach { document ->
                document.toObjectWithId(FirestoreCategory::class.java, Category::class)?.let {
                    categories.add(it)
                }
            }
            try {
                offer(Result.success(categories))
            } catch (e: CancellationException) {
                offer(Result.failed(e))
            }

            exception?.let {
                offer(Result.failed(it))
                cancel(it.message.toString())
            }
        }

        awaitClose {
            listener.remove()
            cancel()
        }
    }

    override fun getCategoryById(id: String): Flow<Result<Category>> =
        categoryCollection.document(id).getSnapshotFlow {
            it.toObjectWithId(FirestoreCategory::class.java, Category::class)
        }
}
