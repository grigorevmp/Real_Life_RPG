package com.nonameteam.realliferpg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private var someFragment: ProfileFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.statusBarColor = getColor(R.color.cardBackground)

        val bottomNavigationBar = findViewById<BottomNavigationView>(R.id.bottom_tab_bar)
        underlineSelectedItem(-1)

        //supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MoviesFragment()).commit()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ProfileFragment(), "TAG").commit()
        } else {
            someFragment =
                supportFragmentManager.findFragmentByTag("TAG") as? ProfileFragment
        }

        bottomNavigationBar.setOnItemSelectedListener {
            underlineSelectedItem(it.itemId)

            val selectedFragment: Fragment?
            when (it.itemId) {
                R.id.profileFragment -> {
                    selectedFragment = ProfileFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit()
                    return@setOnItemSelectedListener true
                }
                R.id.mapFragment -> {
                    selectedFragment = MapFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit()
                    return@setOnItemSelectedListener true
                }
                R.id.tasksFragment -> {
                    selectedFragment = TasksFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit()
                    return@setOnItemSelectedListener true
                }
                else -> false
            }
        }

    }



    private fun underlineSelectedItem(itemId: Int) {
        val constraintLayout: ConstraintLayout = findViewById(R.id.mainLayout)
        TransitionManager.beginDelayedTransition(constraintLayout)
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.setHorizontalBias(
            R.id.underline,
            getItemPosition(itemId) * 0.5f
        )
        constraintSet.applyTo(constraintLayout)
    }

    private fun getItemPosition(itemId: Int): Int {
        return when (itemId) {
            R.id.profileFragment -> 0
            R.id.mapFragment -> 1
            R.id.tasksFragment -> 2
            else -> 0
        }
    }
}