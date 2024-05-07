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
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.descriptors.PrimitiveKind
import no.uio.ifi.IN2000.team24_app.data.bank.BankRepository
import no.uio.ifi.IN2000.team24_app.data.character.Character
import no.uio.ifi.IN2000.team24_app.data.character.ClothesRepository
import no.uio.ifi.IN2000.team24_app.data.character.Clothing
import no.uio.ifi.IN2000.team24_app.data.character.Head
import no.uio.ifi.IN2000.team24_app.data.character.Legs
import no.uio.ifi.IN2000.team24_app.data.character.Torso
import no.uio.ifi.IN2000.team24_app.data.character.getDefaultBackupCharacter
import no.uio.ifi.IN2000.team24_app.data.character.heads
import no.uio.ifi.IN2000.team24_app.data.character.legs
import no.uio.ifi.IN2000.team24_app.data.character.loadSelectedClothes
import no.uio.ifi.IN2000.team24_app.data.character.torsos


class StoreScreenViewModel(
    val bankRepository : BankRepository = BankRepository(),
    val clothesRepository : ClothesRepository= ClothesRepository(),

    private val _hodePlagg: MutableStateFlow<List<Head>> = MutableStateFlow(emptyList()),
    val hodePlagg: StateFlow<List<Head>> = _hodePlagg.asStateFlow(),

    private val _overdeler: MutableStateFlow<List<Torso>> = MutableStateFlow(emptyList()),
    val overdeler: StateFlow<List<Torso>> = _overdeler.asStateFlow(),

    private val _bukser: MutableStateFlow<List<Legs>> = MutableStateFlow(emptyList()),
    val bukser: StateFlow<List<Legs>> = _bukser.asStateFlow(),





): ViewModel() {

    private var _currentSum:MutableStateFlow<Int?> = MutableStateFlow(loadBalanceFromDisk())
    val currentSum: StateFlow<Int?> =_currentSum.asStateFlow()

    val characterState :MutableStateFlow<Character> = MutableStateFlow(loadClothesFromDisk())
    var character : StateFlow<Character> = characterState.asStateFlow()


    init {
        //load selected clothes from disk
        viewModelScope.launch {
            characterState.update { loadSelectedClothes() }
            _currentSum.update { bankRepository.getBankBalance() }
        }
        getPlagg()

    }
    fun loadClothesFromDisk(): Character {
        var character = getDefaultBackupCharacter()
        runBlocking {
            character = loadSelectedClothes()
        }
        return character
    }
    fun loadBalanceFromDisk(): Int? {
        var balance : Int? = null
        runBlocking {
            balance = bankRepository.getBankBalance()
        }
        return balance
    }


    //I assume this is not owned clothes
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

    fun unlockPlagg(plagg: Clothing){
        viewModelScope.launch(Dispatchers.IO) {
            clothesRepository.setClothingToOwned(plagg.imageAsset)
        }
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
    suspend fun hentHodeplagg(): ArrayList<Head> {
        return  ArrayList(clothesRepository.getAllNotOwnedHeads())//casting to arraylist, then i dont need to refactor too much
    }

    suspend fun hentOverdeler(): ArrayList<Torso> {
        return ArrayList(clothesRepository.getAllNotOwnedTorsos())
    }

    suspend fun hentBukser(): ArrayList<Legs> {
        return ArrayList(clothesRepository.getAllNotOwnedLegs())
    }
}

