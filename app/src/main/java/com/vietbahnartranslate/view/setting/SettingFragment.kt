package com.vietbahnartranslate.view.setting

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso
import com.vietbahnartranslate.R
import com.vietbahnartranslate.utils.DataUtils
import kotlinx.coroutines.DelicateCoroutinesApi

class SettingFragment : Fragment() {
    private val TAG = "SettingFragment"

    private lateinit var gender: TextView
    private lateinit var speed: TextView
    private lateinit var backupRestore: TextView
    private lateinit var signIn: TextView

    private lateinit var signInImageView: ImageView

    private var bottomNavigationView: BottomNavigationView? = null

    @SuppressLint("SetTextI18n")
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        gender = view.findViewById(R.id.setting_gender_textView)
        speed = view.findViewById(R.id.setting_speed_textView)
        backupRestore = view.findViewById(R.id.setting_backup_restore_textView)
        signIn = view.findViewById(R.id.sign_in_textView)
        signInImageView = view.findViewById(R.id.sign_in_imageView)

        bottomNavigationView = activity?.findViewById(R.id.bottom_navigation_view)
        if (bottomNavigationView?.visibility == View.GONE) {
            bottomNavigationView?.visibility = View.VISIBLE
        }

        if (DataUtils.isSignedIn) {
            signIn.text = "Xin chào bạn, ${DataUtils.displayName}"
            Log.d(TAG, "photoURL is ${DataUtils.photoURL}")
            Picasso.with(requireContext()).load(DataUtils.photoURL).into(signInImageView)
        }

        gender.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Giọng đọc phát âm")
                .setItems(arrayOf("Nam", "Nữ")) { dialog, which ->
                    when (which) {
                        0 -> {
                            DataUtils.gender = false
                        }
                        1 -> {
                            DataUtils.gender = true
                        }
                    }
                }
                .create()
            dialog.show()
        }

        speed.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Tốc độ phát âm")
                .setItems(arrayOf("Chậm hơn", "Bình thường", "Nhanh hơn")) { dialog, which ->
                    when (which) {
                        0 -> {
                            DataUtils.speed = 0.5f
                        }
                        1 -> {
                            DataUtils.speed = 1.0f
                        }
                        2 -> {
                            DataUtils.speed = 1.5f
                        }
                    }
                }
                .create()
            dialog.show()
        }

        backupRestore.setOnClickListener {
            if (DataUtils.isSignedIn) {
                findNavController().navigate(R.id.action_setting_fragment_to_backupFragment)
            } else {
                Toast.makeText(requireContext(), "Bạn cần đăng nhập để sử dụng tính năng này", Toast.LENGTH_SHORT).show()
            }

        }

        signIn.setOnClickListener {
            findNavController().navigate(R.id.action_setting_fragment_to_signInFragment)
        }

        return view
    }
}