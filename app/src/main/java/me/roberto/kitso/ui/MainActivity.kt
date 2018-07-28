package me.roberto.kitso.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import me.roberto.kitso.R

class MainActivity : AppCompatActivity() {

//TODO: USE THIS ACTIVITY WHEN MORE CONTENT IS ADDED TO THE APP


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                switchFragment(WalletFragment.newInstance(), WalletFragment.TAG)

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                switchFragment(MarketFragment.newInstance(), MarketFragment.TAG)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navigation = findViewById<BottomNavigationView>(R.id.navigation)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


        switchFragment(MarketFragment.newInstance(), MarketFragment.TAG)



    }



    fun switchFragment(fragment: Fragment, tag: String): Boolean {
        if (fragment.isAdded) return false
        val ft = supportFragmentManager.beginTransaction()
        supportFragmentManager.findFragmentById(R.id.container)?.let { ft.detach(it) }
        attachFragment(fragment, tag, ft)
        supportFragmentManager.executePendingTransactions()
        return true
    }

    fun attachFragment(fragment: Fragment, tag: String, ft: FragmentTransaction) {
        if (fragment.isDetached) {
            ft.attach(fragment)
        } else {
            ft.add(R.id.container, fragment, tag)
        }
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
    }
}
