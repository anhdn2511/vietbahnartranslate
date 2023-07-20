package com.vietbahnartranslate.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.vietbahnartranslate.R
import com.vietbahnartranslate.utils.KeyboardUtils
import com.vietbahnartranslate.view.MainActivity
import com.vietbahnartranslate.viewmodel.update.UpdateViewModel


class MyKeyboard : Fragment() {
    private val TAG = "KeyboardFragment"

    private lateinit var mButtona: Button
    private lateinit var mButtonb: Button
    private lateinit var mButtonc: Button
    private lateinit var mButtond: Button
    private lateinit var mButtone: Button
    private lateinit var mButtonf: Button
    private lateinit var mButtong: Button
    private lateinit var mButtonh: Button
    private lateinit var mButtoni: Button
    private lateinit var mButtonj: Button
    private lateinit var mButtonk: Button
    private lateinit var mButtonl: Button
    private lateinit var mButtonm: Button
    private lateinit var mButtonn: Button
    private lateinit var mButtono: Button
    private lateinit var mButtonp: Button
    private lateinit var mButtonq: Button
    private lateinit var mButtonr: Button
    private lateinit var mButtons: Button
    private lateinit var mButtont: Button
    private lateinit var mButtonu: Button
    private lateinit var mButtonv: Button
    private lateinit var mButtonw: Button
    private lateinit var mButtonx: Button
    private lateinit var mButtony: Button
    private lateinit var mButtonz: Button
    private lateinit var mButtonɛ: Button
    private lateinit var mButtonɯ: Button
    private lateinit var mButtonɤ: Button
    private lateinit var mButtonɔ: Button
    private lateinit var mButtonĭ: Button
    private lateinit var mButtonɛ̆: Button
    private lateinit var mButtonɘ: Button
    private lateinit var mButtonɘ̆: Button
    private lateinit var mButtonɔ̆: Button
    private lateinit var mButtonɤ̆: Button
    private lateinit var mButtonă: Button
    private lateinit var mButtonŭ: Button
    private lateinit var mButtonDelete: Button
    private lateinit var mButtonEnter: Button

    var keyValues = SparseArray<String>()
    var inputConnection: InputConnection? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView()")

        /* variables used to contain input from user */
        val view = inflater.inflate(R.layout.keyboard, container, false)

        mButtona = view.findViewById(R.id.button_a)
        mButtonb = view.findViewById(R.id.button_b)
        mButtonc = view.findViewById(R.id.button_c)
        mButtond = view.findViewById(R.id.button_d)
        mButtone = view.findViewById(R.id.button_e)
        mButtonf = view.findViewById(R.id.button_f)
        mButtong = view.findViewById(R.id.button_g)
        mButtonh = view.findViewById(R.id.button_h)
        mButtoni = view.findViewById(R.id.button_i)
        mButtonj = view.findViewById(R.id.button_j)
        mButtonk = view.findViewById(R.id.button_k)
        mButtonl = view.findViewById(R.id.button_l)
        mButtonm = view.findViewById(R.id.button_m)
        mButtonn = view.findViewById(R.id.button_n)
        mButtono = view.findViewById(R.id.button_o)
        mButtonp = view.findViewById(R.id.button_p)
        mButtonq = view.findViewById(R.id.button_q)
        mButtonr = view.findViewById(R.id.button_r)
        mButtons = view.findViewById(R.id.button_s)
        mButtont = view.findViewById(R.id.button_t)
        mButtonu = view.findViewById(R.id.button_u)
        mButtonv = view.findViewById(R.id.button_v)
        mButtonw = view.findViewById(R.id.button_w)
        mButtonx = view.findViewById(R.id.button_x)
        mButtony = view.findViewById(R.id.button_y)
        mButtonz = view.findViewById(R.id.button_z)
        mButtonɛ = view.findViewById(R.id.button_ɛ)
        mButtonɯ = view.findViewById(R.id.button_ɯ)
        mButtonɤ = view.findViewById(R.id.button_ɤ)
        mButtonɔ = view.findViewById(R.id.button_ɔ)
        mButtonĭ = view.findViewById(R.id.button_ĭ)
        mButtonɛ̆ = view.findViewById(R.id.button_ɛ̆)
        mButtonɘ = view.findViewById(R.id.button_ɘ)
        mButtonɘ̆ = view.findViewById(R.id.button_ɘ̆)
        mButtonɔ̆ = view.findViewById(R.id.button_ɔ̆)
        mButtonɤ̆ = view.findViewById(R.id.button_ɤ̆)
        mButtonă = view.findViewById(R.id.button_ă)
        mButtonŭ = view.findViewById(R.id.button_ŭ)
        mButtonDelete = view.findViewById(R.id.button_delete)
        mButtonEnter = view.findViewById(R.id.button_enter)

