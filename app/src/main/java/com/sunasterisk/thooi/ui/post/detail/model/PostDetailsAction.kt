package com.sunasterisk.thooi.ui.post.detail.model

import com.sunasterisk.thooi.data.model.SummaryUser

/**
 * Created by Cong Vu Chi on 10/09/20 20:01.
 */
sealed class PostDetailsAction {

    sealed class CustomerAction : PostDetailsAction() {

        data class SelectFixer(val summaryUser: SummaryUser) : CustomerAction()

        object AssignFixer : CustomerAction()

        object ReassignFixer : CustomerAction()

        object CancelFixing : CustomerAction()

        object FinishFixing : CustomerAction()

        object ClosePost : CustomerAction()

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

    sealed class FixerAction : PostDetailsAction() {
        object ApplyJob : FixerAction()

        object StartFixing : FixerAction()

        companion object {
            @JvmStatic
            fun applyJob() = ApplyJob

            @JvmStatic
            fun startFixing() = StartFixing
        }
    }
}
