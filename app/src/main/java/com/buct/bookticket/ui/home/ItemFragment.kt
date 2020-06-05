package com.buct.bookticket.ui.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.buct.bookticket.R
import com.buct.bookticket.ui.home.dummy.DummyContent
import com.buct.bookticket.util.Get
import com.buct.bookticket.util.dingdan
import com.buct.bookticket.util.scheduleData
import com.buct.bookticket.util.todingdan

/**
 * A fragment representing a list of Items.
 */
class ItemFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private var dingdanlist=ArrayList<dingdan>()
    private lateinit var viewAdapter: ItemRecyclerViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)
        // Set the adapter
        recyclerView=view.findViewById(R.id.list)
        viewManager = LinearLayoutManager(activity)
        viewAdapter= ItemRecyclerViewAdapter(dingdanlist)
        recyclerView.adapter=viewAdapter
        recyclerView.layoutManager=viewManager
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val info = activity?.getSharedPreferences(
            "info", Context.MODE_PRIVATE)
        val user=info?.getString("user",null)
        Thread(Runnable {
            val res=Get(requireContext(),"http://49.233.81.150:9090/servier/GetUserTicket?name="+user)
            val list1= todingdan(res.toString())
            dingdanlist.clear()
            dingdanlist.addAll(list1)
            activity?.runOnUiThread {
                viewAdapter.notifyDataSetChanged()
            }
        }).start()
    }
}