package com.github.pitman2e.mutespeaker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.pitman2e.mutespeaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        replaceFragmentHome()
    }

    private fun replaceFragmentHome() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment, PreferencesFragment())
        ft.commit()
    }
}