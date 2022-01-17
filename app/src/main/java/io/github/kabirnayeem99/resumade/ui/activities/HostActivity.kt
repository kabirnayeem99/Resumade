package io.github.kabirnayeem99.resumade.ui.activities

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.resumade.R
import io.github.kabirnayeem99.resumade.common.base.BaseActivity
import io.github.kabirnayeem99.resumade.databinding.ActivityHostBinding

@AndroidEntryPoint
class HostActivity : BaseActivity<ActivityHostBinding>() {
    override val layout: Int
        get() = R.layout.activity_host

    override fun onCreated(savedInstanceState: Bundle?) {

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}