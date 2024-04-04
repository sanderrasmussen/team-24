package no.uio.ifi.IN2000.team24_app.ui.store


import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
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


class StoreScreenViewModel(sharedViewModel: SharedBankViewModel): ViewModel() {
    private val bankLiveData: LiveData<Bank> = sharedViewModel.bankkLiveData

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

            } catch (e: Exception){
                Log.e(TAG, "An error occurred: ${e.message}", e)

            }
        }
    }


    /*

    fun getBankLiveData(): LiveData<Bank> {
        return bankLiveData
    }
}

fun subtractMoney(clothingPrice: Int) {
    val bank = bankLiveData.value
    bank?.withdrawMoney(clothingPrice)
    bank?.let { sharedViewModel.setBankLiveData(it) }
}

fun getCurrentSum(): Int {
    val bank = bankLiveData.value
    return bank?.sum ?: 0 // Defaulter til 0 hvis bank er null
}


     */
fun unlockPlagg(plagg: Clothing){
        plagg.unlocked = true;
    }

    //fjerne penger. Sette unlocked =true.
    //grå ut de man ikke har råd til.


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
