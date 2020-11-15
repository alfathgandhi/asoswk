package org.d3ifcool.quiz.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.quiz.R


class HomeAdapter(var context: Context):RecyclerView.Adapter<HomeAdapter.ItemHolder>(){
    var data = listOf<Int>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
       val itemHolder= LayoutInflater.from(parent.context).
       inflate(R.layout.grid_layout,parent,false)
        return ItemHolder(itemHolder)
    }

    override fun getItemCount(): Int {
        return data.size
    }



    inner class ItemHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        var stage_tv = itemView.findViewById<TextView>(R.id.tv_stage_grid)
        var nilai_tv = itemView.findViewById<TextView>(R.id.tv_score_grid)
        var card = itemView.findViewById<CardView>(R.id.cardnya)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val stage :String=(position+1).toString()
        val score = data.get(position)

        if(data[position]!=null){
            holder.stage_tv.text="Stage ${stage}"
            holder.nilai_tv.text=score.toString()


        }else{


            holder.card.visibility=View.GONE

        }



    }
}