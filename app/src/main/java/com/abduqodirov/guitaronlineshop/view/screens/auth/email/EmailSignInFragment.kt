package com.abduqodirov.guitaronlineshop.view.screens.auth.email

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.abduqodirov.guitaronlineshop.data.model.Response
import com.abduqodirov.guitaronlineshop.data.model.TokenUserDTO
import com.abduqodirov.guitaronlineshop.databinding.FragmentEmailSignInBinding
import com.abduqodirov.guitaronlineshop.view.ShopApplication
import com.abduqodirov.guitaronlineshop.view.screens.auth.AuthViewModel
import timber.log.Timber
import javax.inject.Inject

class EmailSignInFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<AuthViewModel> { viewModelFactory }

    private var _binding: FragmentEmailSignInBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as ShopApplication).appComponent.authComponent().create()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmailSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()

        observeSignedInUser()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeSignedInUser() {
        viewModel.user.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Response.Loading -> switchUIToLoadingState()
                    is Response.Success -> onUserSignedInSuccessfully(it)
                    is Response.Failure -> showErrors(it)
                }
            }
        )
    }

    private fun setupClickListeners() {
        binding.run {
            submitBtn.setOnClickListener {
                // TODO validate fields and show message.
                val email = emailInput.text.toString()
                val password = passwordInput.text.toString()

                viewModel.signIn(email, password)
            }
        }

        binding.forgetPasswordBtn.setOnClickListener {
            navigateToForgetScreen()
        }

        binding.signUpBtn.setOnClickListener {
            navigateToSignUpScreen()
        }
    }

    private fun onUserSignedInSuccessfully(success: Response.Success<TokenUserDTO>) {
        Timber.d("kirdi muvafaqqiyatli ${success.data}")
    }

    private fun switchUIToLoadingState() {
        Timber.d("loading bo'lyapti")
    }

    private fun showErrors(failure: Response.Failure) {
        Timber.d("error keldi ${failure.errorMessage}")
    }

    private fun navigateToSignUpScreen() {
        findNavController().navigate(
            EmailSignInFragmentDirections.actionEmailSignInFragmentToSignUpFragment()
        )
    }

    private fun navigateToForgetScreen() {
        findNavController().navigate(
            EmailSignInFragmentDirections.actionEmailSignInFragmentToForgetPasswordFragment()
        )
    }
}
