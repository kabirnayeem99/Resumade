package io.github.kabirnayeem99.resumade.adapter

import io.github.kabirnayeem99.resumade.ui.fragments.EducationFragment
import io.github.kabirnayeem99.resumade.ui.fragments.ExperienceFragment
import io.github.kabirnayeem99.resumade.ui.fragments.PersonalFragment
import io.github.kabirnayeem99.resumade.ui.fragments.ProjectsFragment

class FragmentAdapter(manager: androidx.fragment.app.FragmentManager) :
    androidx.fragment.app.FragmentPagerAdapter(manager) {

    private val listOfFragments: List<androidx.fragment.app.Fragment> =
        listOf(PersonalFragment(), EducationFragment(), ExperienceFragment(), ProjectsFragment())
    private val listOfTitles: List<String> =
        listOf("Personal", "Education", "Experience", "Projects")

    override fun getItem(position: Int): androidx.fragment.app.Fragment = listOfFragments[position]

    override fun getCount(): Int = listOfFragments.size

    override fun getPageTitle(position: Int) = listOfTitles[position]

}