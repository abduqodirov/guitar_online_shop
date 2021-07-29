package com.abduqodirov.guitaronlineshop.view.screens.auth.email

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.abduqodirov.guitaronlineshop.databinding.FragmentEmailSignInBinding

class EmailSignInFragment : Fragment() {

    private var _binding: FragmentEmailSignInBinding? = null
    private val binding get() = _binding!!

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupClickListeners() {
        binding.run {
            submitBtn.setOnClickListener {
                val email = emailInput.text.toString()
                val password = passwordInput.text.toString()
                // TODO validate fields and show message.
            }
        }

        binding.forgetPasswordBtn.setOnClickListener {
            navigateToForgetScreen()
        }

        binding.signUpBtn.setOnClickListener {
            navigateToSignUpScreen()
        }
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
