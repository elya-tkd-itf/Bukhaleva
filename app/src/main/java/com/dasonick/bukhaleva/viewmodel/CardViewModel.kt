package com.dasonick.bukhaleva.viewmodel

import android.annotation.SuppressLint
import android.content.ContentValues
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dasonick.bukhaleva.common.Common
import com.dasonick.bukhaleva.database.CardDataBase
import com.dasonick.bukhaleva.model.CardInfo
import com.dasonick.bukhaleva.model.CardsResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CardViewModel(private val cardDataBase: CardDataBase) : ViewModel() {
    private var hot = 0
    private var top = 0
    private var latest = 0

    private val _cardInfo: MutableStateFlow<CardInfoState> = MutableStateFlow(
        CardInfoState.Downloading(
            0
        )
    )
    val cardInfo: StateFlow<CardInfoState> = _cardInfo.asStateFlow()

    private fun getNewCards(stype: String, number: Int) {
        val i = when (stype) {
            "hot" -> hot
            "top" -> top
            else -> latest
        }
        val mService = Common.retrofitService
        mService.getCardList(stype, i).enqueue(object : Callback<CardsResult> {
            override fun onFailure(call: Call<CardsResult>, t: Throwable) {
                _cardInfo.value = CardInfoState.Error(RuntimeException())
            }

            override fun onResponse(call: Call<CardsResult>, response: Response<CardsResult>) {
                val cardsResult = response.body() as CardsResult
                if (cardsResult.result.isEmpty()) {
                    _cardInfo.value = CardInfoState.Error(RuntimeException())
                    return
                }

                for (card in cardsResult.result) {
                    val cv = ContentValues()
                    cv.put("url", card.gifURL)
                    cv.put("name", card.description)
                    val db = cardDataBase.writableDatabase
                    db.insert(stype, null, cv)
                }

                when (stype) {
                    "hot" -> hot++
                    "top" -> top++
                    else -> latest++
                }

                val card = getCardFromDB(stype, number)
                if (card != null) _cardInfo.value = CardInfoState.Success(card)
                else _cardInfo.value = CardInfoState.Error(RuntimeException())
            }
        })
    }

    fun getCard(type: Int, number: Int) {
        _cardInfo.value = CardInfoState.Downloading(number)
        viewModelScope.launch {
            val stype: String = when (type) {
                1 -> "latest"
                2 -> "top"
                else -> "hot"
            }
            val card: CardInfo? = getCardFromDB(stype, number)
            if (card != null) {
                _cardInfo.value = CardInfoState.Success(card)
                return@launch
            }

            getNewCards(stype, number)
        }
    }

    @SuppressLint("Range")
    private fun getCardFromDB(stype: String, number: Int): CardInfo? {
        val db = cardDataBase.writableDatabase
        val c = db.query(stype, null, "id = ?", arrayOf(number.toString()), null, null, null, null)
        return if (c.moveToFirst()) {
            val card = CardInfo(
                c.getInt(c.getColumnIndex("id")), c.getString(c.getColumnIndex("url")),
                c.getString(c.getColumnIndex("name"))
            )
            c.close()
            card
        } else {
            c.close()
            null
        }
    }
}

sealed class CardInfoState {
    data class Success(val card: CardInfo) : CardInfoState()
    data class Error(val exception: Throwable) : CardInfoState()
    data class Downloading(val number: Int) : CardInfoState()
}