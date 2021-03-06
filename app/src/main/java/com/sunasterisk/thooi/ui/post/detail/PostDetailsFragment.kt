package com.sunasterisk.thooi.ui.post.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.base.BaseFragment
import com.sunasterisk.thooi.databinding.FragmentDetailPostBinding
import com.sunasterisk.thooi.di.getViewModelFactory
import com.sunasterisk.thooi.ui.post.detail.model.PostDetailsAction
import com.sunasterisk.thooi.ui.post.detail.model.PostDetailsAction.CustomerAction.*
import com.sunasterisk.thooi.ui.post.detail.model.PostDetailsAction.FixerAction.ApplyJob
import com.sunasterisk.thooi.ui.post.detail.model.PostDetailsAction.FixerAction.StartFixing
import com.sunasterisk.thooi.util.MarginItemDecoration
import com.sunasterisk.thooi.util.observeEvent
import com.sunasterisk.thooi.util.toast

class PostDetailsFragment : BaseFragment<FragmentDetailPostBinding>() {

    private val args: PostDetailsFragmentArgs by navArgs()

    private val viewModel by viewModels<PostDetailsVM> { getViewModelFactory() }

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean,
    ) = FragmentDetailPostBinding.inflate(inflater, container, attachToRoot).also {
        it.postDetailsVM = viewModel
        it.lifecycleOwner = viewLifecycleOwner
    }

    override fun setupView() {
        setupSliderView()
        setupAppliedFixersRecyclerView()
    }

    override fun initListener() {
        binding.imageUpButton.setOnClickListener { findNavController().navigateUp() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadPost(args.postId)
        savedInstanceState
            ?.getInt(KEY_MOTION_STATE)
            ?.let(binding.constraintLayout::transitionToState)
    }

    override fun onObserveLiveData() = with(viewModel) {
        toastStringRes.observeEvent(viewLifecycleOwner) {
            context?.toast(getString(it))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_MOTION_STATE, binding.constraintLayout.currentState)
    }

    private fun setupSliderView() = with(binding.imageSliderJobThumbnails) {
        setSliderAdapter(ImageSliderAdapter())
        setIndicatorAnimation(IndicatorAnimationType.WORM)
        setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        isAutoCycle = true
        startAutoCycle()
    }

    private fun setupAppliedFixersRecyclerView() =
        with(binding.scrollViewJobDetails) {
            adapter = PostDetailsAdapter(viewModel.postDetail, viewLifecycleOwner) { action ->
                when (action) {
                    is PostDetailsAction.CustomerAction -> {
                        try {
                            val viewModel = checkViewModelInstance<CustomerPostDetailsVM>()
                            when (action) {
                                is SelectFixer -> viewModel.selectFixer(action.summaryUser)
                                AssignFixer -> viewModel.assignFixer()
                                ReassignFixer -> viewModel.reassignFixer()
                                CancelFixing -> viewModel.cancelFixing()
                                FinishFixing -> viewModel.finishFixing()
                                ClosePost -> viewModel.closePost()
                            }
                        } catch (_: IllegalStateException) {
                        }
                    }
                    is PostDetailsAction.FixerAction -> {
                        val viewModel = checkViewModelInstance<FixerPostDetailsVM>()
                        when (action) {
                            ApplyJob -> viewModel.applyJob()
                            StartFixing -> viewModel.startFixing()
                        }
                    }
                    is PostDetailsAction.ShowCustomer -> findNavController().navigate(
                        PostDetailsFragmentDirections.postDetailsToProfile(action.customerId)
                    )
                }
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(MarginItemDecoration(resources, R.dimen.dp_16, R.dimen.dp_16))
        }

    private inline fun <reified VM : PostDetailsVM> checkViewModelInstance() =
        checkNotNull(viewModel as? VM) {
            val expectClassName = VM::class.java.simpleName
            val actualClassName = viewModel::class.java.simpleName
            "Wrong instance of PostDetailsVM, required $expectClassName but was $actualClassName"
        }
}

private const val KEY_MOTION_STATE = "motion_state"
