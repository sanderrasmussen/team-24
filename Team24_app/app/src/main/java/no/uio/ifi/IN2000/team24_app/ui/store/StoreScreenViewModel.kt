package no.uio.ifi.IN2000.team24_app.ui.store

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class StoreScreenViewModel: ViewModel() {

    private val _hodePlagg: MutableStateFlow<List<Head>> = MutableStateFlow(emptyList())
    val hodePlagg: StateFlow<List<Head>> = _hodePlagg

    private val _overdeler: MutableStateFlow<List<Torso>> = MutableStateFlow(emptyList())
    val overdeler: StateFlow<List<Torso>> = _overdeler

    private val _bukser: MutableStateFlow<List<Legs>> = MutableStateFlow(emptyList())
    val bukser: StateFlow<List<Legs>> = _bukser

init {
    getPlagg()
}

    /*
    Kan ikke gjøre det på denne måten, fordi vi skal bare vise de klærne
    som ikke er unlocked enda. Kun kalle på funksjonen kommer til å vise
    alle klærne.
     */
    private fun getPlagg(){
        viewModelScope.launch {
            try {
                _hodePlagg.value = heads()
                _overdeler.value = torsos()
                _bukser.value = legs()
            } catch (e: Exception){
                Log.e("StoreScreenViewModel", "Error fetching clothing data: ${e.message}", e)

            }

        }
    }



 /*   fun hentHodeplagg():ArrayList<Head>{
        val listeHead =  heads()
        val lockedHeads = ArrayList<Head>()

        listeHead.forEach { head ->
            if(head.unlocked == false){
                lockedHeads.add(head)
        }

    }

    fun hentOverdeler(){

    }

    fun hentBukser(){

    }

  */

}