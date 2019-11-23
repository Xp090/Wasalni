package com.starbugs.wasalni_rider.ui.splash

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.Observer
import com.starbugs.wasalni_core.data.model.User
import com.starbugs.wasalni_core.ui.BaseActivity
import com.starbugs.wasalni_core.data.holder.NetworkState
import com.starbugs.wasalni_core.data.holder.WasalniPersistenceError
import com.starbugs.wasalni_rider.R
import com.starbugs.wasalni_rider.databinding.ActivityRiderSplashBinding
import com.starbugs.wasalni_rider.ui.home.HomeActivity
import com.starbugs.wasalni_rider.ui.login.LoginActivity
import org.jetbrains.anko.startActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class RiderSplashActivity : BaseActivity<ActivityRiderSplashBinding>() {

    override val mViewModel: RiderSplashViewModel by viewModel()

    override fun getLayoutRes(): Int = R.layout.activity_rider_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)


        mViewModel.fetchUserDataIfLoggedIn(2000)
            .observe(this, Observer {
                when (it) {
                    is NetworkState.Success<User> -> startActivity<HomeActivity>()
                    is NetworkState.Failure -> when (it.error) {
                        is WasalniPersistenceError.UserNotLoggedIn -> startActivity<LoginActivity>()
                    } //todo complaete the condation
                }
                finishAffinity()
            })
    }

}
