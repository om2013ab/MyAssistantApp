package com.omarahmed.myassistant.holiday.countries

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.omarahmed.myassistant.data.remote.CountriesResponse
import com.omarahmed.myassistant.databinding.RvCountriesItemBinding

class CountriesAdapter(private val onClickListener: OnClickListener) : RecyclerView.Adapter<CountriesAdapter.CountriesViewHolder>() {

    class CountriesViewHolder(private val binding: RvCountriesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(countriesInfo: CountriesResponse.Countries.CountriesInfo){
            binding.countriesInfo = countriesInfo
            binding.executePendingBindings()
        }
    }

    object DiffCallback: DiffUtil.ItemCallback<CountriesResponse.Countries.CountriesInfo>(){
        override fun areItemsTheSame(oldItem: CountriesResponse.Countries.CountriesInfo, newItem: CountriesResponse.Countries.CountriesInfo
        )= oldItem.uuid == newItem.uuid


        override fun areContentsTheSame(oldItem: CountriesResponse.Countries.CountriesInfo, newItem: CountriesResponse.Countries.CountriesInfo
        )= oldItem == newItem
    }

    val countriesList = AsyncListDiffer(this,DiffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CountriesViewHolder(RvCountriesItemBinding.inflate(LayoutInflater.from(parent.context)))

    override fun getItemCount() = countriesList.currentList.size

    override fun onBindViewHolder(holder: CountriesViewHolder, position: Int) {
        val currentCountry = countriesList.currentList[position]
        holder.bind(currentCountry)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(currentCountry)
        }
    }

    class OnClickListener(val clickListener: (countryInfo:CountriesResponse.Countries.CountriesInfo)-> Unit){
        fun onClick(countryInfo:CountriesResponse.Countries.CountriesInfo) = clickListener(countryInfo)
    }
}