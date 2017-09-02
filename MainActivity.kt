package com.user.canopas.rxkotlin

import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import android.support.v7.widget.GridLayoutManager
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView


class MainActivity : AppCompatActivity() {
    val URL = "http://microblogging.wingnity.com/JSONParsingTutorial/"
    var Arr_list: ArrayList<Actors>? = null
    var recyclerView: RecyclerView? = null
    var adptr: Cust_adptr? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView) as RecyclerView?

        val mLayoutManager = GridLayoutManager(this, 2)
        recyclerView!!.setLayoutManager(mLayoutManager)

        Arr_list = ArrayList<Actors>()

        getActorsList()


    }

    private fun getActorsList() {
        val Arr = ArrayList<Actors>()

        val gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
        val retrofit = Retrofit.Builder().baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        val observable = retrofit.create(ApiService::class.java).getActors()
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ actorList ->
                    var list = actorList.result
                    for (i in 0..list.size - 1) {

                        Arr.add(Actors(list[i].name,
                                list[i].description,
                                list[i].dob,
                                list[i].country,
                                list[i].height,
                                list[i].spouse,
                                list[i].children,
                                list[i].image))

                    }
                },
                        { e -> Log.e("error", e.message) },
                        {
                            if (Arr_list != null)
                                Arr_list = Arr
                            adptr = Cust_adptr(Arr_list, this@MainActivity)
                            recyclerView!!.adapter = adptr
                        })


    }


    class Cust_adptr(internal var arr_list: ArrayList<Actors>?, internal var c: Context) : RecyclerView.Adapter<Cust_adptr.viewHolder>() {
        var listener: AdapterView.OnItemClickListener? = null
        override fun onBindViewHolder(holder: viewHolder?, pos: Int) {
            Glide.with(c).load(arr_list!![pos].image).into(holder!!.img)
            holder.actor_name.text = arr_list!![pos].name
            holder.bind(arr_list!![pos], c);


        }

        override fun getItemCount() = arr_list!!.size

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Cust_adptr.viewHolder {
            val view = LayoutInflater.from(c).inflate(R.layout.rowdesign, parent, false)


            return viewHolder(view)
        }

        class viewHolder(internal var view: View?) : RecyclerView.ViewHolder(view) {
            val img = view!!.findViewById<ImageView>(R.id.img)

            val actor_name = view!!.findViewById<TextView>(R.id.name)
            fun bind(actors: Actors, c: Context) {
                view!!.setOnClickListener(View.OnClickListener {
                    //                    var actor = Actors(actors.name, actors.description, actors.dob, actors.country, actors.height, actors.spouse, actors.children, actors.image)
                    var dialog = Dialog(c)
                    dialog.setContentView(R.layout.actor_detail)
                    val img = dialog.findViewById<CircleImageView>(R.id.photo)
                    val name = dialog.findViewById<TextView>(R.id.name_txt)
                    val desc = dialog.findViewById<TextView>(R.id.description_txt)
                    val country = dialog.findViewById<TextView>(R.id.country_txt)
                    val dob = dialog.findViewById<TextView>(R.id.dob_txt)
                    val spouse = dialog.findViewById<TextView>(R.id.spouse_txt)
                    val child = dialog.findViewById<TextView>(R.id.child_txt)
                    val height = dialog.findViewById<TextView>(R.id.height_txt)
                    Glide.with(c).load(actors.image).into(img)
                    desc.text=actors.description
                    name.text = actors.name
                    country.text = actors.country
                    child.text = "Children: "+actors.children
                    height.text = "Height: "+actors.height
                    dob.text = "DOB: "+actors.dob
                    spouse.text = "Spouse: "+actors.spouse
                    dialog.show()
                })
            }
        }


    }
}






