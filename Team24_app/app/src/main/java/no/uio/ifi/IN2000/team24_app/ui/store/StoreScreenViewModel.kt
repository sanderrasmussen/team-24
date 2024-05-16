package no.uio.ifi.IN2000.team24_app.ui.store

import android.content.ContentValues.TAG
import android.util.Log
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

/**
 * ViewModel for the StoreScreen.
 * Contains the logic for loading the clothes that are not owned by the user,
 * and the logic for buying clothes.
 */
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

    /**
     * function for loading the character from disk. this is how the character starts, before the user potentially previews a locked clothing item.
     * @return the character with the default clothes
     */
    fun loadClothesFromDisk(): Character {
        var character = getDefaultBackupCharacter()
        runBlocking {
            character = loadSelectedClothes()
        }
        return character
    }

    /**
     * function for loading the bank balance from disk. this is how the bank balance starts, before the user potentially buys a clothing item.
     * @return the bank balance
     * @return null if the bank balance is not found
     */
    private fun loadBalanceFromDisk(): Int? {
        var balance: Int? = null
        runBlocking {
            balance = bankRepository.getBankBalance()
        }
        return balance
    }

    /**
     * function for subtracting money from the bank balance when a clothing item is bought.
     * This both sends the request to the bankRepository to withdraw the money, and updates the currentSum state.
     * @param clothingPrice the price of the clothing item
     * @see BankRepository.withdraw
     */
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

    /**
     * function for buying a clothing item. this function is called when the user clicks the buy button on a clothing item.
     * This both sets it to owned in the clothesRepository, and removes it from the state list of not owned clothes.
     */
    fun unlockClothing(clothing: Clothing) {
        viewModelScope.launch(Dispatchers.IO) {
            clothesRepository.setClothingToOwned(clothing.imageAsset)
        }
        when (clothing) {
            is Head -> {
                val oldHeads = _head.value.toMutableList()
                oldHeads.remove(clothing)
                _head.update { oldHeads }
            }
            is Torso -> {
                val oldTorsos = _torso.value.toMutableList()
                oldTorsos.remove(clothing)
                _torso.update { oldTorsos }

            }
            is Legs -> {
                val oldLegs = _legs.value.toMutableList()
                oldLegs.remove(clothing)
                _legs.update { oldLegs }
            }
        }
    }

    /**
     * function for loading the heads that are not owned by the user, gotten from the database.
     * @return a list of heads that are not owned by the user
     * @see ClothesRepository.getAllNotOwnedHeads
     */
    private fun loadNotOwnedHeads() : List<Head> {
        var heads: List<Head> = emptyList()
        runBlocking {
            heads = clothesRepository.getAllNotOwnedHeads()
        }
        return heads
    }

    /**
     * function for loading the torsos that are not owned by the user, gotten from the database.
     * @return a list of torsos that are not owned by the user
     * @see ClothesRepository.getAllNotOwnedTorsos
     */
    fun loadNotOwnedTorsos() : List<Torso>{
        var torsos : List<Torso> = emptyList()
        runBlocking {
            torsos = clothesRepository.getAllNotOwnedTorsos()
        }
        return torsos
    }

    /**
     * function for loading the legs that are not owned by the user, gotten from the database.
     * @return a list of legs that are not owned by the user
     * @see ClothesRepository.getAllNotOwnedLegs
     */
    fun loadNotOwnedLegs() : List<Legs>{
        var legs : List<Legs> = emptyList()
        runBlocking {
            legs = clothesRepository.getAllNotOwnedLegs()
        }
        return legs
    }
}