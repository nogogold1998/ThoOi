package com.sunasterisk.thooi.ui.post.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.sunasterisk.thooi.base.BaseFragment
import com.sunasterisk.thooi.databinding.FragmentDetailPostBinding

class PostDetailsFragment : BaseFragment<FragmentDetailPostBinding>() {

    @VisibleForTesting
    lateinit var viewModelFactory: PostDetailsVM.Factory

    private val args: PostDetailsFragmentArgs by navArgs()

    private val viewModel by viewModels<PostDetailsVM> { viewModelFactory }

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean,
    ) = FragmentDetailPostBinding.inflate(inflater, container, attachToRoot).also {
        it.postDetailsVM = viewModel
        it.lifecycleOwner = viewLifecycleOwner
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadPost(args.postId)
        setupSliderView()
    }

    private fun setupSliderView() = with(binding.imageSliderJobThumbnails) {
        setSliderAdapter(ImageSliderAdapter())
        setIndicatorAnimation(IndicatorAnimationType.WORM)
        setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        isAutoCycle = true
        startAutoCycle()
    }
}
