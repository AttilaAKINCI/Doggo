package com.akinci.doggoapp.feature.detail.view

import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.akinci.doggoapp.R
import com.akinci.doggoapp.common.component.SnackBar
import com.akinci.doggoapp.common.component.TileDrawable
import com.akinci.doggoapp.common.helper.state.ListState
import com.akinci.doggoapp.common.helper.state.UIState
import com.akinci.doggoapp.databinding.FragmentDetailBinding
import com.akinci.doggoapp.feature.detail.adapter.DetailListAdapter
import com.akinci.doggoapp.feature.detail.adapter.ShimmerAdapter
import com.akinci.doggoapp.feature.detail.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class DetailFragment : Fragment() {

    lateinit var binding: FragmentDetailBinding
    private val viewModel : DetailViewModel by viewModels()

    private val detailImageListAdapter = DetailListAdapter()
    private val shimmerAdapter = ShimmerAdapter()

    private val detailArgs by lazy { DetailFragmentArgs.fromBundle(requireArguments()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /** Initialization of ViewBinding, not need for DataBinding here **/
        binding = FragmentDetailBinding.inflate(layoutInflater)

        // set tile background
        val backgroundDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.pattern)
        binding.tileImageView.setImageDrawable(TileDrawable(backgroundDrawable!!, Shader.TileMode.REPEAT))

        Timber.d("DetailFragment created..")
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(detailArgs.subBreed.isNotBlank()){
            binding.detailTitleTextView.text = resources.getString(R.string.detail_title, detailArgs.breed, detailArgs.subBreed)
            viewModel.getSubBreeds(detailArgs.breed, detailArgs.subBreed)
        }else{
            binding.detailTitleTextView.text = detailArgs.breed
            viewModel.getBreeds(detailArgs.breed)
        }

        // observe breed image data
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.breedImageListData.collect{ state ->
                when(state){
                    is ListState.OnLoading -> {
                        binding.detailBreedImageRecyclerList.adapter = shimmerAdapter
                    }
                    is ListState.OnData -> {
                        binding.detailBreedImageRecyclerList.adapter = detailImageListAdapter
                        detailImageListAdapter.submitList(state.data)
                    }
                    else -> { /** NOP **/ }
                }
            }
        }

        // observe ui events
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect{ state ->
                when(state){
                    is UIState.OnServiceError -> {
                        SnackBar.make(binding.root, resources.getString(R.string.global_service_error))
                    }
                    else -> { /** NOP **/ }
                }
            }
        }

    }
}