package com.sunasterisk.thooi.ui.post.detail.model

import com.sunasterisk.thooi.data.model.SummaryUser

/**
 * Created by Cong Vu Chi on 10/09/20 20:01.
 */
sealed class PostDetailsAction {

    data class SelectFixer(val summaryUser: SummaryUser) : PostDetailsAction()

    object AssignFixer : PostDetailsAction()

    object ReassignFixer : PostDetailsAction()

    object CancelFixing : PostDetailsAction()

    object FinishFixing : PostDetailsAction()

    object ClosePost : PostDetailsAction()

    companion object {
        @JvmStatic
        fun assignFixer() = AssignFixer

        @JvmStatic
        fun reassignFixer() = ReassignFixer

        @JvmStatic
        fun cancelFixing() = CancelFixing

        @JvmStatic
        fun finishFixing() = FinishFixing

        @JvmStatic
        fun closePost() = ClosePost
    }
}
