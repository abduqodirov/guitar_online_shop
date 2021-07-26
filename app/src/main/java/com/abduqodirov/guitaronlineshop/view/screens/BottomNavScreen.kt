package com.abduqodirov.guitaronlineshop.view.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.abduqodirov.guitaronlineshop.R
import com.abduqodirov.guitaronlineshop.databinding.FragmentBottomNavScreenBinding
import com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productslist.ProductsListFragment
import com.abduqodirov.guitaronlineshop.view.screens.profile.ProfileFragment

class BottomNavScreen : Fragment() {

    private var _binding: FragmentBottomNavScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var fManager: FragmentManager
    private var activeFragment: Fragment? = null

    private var productsScreen: Fragment? = null
    private var profileScreen: Fragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomNavScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpBottomNavigation()
        setUpBottomMenuListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpBottomNavigation() {
        fManager = requireActivity().supportFragmentManager

        productsScreen = ProductsListFragment.newInstance()
        profileScreen = ProfileFragment.newInstance()

        // if (productsScreen == null) {
        //     productsScreen = ProductsListFragment.newInstance()
        // }
        //
        // if (profileScreen == null) {
        //     profileScreen = ProfileFragment.newInstance()
        // }

        if (activeFragment == null) {
            activeFragment = productsScreen
        }

        fManager.beginTransaction().apply {
            if (!productsScreen!!.isAdded) {
                add(R.id.bottom_fragment_container_view, productsScreen!!)
            }
            hide(productsScreen!!)

            if (!profileScreen!!.isAdded) {
                add(R.id.bottom_fragment_container_view, profileScreen!!)
            }
            hide(profileScreen!!)

            if (activeFragment != null) {
                show(activeFragment!!)
            }
        }.commit()
    }

    private fun setUpBottomMenuListeners() {
        fManager = requireActivity().supportFragmentManager

        binding.bottomFragmentMenu.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.productsListFragment -> switchFragmentTo(productsScreen, fManager)

                R.id.profileFragment -> switchFragmentTo(profileScreen, fManager)

                else -> false
            }
        }
    }

    private fun switchFragmentTo(
        selectedFragment: Fragment?,
        fragmentManager: FragmentManager
    ): Boolean {
        fragmentManager.beginTransaction().also {
            if (activeFragment != null) {
                it.hide(activeFragment!!)
            }
            if (selectedFragment != null) {
                it.show(selectedFragment)
            }
        }.commit()
        activeFragment = selectedFragment
        return true
    }
}
