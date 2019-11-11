package com.ryunen344.twikot.accountlist

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ryunen344.twikot.IOState
import com.ryunen344.twikot.domain.entity.AccountAndAccountDetail
import com.ryunen344.twikot.domain.repository.AccountRepositoryImpl
import com.ryunen344.twikot.domain.repository.OAuthRepositoryImpl
import com.ryunen344.twikot.util.LogUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import java.util.concurrent.TimeUnit

class AccountListViewModel(
        private val accountRepositoryImpl : AccountRepositoryImpl,
        private val oAuthRepositoryImpl : OAuthRepositoryImpl
) : ViewModel(), KoinComponent {

    private var compositeDisposable : CompositeDisposable = CompositeDisposable()

    private var _ioState : MutableLiveData<IOState> = MutableLiveData(IOState.NOPE)
    val ioState : LiveData<IOState>
        get() = _ioState

    private var _oAuthRequestUri : MutableLiveData<Uri> = MutableLiveData()
    val oAuthRequestUri : LiveData<Uri>
        get() = _oAuthRequestUri


    private var _items : MutableLiveData<List<AccountAndAccountDetail>> = MutableLiveData()
    val items : LiveData<List<AccountAndAccountDetail>>
        get() = _items

    fun loadAccountList() {
        LogUtil.d()

        when (ioState.value) {
            is IOState.LOADING, IOState.LOADED, IOState.ERROR("something happens!") ->
                return
        }

        _ioState.value = IOState.LOADING
        accountRepositoryImpl.findAccountList()
                .delay(800, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            _ioState.value = IOState.LOADED
                            _items.value = it
                        },
                        {
                            _ioState.value = IOState.ERROR(it)
                        }
                ).let {
                    compositeDisposable.add(it)
                }
    }

    fun generateOAuthRequestUri(consumerKey : String, consumerSecretKey : String) {
        oAuthRepositoryImpl.initOAuthAuthorization(consumerKey, consumerSecretKey)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            _oAuthRequestUri.value = oAuthRepositoryImpl.loadAuthorizationURL()
                        },
                        {
                            _ioState.value = IOState.ERROR(it)
                        }
                ).let {
                    compositeDisposable.add(it)
                }
    }

    fun onDestroy(){
        compositeDisposable.dispose()
    }
}