        // set button click listeners
        mButtona.setOnClickListener(this)
        mButtonb.setOnClickListener(this)
        mButtonc.setOnClickListener(this)
        mButtond.setOnClickListener(this)
        mButtone.setOnClickListener(this)
        mButtonf.setOnClickListener(this)
        mButtong.setOnClickListener(this)
        mButtonh.setOnClickListener(this)
        mButtoni.setOnClickListener(this)
        mButtonj.setOnClickListener(this)
        mButtonk.setOnClickListener(this)
        mButtonl.setOnClickListener(this)
        mButtonm.setOnClickListener(this)
        mButtonn.setOnClickListener(this)
        mButtono.setOnClickListener(this)
        mButtonp.setOnClickListener(this)
        mButtonq.setOnClickListener(this)
        mButtonr.setOnClickListener(this)
        mButtons.setOnClickListener(this)
        mButtont.setOnClickListener(this)
        mButtonu.setOnClickListener(this)
        mButtonv.setOnClickListener(this)
        mButtonw.setOnClickListener(this)
        mButtonx.setOnClickListener(this)
        mButtony.setOnClickListener(this)
        mButtonz.setOnClickListener(this)
        mButtonɛ.setOnClickListener(this)
        mButtonɯ.setOnClickListener(this)
        mButtonɤ.setOnClickListener(this)
        mButtonɔ.setOnClickListener(this)
        mButtonĭ.setOnClickListener(this)
        mButtonɛ̆.setOnClickListener(this)
        mButtonɘ.setOnClickListener(this)
        mButtonɘ̆.setOnClickListener(this)
        mButtonɔ̆.setOnClickListener(this)
        mButtonɤ̆.setOnClickListener(this)
        mButtonDelete.setOnClickListener(this)
        mButtonEnter.setOnClickListener(this)

        // map buttons IDs to input strings
        keyValues.put(R.id.button_a, "a")
        keyValues.put(R.id.button_b, "b")
        keyValues.put(R.id.button_c, "c")
        keyValues.put(R.id.button_d, "d")
        keyValues.put(R.id.button_e, "e")
        keyValues.put(R.id.button_f, "f")
        keyValues.put(R.id.button_g, "g")
        keyValues.put(R.id.button_h, "h")
        keyValues.put(R.id.button_i, "i")
        keyValues.put(R.id.button_j, "j")
        keyValues.put(R.id.button_k, "k")
        keyValues.put(R.id.button_l, "l")
        keyValues.put(R.id.button_m, "m")
        keyValues.put(R.id.button_n, "o")
        keyValues.put(R.id.button_p, "p")
        keyValues.put(R.id.button_q, "q")
        keyValues.put(R.id.button_r, "r")
        keyValues.put(R.id.button_s, "s")
        keyValues.put(R.id.button_t, "t")
        keyValues.put(R.id.button_u, "u")
        keyValues.put(R.id.button_v, "v")
        keyValues.put(R.id.button_w, "w")
        keyValues.put(R.id.button_x, "x")
        keyValues.put(R.id.button_y, "y")
        keyValues.put(R.id.button_z, "z")
        keyValues.put(R.id.button_ɛ, "ɛ")
        keyValues.put(R.id.button_ɯ, "ɯ")
        keyValues.put(R.id.button_ɤ, "ɤ")
        keyValues.put(R.id.button_ɔ, "ɔ")
        keyValues.put(R.id.button_ĭ, "ĭ")
        keyValues.put(R.id.button_ɛ̆, "ɛ̆")
        keyValues.put(R.id.button_ɘ, "ɘ")
        keyValues.put(R.id.button_ɘ̆, "ɘ̆")
        keyValues.put(R.id.button_ɔ̆, "ɔ̆")
        keyValues.put(R.id.button_ɤ̆, "ɤ̆")
        keyValues.put(R.id.button_enter, "\n")
    }

    fun onClick(v: View) {

        // do nothing if the InputConnection has not been set yet
        if (inputConnection == null) return

        // Delete text or input key value
        // All communication goes through the InputConnection
        if (v.getId() === R.id.button_delete) {
            val selectedText = inputConnection!!.getSelectedText(0)
            if (TextUtils.isEmpty(selectedText)) {
                // no selection, so delete previous character
                inputConnection!!.deleteSurroundingText(1, 0)
            } else {
                // delete the selection
                inputConnection!!.commitText("", 1)
            }
        } else {
            val value = keyValues[v.getId()]
            inputConnection!!.commitText(value, 1)
        }
    }

    // The activity (or some parent or controller) must give us
    // a reference to the current EditText's InputConnection
    fun setInputConnection(ic: InputConnection?) {
        inputConnection = ic
    }
}