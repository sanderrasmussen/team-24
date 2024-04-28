package no.uio.ifi.IN2000.team24_app.ui.store


import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import no.uio.ifi.IN2000.team24_app.data.bank.BankRepository
import no.uio.ifi.IN2000.team24_app.data.character.Character
import no.uio.ifi.IN2000.team24_app.data.character.Clothing
import no.uio.ifi.IN2000.team24_app.data.character.Head
import no.uio.ifi.IN2000.team24_app.data.character.Legs
import no.uio.ifi.IN2000.team24_app.data.character.Torso
import no.uio.ifi.IN2000.team24_app.data.character.heads
import no.uio.ifi.IN2000.team24_app.data.character.legs
import no.uio.ifi.IN2000.team24_app.data.character.torsos


class StoreScreenViewModel: ViewModel() {

    private val _hodePlagg: MutableStateFlow<List<Head>> = MutableStateFlow(emptyList())
    val hodePlagg: StateFlow<List<Head>> = _hodePlagg.asStateFlow()

    private val _overdeler: MutableStateFlow<List<Torso>> = MutableStateFlow(emptyList())
    val overdeler: StateFlow<List<Torso>> = _overdeler.asStateFlow()

    private val _bukser: MutableStateFlow<List<Legs>> = MutableStateFlow(emptyList())
    val bukser: StateFlow<List<Legs>> = _bukser.asStateFlow()


    private val characterStore = Character(head = heads().first(), torso = torsos().first(), legs = legs().first())
    val characterStateStore = MutableStateFlow(characterStore)

    private var _currentSum:MutableStateFlow<Int?> = MutableStateFlow(null)
    val currentSum: StateFlow<Int?> =_currentSum.asStateFlow()

    val bankRepository = BankRepository()




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








    fun subtractMoney(clothingPrice: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                bankRepository.withdraw(clothingPrice)
                _currentSum.update { it -> it?.minus(clothingPrice)?:0}
            } catch (e: Exception) {
                Log.e(TAG, "Error subtracting money: ${e.message}", e)
            }
        }
    }


    suspend fun getCurrentSum() {
         try {
            var sum: Int? = null
            viewModelScope.launch {
                sum = withContext(Dispatchers.IO) {
                    bankRepository.getBankBalance()
                }
            }.join() // Ensure the coroutine completes before returning the sum
            _currentSum.update { sum }

        } catch (e: Exception) {
            // Handle any errors that may occur during the coroutine execution
            Log.e(TAG, "Error getting bank balance: ${e.message}", e)
        }
    }







    fun unlockPlagg(plagg: Clothing){
        plagg.unlocked = true;
        when(plagg){

            is Head -> {
               val test =  _hodePlagg.value.toMutableList()
                test.remove(plagg)
                _hodePlagg.update { test }
            }
            is Torso -> {
                val test =  _overdeler.value.toMutableList()
                test.remove(plagg)
                _overdeler.update { test }

            }

            is Legs -> {
                val test =  _bukser.value.toMutableList()
                test.remove(plagg)
                _bukser.update { test }
            }


        }
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

