package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.databinding.SignInFragmentBinding
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewmodel.SignInViewModel

class SignInFragment : Fragment() {

    private val signInViewModel: SignInViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    lateinit var appAuth: AppAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = SignInFragmentBinding.inflate(
            inflater,
            container,
            false
        )

        signInViewModel.dataState.observe(viewLifecycleOwner, { state ->
            with(binding) {
                if(state.loading) {
                    signInProgressLoading.visibility = View.VISIBLE
                }
                if (state.authState) {
                    findNavController().navigate(R.id.feedFragment)
                }
                if (state.error) {
                    signInError.visibility = View.VISIBLE
                    ifErrorGone.visibility = View.GONE
                    retryButton.setOnClickListener {
                        signInError.visibility = View.GONE
                        ifErrorGone.visibility = View.VISIBLE
                    }
                }
            }
        })

        with(binding) {
            signInButton.setOnClickListener {
                if (usernameInput.text?.trim().toString().isBlank() || passwordInput.text?.trim().toString().isBlank()) {
                    Toast.makeText(context, "Введите логин и пароль", Toast.LENGTH_SHORT).show()
                } else {
                    signInViewModel.signIn(usernameInput.text?.trim().toString(), passwordInput.text?.trim().toString())
                }
            }
            cancelButton.setOnClickListener {
                findNavController().navigate(R.id.feedFragment)
            }

        }

        return binding.root
    }


}