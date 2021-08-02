package com.abduqodirov.guitaronlineshop.view.screens.auth.signup

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
import com.abduqodirov.guitaronlineshop.databinding.FragmentSignUpBinding
import com.abduqodirov.guitaronlineshop.view.ShopApplication
import com.abduqodirov.guitaronlineshop.view.model.Validation
import com.abduqodirov.guitaronlineshop.view.util.setErrorTextResId
import javax.inject.Inject

const val EDITTEXT_SIGN_UP_EMAIL_POSITION = 0
const val EDITTEXT_SIGN_UP_PASSWORD_POSITION = 1
const val EDITTEXT_SIGN_UP_CONFIRM_PASSWORD_POSITION = 2

class SignUpFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<SignUpViewModel> { viewModelFactory }

    private var _binding: FragmentSignUpBinding? = null
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
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()

        setupFormValidators()

        observeSignedUpUser()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeSignedUpUser() {
        viewModel.user.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Response.Loading -> switchUIToLoadingState()
                    is Response.Success -> onUserSignedUpSuccessfully(it.data)
                    is Response.Failure -> showErrors(it)
                }
            }
        )
    }

    private fun setupFormValidators() {
        binding.run {
            viewModel.formValidations.observe(
                viewLifecycleOwner,
                Observer {
                    manageButtonVisibilityWithValidation(it)
                    textlayoutEmail.setErrorTextResId(it[EDITTEXT_SIGN_UP_EMAIL_POSITION])
                    textlayoutPassword.setErrorTextResId(it[EDITTEXT_SIGN_UP_PASSWORD_POSITION])
                    textlayoutConfirmPassword.setErrorTextResId(it[EDITTEXT_SIGN_UP_CONFIRM_PASSWORD_POSITION])
                }
            )

            emailInput.addTextChangedListener { emailText ->
                viewModel.validateEditText(EDITTEXT_SIGN_UP_EMAIL_POSITION, emailText.toString())
            }

            passwordInput.addTextChangedListener { passwordText ->
                viewModel.validatePassword(
                    position = EDITTEXT_SIGN_UP_PASSWORD_POSITION,
                    siblingPosition = EDITTEXT_SIGN_UP_CONFIRM_PASSWORD_POSITION,
                    password = passwordText.toString(),
                    siblingPassword = passwordConfirmInput.text.toString()
                )
            }

            passwordConfirmInput.addTextChangedListener { confirmPasswordText ->
                viewModel.validatePassword(
                    position = EDITTEXT_SIGN_UP_CONFIRM_PASSWORD_POSITION,
                    siblingPosition = EDITTEXT_SIGN_UP_PASSWORD_POSITION,
                    password = confirmPasswordText.toString(),
                    siblingPassword = passwordInput.text.toString()
                )
            }
        }
    }

    private fun setupClickListeners() {
        binding.run {

            submitBtn.setOnClickListener {
                viewModel.validateEditText(
                    EDITTEXT_SIGN_UP_EMAIL_POSITION,
                    emailInput.text.toString()
                )
                viewModel.validatePassword(
                    position = EDITTEXT_SIGN_UP_PASSWORD_POSITION,
                    siblingPosition = EDITTEXT_SIGN_UP_CONFIRM_PASSWORD_POSITION,
                    password = passwordInput.text.toString(),
                    siblingPassword = passwordConfirmInput.text.toString()
                )
                viewModel.validatePassword(
                    position = EDITTEXT_SIGN_UP_CONFIRM_PASSWORD_POSITION,
                    siblingPosition = EDITTEXT_SIGN_UP_PASSWORD_POSITION,
                    password = passwordInput.text.toString(),
                    siblingPassword = passwordConfirmInput.text.toString()
                )

                viewModel.formValidations.value?.let { arrayOfValidations ->
                    val isAllFieldsValid = manageButtonVisibilityWithValidation(arrayOfValidations)
                    if (!isAllFieldsValid) {
                        return@setOnClickListener
                    }
                }

                val email = emailInput.text.toString()
                val password = passwordInput.text.toString()

                viewModel.signUp(email, password)
            }

            signInNavigateBtn.setOnClickListener {
                navigateToSignInFragment()
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

    private fun onUserSignedUpSuccessfully(signedUpUser: TokenUserDTO) {
        findNavController().popBackStack(R.id.emailSignInFragment, true)
        // TODO: 8/2/2021 navigate up to pop inclusive sign in fragment
    }

    private fun switchUIToLoadingState() {
        binding.run {
            // TODO hamma joyda progresbarni o'zini methodini ishlatish
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

            messageTxt.text = failure.errorMessage ?: getString(R.string.failed_to_sign_up)
        }
    }

    private fun navigateToSignInFragment() {
        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToEmailSignInFragment())
    }
}
