package com.d3if0093.jurnalmodul7

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.d3if0093.jurnalmodul7.databinding.FragmentTampilDiaryBinding
import kotlin.math.log

/**
 * A simple [Fragment] subclass.
 */
class TampilDiary : Fragment() {
private lateinit var binding: FragmentTampilDiaryBinding
    private  lateinit var diaryViewModel: DiaryViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         binding=DataBindingUtil.inflate(inflater,R.layout.fragment_tampil_diary, container, false)

    setHasOptionsMenu(true)

       val application = requireNotNull(this.activity).application

  val dataSource = DiaryDatabase.getInstance(application).DiaryDao

       val viewModelFactory = DiaryViewModelFactory(dataSource, application)

        diaryViewModel = ViewModelProviders.of(this,viewModelFactory).get(DiaryViewModel::class.java)

        binding.diaryX = diaryViewModel
        binding.setLifecycleOwner(this)

        diaryViewModel.diarys.observe(viewLifecycleOwner, Observer {
             hasil -> hasil.forEach {
            Log.i("hasill","${it.diary}")

        }
        })

        binding.pindah.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_tampilDiary_to_tambahDiary)
        }


        return binding.root

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu,menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
when(item.itemId){
    R.id.hapus->{
        diaryViewModel.onClickHapus()
        return true
    }
}

        return NavigationUI.onNavDestinationSelected(item!!,view!!.findNavController()) || super.onOptionsItemSelected(item)
    }

}
