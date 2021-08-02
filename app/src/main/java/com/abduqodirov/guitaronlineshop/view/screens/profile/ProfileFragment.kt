package com.abduqodirov.guitaronlineshop.view.screens.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.abduqodirov.guitaronlineshop.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    // TODO: 8/2/2021 persist user token. And refresh token
    // TODO: 8/2/2021 Get user data by id and display in UI

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

        setupClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupClickListeners() {
        binding.profileCreateProductBtn.setOnClickListener {
            navigateToSubmitProductScreen()
        }

        binding.tempSignInBtn.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToAuthGraph())
        }
    }

    private fun navigateToSubmitProductScreen() {

        findNavController().navigate(
            ProfileFragmentDirections.actionProfileFragmentToSubmitNewProductFragment()
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}
