package com.vietbahnartranslate.view.setting

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vietbahnartranslate.R
import com.vietbahnartranslate.viewmodel.setting.BackupRestoreViewModel

class BackupRestoreFragment : Fragment() {
    private val TAG = "BackupFragment"

    private val backupRestoreViewModel: BackupRestoreViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[BackupRestoreViewModel::class.java]
    }

    private lateinit var backupButton: Button
    private lateinit var restoreButton: Button
    private lateinit var timestampTextView: TextView
    private lateinit var deviceTextView: TextView

    private var bottomNavigationView : BottomNavigationView? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        backupRestoreViewModel.init()
        bottomNavigationView = activity?.findViewById(R.id.bottom_navigation_view)
        bottomNavigationView?.visibility = View.GONE

        val view = inflater.inflate(R.layout.fragment_backup_restore, container, false)
        backupButton = view.findViewById(R.id.backup_button)
        restoreButton = view.findViewById(R.id.restore_button)
        timestampTextView = view.findViewById(R.id.timestamp_backup_textView)
        deviceTextView = view.findViewById(R.id.device_backup_textView)

        backupButton.setOnClickListener{
            Log.d(TAG, "onBackupButtonClick")
            backupRestoreViewModel.onBackupButtonClick()
        }

        restoreButton.setOnClickListener {
            Log.d(TAG, "onRestoreButtonClick")
            backupRestoreViewModel.onRestoreButtonClick()
        }

        
        deviceTextView.text = "Thiết bị: ${Build.MANUFACTURER} ${Build.MODEL}"

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bottomNavigationView?.visibility = View.VISIBLE
    }
}