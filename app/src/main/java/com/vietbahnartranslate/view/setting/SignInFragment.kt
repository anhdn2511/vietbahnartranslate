package com.vietbahnartranslate.view.setting

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseUser
import com.vietbahnartranslate.R
import com.vietbahnartranslate.utils.DataUtils
import com.vietbahnartranslate.viewmodel.setting.SignInViewModel

class SignInFragment : Fragment() {
    private val TAG = "SignInFragment"

    private val signInViewModel: SignInViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[SignInViewModel::class.java]
    }

    private val signInResultLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        val data: Intent? = result.data
        signInViewModel.signInWithGoogleToken(data)
    }

    private var bottomNavigationView: BottomNavigationView? = null
    private lateinit var signInButton: Button
    private lateinit var signInTextview: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        signInViewModel.userLiveData.observe(viewLifecycleOwner, this::onUserChanged)

        // Inflate the layout for this fragment
        bottomNavigationView = activity?.findViewById(R.id.bottom_navigation_view)
        bottomNavigationView?.visibility = View.GONE

        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)
        signInButton = view.findViewById(R.id.sign_in_fragment_button)
        signInTextview = view.findViewById(R.id.sign_in_fragment_textView)

        signInButton.setOnClickListener {
            signInViewModel.signIn(signInResultLauncher, getString(R.string.web_client_id))
        }
        return view
    }

    @SuppressLint("SetTextI18n")
    private fun onUserChanged(user: FirebaseUser?) {
        DataUtils.isSignedIn = (user != null)
        signInTextview.text = """
            name: ${user?.displayName}
            email: ${user?.email}
            photoURL: ${user?.photoUrl}
        """.trimIndent()
        DataUtils.displayName = user?.displayName ?: ""
        DataUtils.email = user?.email ?: ""
        DataUtils.photoURL = user?.photoUrl ?: Uri.EMPTY

        Log.d(TAG, "photoURL is ${DataUtils.photoURL}")
    }
}