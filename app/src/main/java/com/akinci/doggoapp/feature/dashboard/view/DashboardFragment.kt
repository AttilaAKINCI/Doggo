package com.akinci.doggoapp.feature.dashboard.view

import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.transition.Fade
import androidx.transition.TransitionInflater
import androidx.transition.TransitionSet
import com.akinci.doggoapp.R
import com.akinci.doggoapp.common.component.SnackBar
import com.akinci.doggoapp.common.component.TileDrawable
import com.akinci.doggoapp.databinding.FragmentDashboardBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /** Initialization of ViewBinding, not need for DataBinding here **/
        binding = FragmentDashboardBinding.inflate(layoutInflater)

        //hide appbar on splash screen
        (activity as AppCompatActivity).supportActionBar?.show()

        /** view transition configuration **/
        val enterTransitionSet = TransitionSet()
        enterTransitionSet.addTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.move))
        enterTransitionSet.duration = 1000
        sharedElementEnterTransition = enterTransitionSet

        val enterFade = Fade()
        enterFade.startDelay = 1000
        enterFade.duration = 300
        enterTransition = enterFade

        val exitFade = Fade()
        exitFade.startDelay = 0
        exitFade.duration = 300
        exitTransition = exitFade
        /** **/

        // set tile background
        val backgroundDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.pattern)
        binding.tileImageView.setImageDrawable(TileDrawable(backgroundDrawable!!, Shader.TileMode.REPEAT))

        binding.openToDetailButton.setOnClickListener{
            SnackBar.make(binding.root, resources.getString(R.string.choose_breed_msg)).show()
            navigateToDetailPage()
        }

        Timber.d("DashboardFragment created..")
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun navigateToDetailPage(){
        /** Navigate user to detail page. **/
        Timber.d("Navigated to  DetailFragment..")
        NavHostFragment.findNavController(this).navigate(R.id.action_dashboardFragment_to_detailFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.animation.playAnimation()

    }

}