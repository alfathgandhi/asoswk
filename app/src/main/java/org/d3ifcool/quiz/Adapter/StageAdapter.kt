package org.d3ifcool.quiz.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.quiz.R
import org.d3ifcool.quiz.obj.Stage

class StageAdapter(var clickListener:RecycleListClickListener ):RecyclerView.Adapter<StageAdapter.ViewHolder>()
{
    var data = listOf<Stage>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StageAdapter.ViewHolder {
    val layoutInt=LayoutInflater.from(parent.context)
        val view = layoutInt.inflate(R.layout.record_list,parent,false)
            return ViewHolder(view)

    }

    override fun getItemCount()= data.size


    override fun onBindViewHolder(holder: StageAdapter.ViewHolder, position: Int) {
        val int = position.toString()
     holder.initialize(data[position],clickListener,int)



     }
    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

        val soal:TextView = itemView.findViewById(R.id.tv_stage)


        fun initialize(item:Stage,action:RecycleListClickListener, index:String){
            soal.text=item.namaStage


            itemView.setOnClickListener {
                action.onClicked(adapterPosition,item)
            }
        }

    }


}

