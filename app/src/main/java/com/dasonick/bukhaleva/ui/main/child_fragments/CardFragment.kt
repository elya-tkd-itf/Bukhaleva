package com.dasonick.bukhaleva.ui.main.child_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.dasonick.bukhaleva.R
import com.dasonick.bukhaleva.model.CardInfo


class CardFragment : Fragment() {
    private lateinit var textCard: TextView
    private lateinit var cardImage: ImageView
    private lateinit var progress: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_card, container, false)
        textCard = root.findViewById(R.id.cardName)
        cardImage = root.findViewById(R.id.cardImage)
        progress = root.findViewById(R.id.progressBar)
        return root
    }

    fun updateCard(card: CardInfo) {
        updateCard()
        cardImage.layoutParams.height = 1500
        textCard.text = card.name

        Glide.with(this).asGif()
            .load(card.url)
            .listener(object : RequestListener<GifDrawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: GifDrawable?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    progress.visibility = View.INVISIBLE
                    cardImage.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    textCard.layoutParams.width = cardImage.layoutParams.width
                    return false
                }
            }).into(cardImage)
    }

    fun updateCard() {
        textCard.text = ""
        progress.visibility = View.VISIBLE

        textCard.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        cardImage.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        cardImage.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
    }
}