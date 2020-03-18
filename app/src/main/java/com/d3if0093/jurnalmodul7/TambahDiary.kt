package com.d3if0093.jurnalmodul7

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.d3if0093.jurnalmodul7.databinding.FragmentTambahDiaryBinding

/**
 * A simple [Fragment] subclass.
 */
class TambahDiary : Fragment() {
private lateinit var binding:FragmentTambahDiaryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         binding=DataBindingUtil.inflate(inflater, R.layout.fragment_tambah_diary, container, false)
        val application = requireNotNull(this.activity).application

        val dataSource = DiaryDatabase.getInstance(application).DiaryDao

        val viewModelFactory = DiaryViewModelFactory(dataSource, application)

        val diaryViewModel = ViewModelProviders.of(this,viewModelFactory).get(DiaryViewModel::class.java)

        binding.tambah.setOnClickListener {
            if(binding.catat.text.isEmpty()){
                Toast.makeText(activity, "Text Field Wajin Diisi", Toast.LENGTH_LONG).show()
            }
            else {
                diaryViewModel.onClickTambah(binding.catat.text.toString())
                view?.findNavController()?.navigate(R.id.action_tambahDiary_to_tampilDiary)
            }
        }









        return binding.root
    }

}
