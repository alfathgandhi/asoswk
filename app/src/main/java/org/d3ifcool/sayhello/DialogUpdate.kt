//package org.d3ifcool.sayhello
//
//import android.app.AlertDialog
//import android.app.Dialog
//import android.os.Bundle
//import android.util.Log
//import android.view.Gravity
//import android.view.LayoutInflater
//import android.view.View
//import android.widget.EditText
//import android.widget.Toast
//import androidx.fragment.app.DialogFragment
//import kotlinx.android.synthetic.main.dialog_update.view.*
//import org.d3ifcool.sayhello.data.Mahasiswa
//
//class DialogUpdate(val mahasiswa: Mahasiswa) : DialogFragment() {
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val inflater = LayoutInflater.from(requireContext())
//
//        val view = inflater.inflate(R.layout.dialog_update,null,false)
//
//        val builder = AlertDialog.Builder(requireContext())
//
//        with(builder) {
//            setData(view, mahasiswa)
//            setTitle(R.string.update_mhs)
//            setView(view)
//            setPositiveButton(R.string.update) { _, _ ->
//                val mahasiswa = getDataUpdate(view) ?: return@setPositiveButton
//                val listener = requireActivity() as DialogListener
//                listener.processUpdateDialog(mahasiswa)
//            }
//            setNegativeButton(R.string.batal) { _, _ -> dismiss() }
//        }
//        return builder.create()
//    }
//    interface DialogListener {
//        fun processUpdateDialog(mahasiswa: Mahasiswa)
//    }
//
//    private fun setData(view: View, mahasiswa: Mahasiswa){
//        val nimUpdate = view.nimUpdate
//        val namaUpdate = view.namaUpdate
//
//        nimUpdate.setText(mahasiswa.nim)
//        namaUpdate.setText(mahasiswa.nama)
//    }
//
//    private fun getDataUpdate(view: View): Mahasiswa? {
//        val nimUpdate = view.findViewById<EditText>(R.id.nimUpdate)
//        val namaUpdate = view.findViewById<EditText>(R.id.namaUpdate)
//
//        if (nimUpdate.text.isEmpty()){
//            showMessage(R.string.nim_wajib)
//            return null
//        }
//        if (nimUpdate.text.length != 10){
//            showMessage(R.string.nim_10chars)
//        }
//        if (namaUpdate.text.isEmpty()){
//            showMessage(R.string.nama_wajib)
//        }
//        return Mahasiswa(
//            id = mahasiswa.id,
//            nim = nimUpdate.text.toString(),
//            nama = namaUpdate.text.toString()
//        )
//    }
//
//    private fun showMessage(messageResId : Int){
//        val toast = Toast.makeText(requireContext(), messageResId, Toast.LENGTH_LONG)
//        toast.setGravity(Gravity.CENTER, 0,0)
//        toast.show()
//    }
//}