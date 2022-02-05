package com.dasonick.bukhaleva.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dasonick.bukhaleva.R
import com.dasonick.bukhaleva.database.CardDataBase
import com.dasonick.bukhaleva.model.CardInfo
import com.dasonick.bukhaleva.ui.main.child_fragments.CardFragment
import com.dasonick.bukhaleva.ui.main.child_fragments.ErrorConnectionFragment
import com.dasonick.bukhaleva.viewmodel.CardInfoState
import com.dasonick.bukhaleva.viewmodel.CardViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment(private val sectionNumber: Int) : Fragment() {
    private enum class MyFragmentState {
        CARD_FRAGMENT, ERROR_FRAGMENT, NO_FRAGMENT
    }

    private var cardNumber: Int = 0
    private lateinit var cardViewModel: CardViewModel
    private lateinit var fragmentState: MyFragmentState
    private lateinit var cardFragment: CardFragment
    private val errorFragment = ErrorConnectionFragment()
    private lateinit var lastCardButton: FloatingActionButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cardNumber = savedInstanceState?.getInt(CARD_NUMBER) ?: 1
        fragmentState = (savedInstanceState?.getSerializable(FRAGMENT_STATE)
            ?: MyFragmentState.NO_FRAGMENT) as MyFragmentState

        val root = inflater.inflate(R.layout.fragment_tab, container, false)
        val nextCardButton: FloatingActionButton = root.findViewById(R.id.nextCard)
        val newCardButton: FloatingActionButton = root.findViewById(R.id.newCard)

        lastCardButton = root.findViewById(R.id.backCard)

        cardViewModel = CardViewModel(CardDataBase(context))

        nextCardButton.setOnClickListener {
            cardViewModel.getCard(sectionNumber, cardNumber + 1)
        }
        newCardButton.setOnClickListener {
            cardViewModel.getCard(sectionNumber, 1)
        }
        lastCardButton.setOnClickListener {
            cardViewModel.getCard(sectionNumber, cardNumber - 1)
        }
        cardFragment = CardFragment()
        if (fragmentState == MyFragmentState.NO_FRAGMENT) {
            val fram = childFragmentManager.beginTransaction()
            fram.add(R.id.container, cardFragment)
            fragmentState = MyFragmentState.CARD_FRAGMENT
            fram.commit()
        }

        lastCardButton.hide()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.wtf("lol", "onViewCreated $sectionNumber")
        cardViewModel.getCard(sectionNumber, cardNumber)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cardViewModel.cardInfo.collect { it ->
                    Log.wtf("lol", "repeated")
                    when (it) {
                        is CardInfoState.Success -> updateCard(it.card)
                        is CardInfoState.Error -> showError()
                        is CardInfoState.Downloading -> createCardDownload(it.number)
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CARD_NUMBER, cardNumber)
        outState.putSerializable(FRAGMENT_STATE, fragmentState)
        Log.wtf("lol", "saved")
    }

    private fun createCardDownload(number: Int) {
        if (fragmentState != MyFragmentState.CARD_FRAGMENT) {
            val frag = childFragmentManager.beginTransaction()
            frag.replace(R.id.container, cardFragment)
            fragmentState = MyFragmentState.CARD_FRAGMENT
            frag.commit()
        }
        cardFragment.updateCard()
        cardNumber = number
        Log.wtf("lol", cardNumber.toString())
        if (cardNumber > 1) lastCardButton.show()
        else lastCardButton.hide()

        Log.wtf("lol", "card downloading")
    }

    private fun updateCard(card: CardInfo) {
        cardFragment.updateCard(card)
        Log.wtf("lol", "card done")
    }

    private fun showError() {
        if (fragmentState != MyFragmentState.ERROR_FRAGMENT) {
            val fram = childFragmentManager.beginTransaction()
            fram.replace(R.id.container, errorFragment)
            fragmentState = MyFragmentState.ERROR_FRAGMENT
            fram.commit()
        }

        Log.wtf("lol", "card error")
    }

    companion object {
        private const val CARD_NUMBER = "card_number"
        private const val FRAGMENT_STATE = "fragment_state"
    }
}