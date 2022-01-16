package io.github.kabirnayeem99.resumade.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.resumade.R
import io.github.kabirnayeem99.resumade.common.base.BaseFragment
import io.github.kabirnayeem99.resumade.databinding.FragmentAboutUsBinding

@AndroidEntryPoint
class AboutUsFragment : BaseFragment<FragmentAboutUsBinding>() {

    override val layout: Int
        get() = R.layout.fragment_about_us

    override fun onCreated(savedInstanceState: Bundle?) {
        setUpViews()
    }

    private fun setUpViews() {
        val text = resources.getString(R.string.aboutUsText)
        binding.apply {
            tvAboutLinksView.text = text
            trAppRepoRow.setOnClickListener {
                val url = "https://www.github.com/kabirnayeem99/resumade"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }

            trDevProfileRow.setOnClickListener {
                val url = "https://www.github.com/kabirnayeem99"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }

            trStandardResumeRow.setOnClickListener {
                val url = "http://www.standardresume.co"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }

            trRateRow.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("market://details?id=io.github.kabirnayeem99.resumade")
                startActivity(intent)
            }
        }
    }
}
