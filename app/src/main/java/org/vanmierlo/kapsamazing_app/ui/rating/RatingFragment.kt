package org.vanmierlo.kapsamazing_app.ui.rating

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.vanmierlo.kapsamazing_app.R

class RatingFragment : Fragment() {

    companion object {
        fun newInstance() = RatingFragment()
    }

    private lateinit var viewModel: RatingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.rating_fragment, container, false)
        val kapidView: TextView = view.findViewById(R.id.kapidText)

        val args = this.arguments

        val kapid = args?.get("kapid")

        kapidView.text = kapid.toString()

        return view
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(RatingViewModel::class.java)
//
//    }

}