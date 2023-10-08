package com.example.myfirstform

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myfirstform.databinding.ActivityMainBinding
import org.json.JSONObject
import kotlin.jvm.Throws
import java.util.HashMap
import com.google.gson.Gson

class MainActivity(binding: Any) : AppCompatActivity() {
    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val username = binding.username
        val password = binding.password
        val btn = binding.btnLogin
        /*val btn = binding.btnRegistrar*/

        //*boton login, cuando le hagan click al boton habrira la siguiente actividad*/
        btn.setOnClickListener {
            doLogin(username.toString(), password.toString())
        }
    }

    private fun doLogin(username: String, password: String) {
        val MyRequestQueue = Volley.newRequestQueue(this)

        val url = "http://192.168.0.2/login.php"

        val itemObjects = JSONObject()
        itemObjects.put("mail", username)
        itemObjects.put("pass", password)

        val request: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url, itemObjects,
            Response.Listener { response ->
                Log.d("response", response.toString())
                var gson = Gson()
                var formatted = gson.fromJson(response.toString(), Map::class.java)

                var gsonData = Gson()
                var formattedData = gsonData.fromJson(formatted.get("Data").toString(), Map::class.java)

                Log.d("exist FortsLogin",formattedData.containsKey("firtsLogin").toString() )

                if(formatted.containsKey("UserExist") && formatted.get("UserExist") == false){
                    openDesignActivity()
                }else if(formatted.containsKey("failedLogin")){
                    val toast = Toast.makeText(this,"Login Invalido", Toast.LENGTH_SHORT)
                    toast.show()
                } else if (formattedData.containsKey("firtsLogin") && formattedData.get("firtsLogin") == true) {
                        openProfileActivity()
                    }else {
                        //TODO redirect to home page
                }
            },
            Response.ErrorListener { error ->
                Log.d("error", error.toString())
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getBodyContentType(): String {
                return "aplication/json"
            }
            override fun getHeaders(): HashMap<String, String> {
                val apiHeader = HashMap<String, String>()
                apiHeader["Content-Type"] = "aplication/json"
                return apiHeader
            }
        }
        MyRequestQueue.add(request)
    }

    private fun openProfileActivity() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }
    private fun openDesignActivity() {
        val intent = Intent(this, DesignActivity::class.java)
        startActivity(intent)
    }
}

