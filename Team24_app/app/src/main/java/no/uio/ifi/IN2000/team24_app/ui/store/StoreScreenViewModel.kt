package no.uio.ifi.IN2000.team24_app.ui.store

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.IN2000.team24_app.data.character.Clothing
import no.uio.ifi.IN2000.team24_app.data.character.Head
import no.uio.ifi.IN2000.team24_app.data.character.Legs
import no.uio.ifi.IN2000.team24_app.data.character.Torso
import no.uio.ifi.IN2000.team24_app.data.character.heads
import no.uio.ifi.IN2000.team24_app.data.character.legs
import no.uio.ifi.IN2000.team24_app.data.character.torsos


class StoreScreenViewModel: ViewModel() {

    private val _hodePlagg: MutableStateFlow<List<Head>> = MutableStateFlow(emptyList())
    val hodePlagg: StateFlow<List<Head>> = _hodePlagg

    private val _overdeler: MutableStateFlow<List<Torso>> = MutableStateFlow(emptyList())
    val overdeler: StateFlow<List<Torso>> = _overdeler

    private val _bukser: MutableStateFlow<List<Legs>> = MutableStateFlow(emptyList())
    val bukser: StateFlow<List<Legs>> = _bukser

   /* private val _antCoins = mutableStateOf(amountCoins)
    val antCoins: State<Int> = _antCoins
*/

    init {
        getPlagg()
    }


    private fun getPlagg() {
        viewModelScope.launch {
            try {
                _hodePlagg.value = hentHodeplagg()
                _overdeler.value = hentOverdeler()
                _bukser.value = hentBukser()
            } catch (e: Exception) {
                Log.e("StoreScreenViewModel", "Error fetching clothing data: ${e.message}", e)

            }

        }
    }

    /*
    fun trekkPenger(plagg: Clothing){
        val oppdatertAntCoins = antCoins.value - plagg.price
        updateAmountCoins(oppdatertAntCoins)
    }
    fun updateAmountCoins(newAmount: Int){
        _antCoins.value = newAmount
    }




    //må vi oppdatere staten til AmountCoins?
    fun getAmountCoins():Int {
          return antCoins.value;
    }
  */
    fun unlockPlagg(plagg: Clothing){
        plagg.unlocked = true;
    }

    fun hentHodeplagg(): ArrayList<Head> {
        val listeHead = heads()
        val lockedHeads = ArrayList<Head>()

        listeHead.forEach { head ->
            if (!head.unlocked) {
                lockedHeads.add(head)
            }


        }
        return lockedHeads

    }

        fun hentOverdeler(): ArrayList<Torso> {
            val listeOverdeler = torsos()
            val lockedOverdeler = ArrayList<Torso>()

            listeOverdeler.forEach { overdel ->
                if (!overdel.unlocked) {
                    lockedOverdeler.add(overdel)
                }

            }
            return lockedOverdeler
        }


        fun hentBukser(): ArrayList<Legs> {
            val listeBukser = legs()
            val lockedBukser = ArrayList<Legs>()

            listeBukser.forEach { bukse ->
                if (!bukse.unlocked) {
                    lockedBukser.add(bukse)
                }


            }
            return lockedBukser
        }


    }
