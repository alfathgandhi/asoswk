package org.d3ifcool.sayhello.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_main.*
import org.d3ifcool.sayhello.R
import org.d3ifcool.sayhello.data.Mahasiswa
import org.d3ifcool.sayhello.data.MahasiswaDb

class MainFragment : Fragment() {

    private var actionMode: ActionMode? = null
    private lateinit var adapter: MainAdapter

    private val actionModeCallback = object : ActionMode.Callback {
        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            if (item?.itemId == R.id.menu_delete) {
                deleteData()
                return true
            }
            if (item?.itemId == R.id.menu_edit){

                return true
            }
            return false
        }

        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode?.menuInflater?.inflate(R.menu.main_mode, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode?.title = adapter.getSelection().size.toString()
            return true
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            actionMode = null
            adapter.resetSelection()
        }
    }

    private val viewModel: MainViewModel by lazy {
        val dataSource = MahasiswaDb.getInstance(requireContext()).dao
        val factory =
            MainViewModelFactory(dataSource)
        ViewModelProvider(this, factory).get(MainViewModel::class.java)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_main, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = MainFragmentArgs.fromBundle(requireArguments())
        fab.setOnClickListener {
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToMainDialog(args.kelas)
            )
        }
        val adapter = MainAdapter(handler)
        val itemDecor = DividerItemDecoration(context, RecyclerView.VERTICAL)
        recyclerView.addItemDecoration(itemDecor)
        recyclerView.adapter = adapter
        viewModel.getData(args.kelas).observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            emptyView.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        })
    }

    private val handler = object : MainAdapter.ClickHandler{
        override fun onLongClick(position: Int): Boolean {
            if (actionMode != null) return false
            val target = activity as AppCompatActivity
            adapter.toggleSelection(position)
            actionMode = target.startSupportActionMode(actionModeCallback)
            return true
        }

        override fun onClick(position: Int, mahasiswa: Mahasiswa){
            if (actionMode != null){
                adapter.toggleSelection(position)
                if (adapter.getSelection().isEmpty())
                    actionMode?.finish()
                else
                    actionMode?.invalidate()
                return
            }
            val message = getString(R.string.mahasiswa_klik, mahasiswa.nama)
            Toast.makeText(context, message,Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteData() {
        val builder = AlertDialog.Builder(requireContext())
            .setMessage(R.string.pesan_hapus)
            .setPositiveButton(R.string.hapus){ _, _ ->
                viewModel.deleteData(adapter.getSelection())
                actionMode?.finish()
            }
            .setNegativeButton(R.string.batal){ dialog, _ ->
                dialog.cancel()
                actionMode?.finish()
            }
        builder.show()
    }

    override fun onDestroy() {
        actionMode?.finish()
        super.onDestroy()
    }

}
