package com.starbugs.wasalni_rider.ui.login

import android.os.Bundle
import androidx.lifecycle.Observer
import com.starbugs.wasalni_core.ui.BaseActivity
import com.starbugs.wasalni_rider.R
import com.starbugs.wasalni_rider.databinding.ActivityLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    override val mViewModel: LoginViewModel by viewModel()

    override fun getLayoutRes(): Int = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.userData.observe(this, Observer {
            Timber.e(it.toString())
        })

        binding.btnLogin.setOnClickListener {
            mViewModel.login()
        }

    }

}
