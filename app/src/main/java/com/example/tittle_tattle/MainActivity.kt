package com.example.tittle_tattle

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest




//016d829068144dc1b48c59bc9bc25329
class MainActivity : AppCompatActivity(), NewsItemClicked {

    private lateinit var mAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rv = findViewById<RecyclerView>(R.id.list)
        rv.setHasFixedSize(true)

        rv.layoutManager = LinearLayoutManager(this)
        fetchData()
        fetchData2()
        mAdapter = NewsAdapter( this)
        rv.adapter = mAdapter

    }



    private fun fetchData(){
        val categories = resources.getStringArray(R.array.categories)
        val arrayAdapter =  ArrayAdapter(this,R.layout.dropdown_item,categories)
        val value = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        value.setAdapter(arrayAdapter)

        value.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            // Get the selected item as a string
            val selectedItem = parent.getItemAtPosition(position).toString()

            Log.d("TAG", selectedItem)
            val url = "https://newsapi.org/v2/top-headlines?country=in&category=$selectedItem&apiKey=016d829068144dc1b48c59bc9bc25329"
            val getRequest: JsonObjectRequest = object : JsonObjectRequest(
                Method.GET,
                url,
                null,
                Response.Listener {
                    Log.e("TAG","$it")
                    val newsJsonArray = it.getJSONArray("articles")
                    val newsArray = ArrayList<News>()
                    for(i in 0 until  newsJsonArray.length()){
                        val newsJsonObject = newsJsonArray.getJSONObject(i)
                        val news = News(
                            newsJsonObject.getString("author"),
                            newsJsonObject.getString("title"),
                            newsJsonObject.getString("url"),
                            newsJsonObject.getString("urlToImage")
                        )
                        newsArray.add(news)
                    }
                    mAdapter.updateNews(newsArray)
                },
                Response.ErrorListener { error ->

                }
            ) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["User-Agent"] = "Mozilla/5.0"
                    return params
                }
            }
            MySingleton.getInstance(this).addToRequestQueue(getRequest)
        }
    }


    private fun fetchData2(){

            val url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=016d829068144dc1b48c59bc9bc25329"
            val getRequest: JsonObjectRequest = object : JsonObjectRequest(
                Method.GET,
                url,
                null,
                Response.Listener {
                    Log.e("TAG","$it")
                    val newsJsonArray = it.getJSONArray("articles")
                    val newsArray = ArrayList<News>()
                    for(i in 0 until  newsJsonArray.length()){
                        val newsJsonObject = newsJsonArray.getJSONObject(i)
                        val news = News(
                            newsJsonObject.getString("author"),
                            newsJsonObject.getString("title"),
                            newsJsonObject.getString("url"),
                            newsJsonObject.getString("urlToImage")
                        )
                        newsArray.add(news)
                    }
                    mAdapter.updateNews(newsArray)
                },
                Response.ErrorListener { error ->

                }
            ) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["User-Agent"] = "Mozilla/5.0"
                    return params
                }
            }
            MySingleton.getInstance(this).addToRequestQueue(getRequest)
        }

   override fun onItemClicked(item: News) {

        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))

    }
}