package com.drekaz.muscle.ui.first_launch

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import com.drekaz.muscle.MainActivity
import com.drekaz.muscle.R
import com.drekaz.muscle.database.UserDatabase
import com.drekaz.muscle.databinding.ActivityFirstLaunchBinding

class FirstLaunchActivity : AppCompatActivity() {

    private val viewModel = FirstLaunchViewModel()
    private lateinit var database: UserDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityFirstLaunchBinding>(this, R.layout.activity_first_launch)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        database = UserDatabase.getInstance(this)

        binding.inputName.doAfterTextChanged {
            viewModel.name.value = it!!.toString()
            viewModel.checkAll()
        }
        binding.inputHeight.doAfterTextChanged {
            viewModel.height.value = it!!.toString().toFloat()
            viewModel.checkAll()
        }
        binding.inputWeight.doAfterTextChanged {
            viewModel.weight.value = it!!.toString().toFloat()
            viewModel.checkAll()
        }
        binding.groupSex.setOnCheckedChangeListener { group, checkedId ->
            when(findViewById<RadioButton>(checkedId).text.toString()) {
                "男性" -> viewModel.sex.value = 1
                "女性" -> viewModel.sex.value = 2
                "その他" -> viewModel.sex.value = 9
            }
            viewModel.checkAll()
        }
        binding.inputFat.doAfterTextChanged {
            viewModel.fat.value = it!!.toString().toFloat()
            viewModel.checkAll()
        }

        /// FirstLaunchViewModelにてBind済み 全てが入力されるとEnabled
        binding.submit.setOnClickListener { v ->
            if(!viewModel.checkName()) {
                Toast.makeText(this, "ニックネームは15文字以内です", Toast.LENGTH_SHORT).show()
            } else if(!viewModel.checkHeight()) {
                Toast.makeText(this, "身長が未入力です", Toast.LENGTH_SHORT).show()
            } else if(!viewModel.checkWeight()) {
                Toast.makeText(this, "体重が未入力です", Toast.LENGTH_SHORT).show()
            } else if(!viewModel.checkSex()) {
                Toast.makeText(this, "性別が未入力です", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.saveData(database)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }



}