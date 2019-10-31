package com.text.textr01.musicplayer.adapters

import android.content.Context
import android.content.Intent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.text.textr01.musicplayer.PlayerActivity
import com.text.textr01.musicplayer.R
import com.text.textr01.musicplayer.SubscriptionActivity
import com.text.textr01.musicplayer.model.DataModel


class DataAdpter(private var dataList: List<DataModel>, private val context: Context ,val phone :String) : RecyclerView.Adapter<DataAdpter.ViewHolder>() {


    val arrayList = ArrayList<String>()
    var arrayListName=ArrayList<String>()
    val arrayListArtist=ArrayList<String>()
    val arrayListImage=ArrayList<String>()

//view holder to hold the layout of list items
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_home, parent, false))
    }

    //for getting size of list of songs

    override fun getItemCount(): Int {

        return dataList.size


    }

    //to bind song upto 10

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataModel=dataList.get(position)

        holder.titleTextView.text=dataModel.song
        holder.artistTextView.text=dataModel.artists
        holder.urlTextView.text=dataModel.url
        holder.coverImageTextView.text=dataModel.cover_image


        if(dataModel.url in arrayList)
        {

        }
        else
        {
            arrayList.add(dataModel.url)
        }

        if(dataModel.song in arrayListName)
        {

        }
        else
        {
            arrayListName.add(dataModel.song)
        }

        if(dataModel.artists in arrayListArtist)
        {

        }
        else
        {
            arrayListArtist.add(dataModel.artists)
        }

        if(dataModel.cover_image in arrayListImage)
        {

        }
        else
        {
            arrayListImage.add(dataModel.cover_image)
        }


        //Toast.makeText(context,arrayList.toString(),Toast.LENGTH_LONG).show()
        //Toast.makeText(context,arrayList.size.toString(),Toast.LENGTH_SHORT).show()
        holder.itemView.setOnClickListener({
           // Toast.makeText(context,holder.titleTextView.text,Toast.LENGTH_LONG).show()
            val intent= Intent(context, PlayerActivity::class.java)
            intent.putExtra("phone",phone)
            intent.putExtra("arrayList",arrayList)
            intent.putExtra("arrayListName",arrayListName)
            intent.putExtra("arrayListArtist",arrayListArtist)
            intent.putExtra("arrayListImage",arrayListImage)
            intent.putExtra("songPosition",position.toString())
            intent.putExtra("song",holder.titleTextView.text.toString())
            intent.putExtra("artist",holder.artistTextView.text.toString())
            intent.putExtra("url",holder.urlTextView.text.toString())
            intent.putExtra("cover_image",holder.coverImageTextView.text.toString())
            context.startActivity(intent)
        })

    }

    //view holder class for recycler view

    class ViewHolder(itemLayoutView: View) : RecyclerView.ViewHolder(itemLayoutView) {
        var titleTextView:TextView
        var artistTextView:TextView
        var urlTextView:TextView
        var coverImageTextView:TextView
        init {
            titleTextView=itemLayoutView.findViewById(R.id.title)
            artistTextView=itemLayoutView.findViewById(R.id.artist)
            urlTextView=itemLayoutView.findViewById(R.id.url)
            coverImageTextView=itemLayoutView.findViewById(R.id.cover_image)



        }

    }

}
