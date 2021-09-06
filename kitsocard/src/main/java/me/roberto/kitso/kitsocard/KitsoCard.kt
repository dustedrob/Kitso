package me.roberto.kitso.kitsocard

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.cardview.widget.CardView

/**
 * Created by roberto on 14/06/17.
 */
class KitsoCard(context: Context, attrs: AttributeSet) : CardView(context, attrs) {
    init {
        val obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.KitsoCard)
        val top_title_text = obtainStyledAttributes.getString(R.styleable.KitsoCard_top_title)
        val amount_text = obtainStyledAttributes.getString(R.styleable.KitsoCard_amount)
        val bottom_title_text = obtainStyledAttributes.getString(R.styleable.KitsoCard_bottom_title)
        initializeViews(context)

        var textView = findViewById<TextView>(R.id.top_title)
        textView.text=top_title_text

        textView = findViewById(R.id.amount)
        textView.text=amount_text


        textView = findViewById(R.id.bottom_title)
        textView.text=bottom_title_text
    }




    fun initializeViews(context: Context)
    {

        val layoutInflater= context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layoutInflater.inflate(R.layout.kitso_card,this)

    }

    fun setTopTitle(text: String)
    {
        val topTitle=this.findViewById(R.id.top_title) as TextView
        topTitle.text=text
    }


    fun setAmount(text: String)
    {
        val amount=this.findViewById(R.id.amount) as TextView
        amount.text=text
    }

    fun setBottomTitle(text: String )
    {
        val bottomTitle=this.findViewById(R.id.bottom_title) as TextView
        bottomTitle.text=text
    }


    override fun onFinishInflate() {
        super.onFinishInflate()


    }
}