package ru.netology.nmedia.activity

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.SignInFragmentBinding
import ru.netology.nmedia.viewmodel.SignInViewModel

@AndroidEntryPoint
class SignInFragment: DialogFragment() {

    private val signInViewModel: SignInViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    var bindingSI: SignInFragmentBinding? = null

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
        bindingSI = binding

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater;

            builder.setView(inflater.inflate(R.layout.sign_in_fragment, null))
                .setPositiveButton(R.string.sign_in,
                    DialogInterface.OnClickListener { dialog, id ->
                        val login = bindingSI?.username?.text?.trim().toString()
                        val password = bindingSI?.password?.text?.trim().toString()
                        signInViewModel.signeIn(login, password)
                    })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}