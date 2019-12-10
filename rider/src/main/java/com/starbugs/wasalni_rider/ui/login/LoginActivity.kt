package com.starbugs.wasalni_rider.ui.login

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.starbugs.wasalni_core.data.holder.NetworkState
import com.starbugs.wasalni_core.ui.BaseActivity
import com.starbugs.wasalni_rider.R
import com.starbugs.wasalni_rider.databinding.ActivityLoginBinding
import com.starbugs.wasalni_rider.ui.home.HomeActivity
import org.jetbrains.anko.startActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    override val mViewModel: LoginViewModel by viewModel()

    override fun getLayoutRes(): Int = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.userData.observe(this, Observer {
            when (it) {
                is NetworkState.Success -> {
                    startActivity<HomeActivity>()
                    finishAffinity()
                }
                is NetworkState.Failure ->{
                    Toast.makeText(this,it.error.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        })

        binding.btnLogin.setOnClickListener {
            mViewModel.login()
        }

    }

}
