package com.starbugs.wasalni_driver.ui.incomingrequest


import android.os.Bundle
import com.starbugs.wasalni_core.ui.BaseActivity
import com.starbugs.wasalni_driver.R
import com.starbugs.wasalni_driver.databinding.ActivityIncomingRequestBinding
import com.starbugs.wasalni_driver.ui.home.HomeActivity
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.singleTop
import org.koin.androidx.viewmodel.ext.android.viewModel


class IncomingRequestActivity : BaseActivity<ActivityIncomingRequestBinding>() {

    override val mViewModel: IncomingRequestViewModel by viewModel()

    override fun getLayoutRes(): Int = R.layout.activity_incoming_request

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.acceptBtn.setOnClickListener {
            mViewModel.accept()
            startActivity(intentFor<HomeActivity>("response" to "Accepted").singleTop())
            finish()
        }
        binding.declineBtn.setOnClickListener {
            mViewModel.decline()
            startActivity(intentFor<HomeActivity>("response" to "Declined").singleTop())
            finish()
        }
    }

}
