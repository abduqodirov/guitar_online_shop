package com.abduqodirov.guitaronlineshop.view.screens.auth.email

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.abduqodirov.guitaronlineshop.R
import com.abduqodirov.guitaronlineshop.data.model.Response
import com.abduqodirov.guitaronlineshop.data.model.TokenUserDTO
import com.abduqodirov.guitaronlineshop.databinding.FragmentEmailSignInBinding
import com.abduqodirov.guitaronlineshop.view.ShopApplication
import com.abduqodirov.guitaronlineshop.view.model.Validation
import com.abduqodirov.guitaronlineshop.view.util.setErrorTextResId
import javax.inject.Inject

const val EDITTEXT_SIGN_IN_EMAIL_POSITION = 0
const val EDITTEXT_SIGN_IN_PASSWORD_POSITION = 1

class EmailSignInFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<SignInViewModel> { viewModelFactory }

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

        setupFormValidators()

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
                    is Response.Success -> onUserSignedInSuccessfully(it.data)
                    is Response.Failure -> showErrors(it)
                }
            }
        )
    }

    private fun setupClickListeners() {
        binding.run {
            submitBtn.setOnClickListener {

                viewModel.validateEditText(
                    EDITTEXT_SIGN_IN_EMAIL_POSITION,
                    emailInput.text.toString()
                )
                viewModel.validateEditText(
                    EDITTEXT_SIGN_IN_PASSWORD_POSITION,
                    passwordInput.text.toString()
                )

                viewModel.formValidations.value?.let { validationArray ->
                    val isAllFieldsValid = manageButtonVisibilityWithValidation(validationArray)
                    if (!isAllFieldsValid) {
                        return@setOnClickListener
                    }
                }

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

    private fun setupFormValidators() {
        binding.run {
            viewModel.formValidations.observe(
                viewLifecycleOwner,
                Observer {
                    manageButtonVisibilityWithValidation(it)
                    textlayoutEmail.setErrorTextResId(it[EDITTEXT_SIGN_IN_EMAIL_POSITION])
                    textlayoutPassword.setErrorTextResId(it[EDITTEXT_SIGN_IN_PASSWORD_POSITION])
                }
            )

            emailInput.addTextChangedListener { emailText ->
                viewModel.validateEditText(
                    EDITTEXT_SIGN_IN_EMAIL_POSITION,
                    emailText.toString()
                )
            }

            passwordInput.addTextChangedListener { passwordText ->
                viewModel.validateEditText(
                    EDITTEXT_SIGN_IN_PASSWORD_POSITION,
                    passwordText.toString()
                )
            }
        }
    }

    // TODO this one also repetitive
    private fun manageButtonVisibilityWithValidation(
        it: Array<Validation>
    ): Boolean {
        var isAllFieldsValid = true
        for (validation in it) {
            if (validation.errorResId != null) {
                isAllFieldsValid = false
                break
            }
        }
        binding.submitBtn.isEnabled = isAllFieldsValid
        return isAllFieldsValid
    }

    private fun onUserSignedInSuccessfully(signedInUser: TokenUserDTO) {
        viewModel.saveToken(signedInUser.accessToken)
        findNavController().popBackStack()
        // TODO: 8/2/2021 pop back stack up to profile.
    }

    private fun switchUIToLoadingState() {
        binding.run {
            submitBtn.isVisible = false
            messageTxt.isVisible = false
            progressBar.show()
        }
    }

    private fun showErrors(failure: Response.Failure) {
        binding.run {
            messageTxt.isVisible = true
            submitBtn.isVisible = true
            progressBar.hide()
            submitBtn.text = getString(R.string.retry)

            // TODO boshqa joylarda ham shunaqa failure errormessageni nnullablega yozib chiqish
            messageTxt.text = failure.errorMessage ?: getString(R.string.failed_to_sign_in)
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
