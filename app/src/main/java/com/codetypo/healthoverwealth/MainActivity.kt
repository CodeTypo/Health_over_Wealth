package com.codetypo.healthoverwealth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG : String = "HOMEPAGE_LOG"


class MainActivity : AppCompatActivity() {

    private val firebaseRepo: FirebaseRepo = FirebaseRepo()
    private var postList: List<PostModel> = ArrayList()
    private val postListAdapter : PostListAdapter = PostListAdapter(postList);


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //check user login
        if(firebaseRepo.getUser()==null){
            //create a new one
            firebaseRepo.createUser().addOnCompleteListener {
                if(it.isSuccessful){
                    //load data
                    loadTestData()
                } else {
                    Log.d(TAG, "Error: ${it.exception!!.message}")
                }
            }
        } else {
            //user logged in
            loadTestData()
        }

        //init recyclerview
        tiles_list.layoutManager = LinearLayoutManager(this)
        tiles_list.adapter = postListAdapter



    }

    private fun loadTestData() {
        firebaseRepo.getPostlist().addOnCompleteListener {
            if(it.isSuccessful){
                postList = it.result!!.toObjects(PostModel::class.java)
                postListAdapter.postListItems = postList
                postListAdapter.notifyDataSetChanged()
            } else {
                Log.d(TAG,"Error: ${it.exception!!.message}")
            }
        }
    }
}