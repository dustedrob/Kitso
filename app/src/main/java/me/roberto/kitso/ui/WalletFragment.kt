package me.roberto.kitso.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import me.roberto.kitso.R


/**
 * A simple [Fragment] subclass.
 */
class WalletFragment : Fragment() {

    companion object {
        val TAG = WalletFragment::class.java.simpleName!!
        fun newInstance() = WalletFragment()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wallet, container, false)
    }



}
