package org.vanmierlo.kapsamazing_app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.toolbox.JsonObjectRequest

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    val url = "http://my-json-feed"

//    val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
//        Response.Listener { response ->
//            textView.text = "Response: %s".format(response.toString())
//        },
//        Response.ErrorListener { error ->
//            // TODO: Handle error
//        }
//    )

// Access the RequestQueue through your singleton class.
//    MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

}