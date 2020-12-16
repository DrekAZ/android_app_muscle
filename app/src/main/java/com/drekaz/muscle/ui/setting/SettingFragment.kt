package com.drekaz.muscle.ui.setting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.drekaz.muscle.R
import com.drekaz.muscle.database.BodyInfoDatabase
import com.drekaz.muscle.database.UserDatabase
import com.drekaz.muscle.database.entity.UserEntity
import com.drekaz.muscle.databinding.FragmentSettingBinding
import com.drekaz.muscle.ui.dialog.DialogChangeBodyInfo
import com.drekaz.muscle.ui.dialog.DialogChangeRadioButton
import com.drekaz.muscle.ui.dialog.DialogChangeText
import kotlinx.coroutines.runBlocking

class SettingFragment : Fragment() {
    private lateinit var settingViewModel: SettingViewModel
    private lateinit var userDatabase: UserDatabase
    private lateinit var bodyInfoDatabase: BodyInfoDatabase
    private lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        settingViewModel = ViewModelProvider.NewInstanceFactory().create(SettingViewModel::class.java)
        userDatabase = UserDatabase.getInstance(requireContext())
        bodyInfoDatabase = BodyInfoDatabase.getInstance(requireContext())

        binding = FragmentSettingBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = settingViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        runBlocking {
            settingViewModel.readUserData(userDatabase)
        }

        val itemName = view.findViewById<TextView>(R.id.item_name)
        val itemSex = view.findViewById<TextView>(R.id.item_sex)
        val itemChangeBody = view.findViewById<TextView>(R.id.item_chg_body)

        itemName.setOnClickListener { v ->
            val dialogChangeText = DialogChangeText(settingViewModel.userData.value!!.name).apply {
                setTargetFragment(this@SettingFragment, 1)
                arguments = Bundle()
            }
            dialogChangeText.show(parentFragmentManager, null)
        }
        itemSex.setOnClickListener { v ->
            val dialogChangeRadioButton = DialogChangeRadioButton(settingViewModel.userData.value!!.sex).apply {
                setTargetFragment(this@SettingFragment, 2)
                arguments = Bundle()
            }
            dialogChangeRadioButton.show(parentFragmentManager, null)
        }
        itemChangeBody.setOnClickListener { v ->
            val dialogChangeBodyInfo = DialogChangeBodyInfo().apply {
                setTargetFragment(this@SettingFragment, 3)
                arguments = Bundle()
            }
            dialogChangeBodyInfo.show(parentFragmentManager, null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            1 -> if(resultCode == Activity.RESULT_OK) {
                val replaceText = data!!.getStringExtra("text")
                val updateData = UserEntity(0, replaceText!!, settingViewModel.userData.value!!.sex)
                settingViewModel.updateUserData(userDatabase, updateData)
            }
            2 -> if(resultCode == Activity.RESULT_OK) {
                val replaceRadio = data!!.getIntExtra("radio", 9)
                println(replaceRadio)
                val updateData = UserEntity(0, settingViewModel.userData.value!!.name, replaceRadio)
                settingViewModel.updateUserData(userDatabase, updateData)
            }
            3 -> if(resultCode == Activity.RESULT_OK) {
                val d = data!!.getStringArrayExtra("BodyInfo")
                settingViewModel.updateBodyInfo(bodyInfoDatabase, d!!)
            }
        }
    }

}