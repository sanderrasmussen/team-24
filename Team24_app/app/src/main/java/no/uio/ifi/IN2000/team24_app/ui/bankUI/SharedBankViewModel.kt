package no.uio.ifi.IN2000.team24_app.ui.bankUI

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import no.uio.ifi.IN2000.team24_app.data.bankRepository.Bank

class SharedBankViewModel: ViewModel(){

    private val bankLiveData = MutableLiveData<Bank>()

    fun getBankLiveData(): LiveData<Bank> {

        return bankLiveData
    }

    fun setBankLiveData(bank: Bank){
        bankLiveData.value = bank
    }

}