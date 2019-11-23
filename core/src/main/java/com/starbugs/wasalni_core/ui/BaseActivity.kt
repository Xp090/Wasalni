package com.starbugs.wasalni_core.ui

import android.os.Bundle
import androidx.annotation.LayoutRes

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties

abstract class BaseActivity< B : ViewDataBinding> : AppCompatActivity() {

    @LayoutRes
    abstract fun getLayoutRes(): Int

    protected abstract val mViewModel: BaseViewModel
    protected lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutRes())
        binding.lifecycleOwner = this
        bindViewModel()
    }

    @Suppress("UNCHECKED_CAST")
    fun bindViewModel() {
        try {
            binding::class.java.getDeclaredMethod("setMViewModel",mViewModel::class.java)
                .invoke(binding,mViewModel)
        } catch (e: RuntimeException) {
            throw RuntimeException("You must set 'mViewModel' variable inside activity xml")
        }

    }

}
