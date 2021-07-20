package com.abduqodirov.guitaronlineshop.view.screens.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.abduqodirov.guitaronlineshop.R
import com.abduqodirov.guitaronlineshop.databinding.FragmentProfileBinding
import com.abduqodirov.guitaronlineshop.view.screens.BottomNavScreenDirections

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileCreateProductBtn.setOnClickListener {
            navigateToSubmitProductScreen()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToSubmitProductScreen() {
        val mainNavController = Navigation.findNavController(
            requireActivity(),
            R.id.main_fragment_container_view
        )

        mainNavController.navigate(
            BottomNavScreenDirections.actionBottomMainToSubmitNewProductFragment()
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}
