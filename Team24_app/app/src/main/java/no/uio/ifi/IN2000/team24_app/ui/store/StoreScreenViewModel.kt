package no.uio.ifi.IN2000.team24_app.ui.store

import android.content.ContentValues.TAG
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import no.uio.ifi.IN2000.team24_app.data.bank.BankRepository
import no.uio.ifi.IN2000.team24_app.data.character.Character
import no.uio.ifi.IN2000.team24_app.data.character.ClothesRepository
import no.uio.ifi.IN2000.team24_app.data.character.Clothing
import no.uio.ifi.IN2000.team24_app.data.character.Head
import no.uio.ifi.IN2000.team24_app.data.character.Legs
import no.uio.ifi.IN2000.team24_app.data.character.Torso
import no.uio.ifi.IN2000.team24_app.data.character.getDefaultBackupCharacter
import no.uio.ifi.IN2000.team24_app.data.character.loadSelectedClothes

@RequiresApi(Build.VERSION_CODES.O)
class StoreScreenViewModel(): ViewModel() {

    val bankRepository: BankRepository = BankRepository()
    val clothesRepository: ClothesRepository = ClothesRepository()

    private val _head: MutableStateFlow<List<Head>> = MutableStateFlow(loadNotOwnedHeads())
    val head: StateFlow<List<Head>> = _head.asStateFlow()

    private val _torso: MutableStateFlow<List<Torso>> = MutableStateFlow(loadNotOwnedTorsos())
    val torso: StateFlow<List<Torso>> = _torso.asStateFlow()

    private val _legs: MutableStateFlow<List<Legs>> = MutableStateFlow(loadNotOwnedLegs())
    val legs: StateFlow<List<Legs>> = _legs.asStateFlow()

    private var _currentSum: MutableStateFlow<Int?> = MutableStateFlow(loadBalanceFromDisk())
    val currentSum: StateFlow<Int?> = _currentSum.asStateFlow()

    val characterState: MutableStateFlow<Character> = MutableStateFlow(loadClothesFromDisk())
    var character: StateFlow<Character> = characterState.asStateFlow()

    init {
        //load selected clothes from disk
        viewModelScope.launch {
            characterState.update { loadSelectedClothes() }
            _currentSum.update { bankRepository.getBankBalance() }
            _head.update { clothesRepository.getAllNotOwnedHeads()}
            _torso.update { clothesRepository.getAllNotOwnedTorsos()}
            _legs.update { clothesRepository.getAllNotOwnedLegs()}
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadClothesFromDisk(): Character {
        var character = getDefaultBackupCharacter()
        runBlocking {
            character = loadSelectedClothes()
        }
        return character
    }

    fun loadBalanceFromDisk(): Int? {
        var balance: Int? = null
        runBlocking {
            balance = bankRepository.getBankBalance()
        }
        return balance
    }

    fun subtractMoney(clothingPrice: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                bankRepository.withdraw(clothingPrice)
                _currentSum.update { it -> it?.minus(clothingPrice) ?: 0 }
            } catch (e: Exception) {
                Log.e(TAG, "Error subtracting money: ${e.message}", e)
            }
        }
    }
    fun unlockClothing(clothing: Clothing) {
        viewModelScope.launch(Dispatchers.IO) {
            clothesRepository.setClothingToOwned(clothing.imageAsset)
        }
        when (clothing) {
            is Head -> {
                val test = _head.value.toMutableList()
                test.remove(clothing)
                _head.update { test }
            }
            is Torso -> {
                val test = _torso.value.toMutableList()
                test.remove(clothing)
                _torso.update { test }

            }
            is Legs -> {
                val test = _legs.value.toMutableList()
                test.remove(clothing)
                _legs.update { test }
            }
        }
    }
    fun loadNotOwnedHeads() : List<Head> {
        var heads: List<Head> = emptyList()
        runBlocking {
            heads = clothesRepository.getAllNotOwnedHeads()
        }
        return heads
    }
    fun loadNotOwnedTorsos() : List<Torso>{
        var torsos : List<Torso> = emptyList()
        runBlocking {
            torsos = clothesRepository.getAllNotOwnedTorsos()
        }
        return torsos
    }
    fun loadNotOwnedLegs() : List<Legs>{
        var legs : List<Legs> = emptyList()
        runBlocking {
            legs = clothesRepository.getAllNotOwnedLegs()
        }
        return legs
    }
}