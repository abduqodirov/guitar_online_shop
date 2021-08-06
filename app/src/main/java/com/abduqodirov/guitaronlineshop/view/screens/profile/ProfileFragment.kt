package com.abduqodirov.guitaronlineshop.view.screens.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.abduqodirov.guitaronlineshop.data.model.Response
import com.abduqodirov.guitaronlineshop.data.model.UserDTO
import com.abduqodirov.guitaronlineshop.databinding.FragmentProfileBinding
import javax.inject.Inject

class ProfileFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<ProfileViewModel> { viewModelFactory }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    // TODO: 8/2/2021 persist user token. And refresh token
    // TODO: 8/2/2021 Get user data by token and id and display in UI

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // TODO check token here and navigate if necessary
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()

        observeData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeData() {
        viewModel.profileData.observe(
            viewLifecycleOwner,
            Observer {
                it.let { response ->

                    when (response) {

                        is Response.Loading -> {
                            switchUItoLoadingState()
                        }

                        is Response.Success -> {
                            switchUItoSuccessState()
                            populateViewsWithSuccessfullyFetchedData(
                                response.data.userDTO
                            )
                        }

                        is Response.Failure -> {
                            // TODO: 8/6/2021 navigate based on error 
                        }
                    }
                }
            }
        )
    }

    private fun populateViewsWithSuccessfullyFetchedData(user: UserDTO) {
        binding.run {
            profileFirstNameTxt.text = user.firstName
            profileLastNameTxt.text = user.firstName
            profileEmailTxt.text = user.email
            // TODO: 8/6/2021 Load images with own image loader repo
        }
    }

    private fun setupClickListeners() {
        binding.run {

            profileCreateProductBtn.setOnClickListener {
                navigateToSubmitProductScreen()
            }
            // tempSignInBtn.setOnClickListener {
            //     findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToAuthGraph())
            // }
        }
    }

    private fun switchUItoLoadingState() {
        binding.run {
            profileProgressBar.show()
            profileDataGroup.isVisible = false
        }
    }

    private fun switchUItoSuccessState() {
        binding.run {
            profileDataGroup.isVisible = true
            profileProgressBar.hide()
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
