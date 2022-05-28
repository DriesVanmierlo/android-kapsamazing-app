package org.vanmierlo.kapsamazing_app.ui.dashboard

import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import org.vanmierlo.kapsamazing_app.R
import org.vanmierlo.kapsamazing_app.databinding.FragmentDashboardBinding
import org.vanmierlo.kapsamazing_app.ui.rating.RatingFragment

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val dashboardViewModel =
//            ViewModelProvider(this).get(DashboardViewModel::class.java)
//
//        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
//        val root: View = binding.root

//        val textView: TextView = binding.newKapsalonText
//        dashboardViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }


        val view: View = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val registerButton: Button = view.findViewById(R.id.registerButton)

        registerButton.setOnClickListener{
            d("check input", "button clicked!")
            val searchBar: EditText = view.findViewById(R.id.newkapsalonSearch)
            val input = searchBar.text.toString()

            val bundle = Bundle()
            bundle.putString("kapid", input)

            val ratingFragment = RatingFragment()
            ratingFragment.arguments = bundle
            fragmentManager?.beginTransaction()?.replace(R.id.navigation_dashboard, ratingFragment)?.commit()

        }


        return view
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}