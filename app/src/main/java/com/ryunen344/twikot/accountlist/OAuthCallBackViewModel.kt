package com.ryunen344.twikot.accountlist

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ryunen344.twikot.IOState
import com.ryunen344.twikot.domain.entity.Account
import com.ryunen344.twikot.domain.repository.AccountRepositoryImpl
import com.ryunen344.twikot.domain.repository.OAuthRepositoryImpl
import com.ryunen344.twikot.util.LogUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import twitter4j.TwitterException

class OAuthCallBackViewModel(
        private val accountRepositoryImpl : AccountRepositoryImpl,
        private val oAuthRepositoryImpl : OAuthRepositoryImpl
) : ViewModel(), KoinComponent {

    private var compositeDisposable : CompositeDisposable = CompositeDisposable()

    private var _ioState : MutableLiveData<IOState> = MutableLiveData(IOState.NOPE)
    val ioState : LiveData<IOState>
        get() = _ioState

    fun saveAccessToken(uri : Uri) {

        //oauth_verifierを取得する
        val verifier = uri.getQueryParameter("oauth_verifier")
        verifier ?: throw TwitterException("no verifier")

        when (ioState.value) {
            is IOState.LOADING, IOState.LOADED, IOState.ERROR("something happens!") ->
                return
        }

        // AccessTokenオブジェクトを取得
        // access token取得後書き込み（永続化）
        _ioState.value = IOState.LOADING
        oAuthRepositoryImpl.loadAccessToken(verifier)
                .flatMapCompletable {
                    LogUtil.d("token is $it")
                    accountRepositoryImpl.insertAccount(Account(it.userId, it.screenName, it.token, it.tokenSecret))
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {
                            _ioState.value = IOState.LOADED
                        },
                        {
                            _ioState.value = IOState.ERROR(it)
                        }
                ).let {
                    compositeDisposable.add(it)
                }
    }
}