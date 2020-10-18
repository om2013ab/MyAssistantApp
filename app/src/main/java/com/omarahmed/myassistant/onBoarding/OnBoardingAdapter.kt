package com.omarahmed.myassistant.onBoarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omarahmed.myassistant.R
import kotlinx.android.synthetic.main.slides_item.view.*

class OnBoardingAdapter(private val onBoardingInfo: List<OnBoardingData>) : RecyclerView.Adapter<OnBoardingAdapter.ViewHolder>() {
    inner class ViewHolder(private val view: View): RecyclerView.ViewHolder(view){
        fun bind(onBoardingData: OnBoardingData){
            view.textTitle.text = onBoardingData.title
            view.textDescription.text = onBoardingData.description
            view.image.setImageResource(onBoardingData.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.slides_item,parent,false
        ))
    }

    override fun getItemCount(): Int {
        return onBoardingInfo.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(onBoardingInfo[position])

    }
}