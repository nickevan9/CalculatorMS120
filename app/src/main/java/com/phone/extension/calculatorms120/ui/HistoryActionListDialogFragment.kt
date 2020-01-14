package com.phone.extension.calculatorms120.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.phone.extension.calculatorms120.R
import kotlinx.android.synthetic.main.fragment_history_action_list_dialog.*
import java.util.*



class HistoryActionListDialogFragment : BottomSheetDialogFragment() {


    private lateinit var mData: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history_action_list_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mData = ArrayList(listOf(getString(R.string.no_history)))

        val data = arguments!!.getStringArrayList(ARG_HISTORY_ACTION)
        if (data!!.isNotEmpty()) {
            mData.clear()
            mData.addAll(data)
        }

        rv_history!!.layoutManager = LinearLayoutManager(context)
        rv_history.adapter = ItemAdapter(mData)


        button_clear_history.setOnClickListener {
            data.clear()
            mData.clear()
            mData.add(getString(R.string.no_history))
            rv_history!!.adapter!!.notifyDataSetChanged()
            Toast.makeText(activity, getString(R.string.history_cleared), Toast.LENGTH_SHORT).show()
        }
    }



    private inner class ViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(R.layout.fragment_history_action_list_dialog_item, parent, false)) {
        internal val actionTextView: TextView = itemView.findViewById<View>(R.id.action) as TextView
        internal val resultTextView: TextView = itemView.findViewById<View>(R.id.result) as TextView

    }

    private inner class ItemAdapter internal constructor(private val mHistoryActionList: ArrayList<String>) : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // Regular expression lookbehind with delimiter "="
            val reg = Regex("(?<=[=])")
            val historyActionList = mHistoryActionList[position].split(reg)
            holder.actionTextView.text = if (historyActionList.size == 1) "" else historyActionList.first()
            holder.resultTextView.text = historyActionList.last().trim()
        }

        override fun getItemCount(): Int {
            return mHistoryActionList.count()
        }

    }

    companion object {

        private val ARG_HISTORY_ACTION = "history_action"

        fun newInstance(historyActionList: ArrayList<String>): HistoryActionListDialogFragment {
            val fragment = HistoryActionListDialogFragment()
            val args = Bundle()
            args.putStringArrayList(ARG_HISTORY_ACTION, historyActionList)
            fragment.arguments = args
            return fragment
        }
    }

}