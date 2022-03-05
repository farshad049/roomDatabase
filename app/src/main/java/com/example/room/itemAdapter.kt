package com.example.room

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.room.databinding.ItemTableBinding

class itemAdapter(private val items:ArrayList<EmployeeEntity>,
                  private val updateListener:(id:Int)->Unit,
                  private val deleteListener:(id:Int)->Unit
       ):RecyclerView.Adapter<itemAdapter.viewHolder>(){

    class viewHolder(binding:ItemTableBinding):RecyclerView.ViewHolder(binding.root){
        val llMain=binding.liItem
        val tvName=binding.tvNameItem
        val tvEmail=binding.tvEmailItem
        val ivEdit=binding.ivEdit
        val ivDelete=binding.ivDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(ItemTableBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val context= holder.itemView.context
        val item=items[position]

        holder.tvName.text=item.name
        holder.tvEmail.text=item.email
       // holder.ivEdit

        if (position % 2==0){
            holder.llMain.setBackgroundColor(ContextCompat.getColor(holder.itemView.context,R.color.lightGray))
        }else{
            holder.llMain.setBackgroundColor(ContextCompat.getColor(holder.itemView.context,R.color.white))
        }

        holder.ivEdit.setOnClickListener {
            updateListener.invoke(item.id)
        }
        holder.ivDelete.setOnClickListener {
            deleteListener.invoke(item.id)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}