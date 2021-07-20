package com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productslist.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.abduqodirov.guitaronlineshop.view.model.SortOrderOption

class SortOrderOptionsAdapter(context: Context, options: Array<SortOrderOption>) :
    ArrayAdapter<SortOrderOption>(context, 0, options) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val option = getItem(position)

        if (view == null) {
            view = LayoutInflater.from(context)
                .inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)
        }

        val optionTitle = view!!.findViewById<TextView>(android.R.id.text1)
        optionTitle.text = option?.displayingTitleResId?.let { context.getString(it) }

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val option = getItem(position)

        if (view == null) {
            view = LayoutInflater.from(context)
                .inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)
        }

        val optionTitle = view!!.findViewById<TextView>(android.R.id.text1)
        optionTitle.text = option?.displayingTitleResId?.let { context.getString(it) }

        return view
    }
}
