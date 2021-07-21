package com.abduqodirov.guitaronlineshop.view.screens

import android.content.Context
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
import timber.log.Timber

class BottomNavScreen : Fragment() {

    private var _binding: FragmentBottomNavScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var fManager: FragmentManager
    private var activeFragment: Fragment? = null

    private var productsScreen: Fragment? = null
    private var profileScreen: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate")
        // setUpBottomNavigation()
    }

    override fun onAttach(context: Context) {

        super.onAttach(context)
        Timber.d("onAttach")
    }

    override fun onDetach() {
        super.onDetach()
        Timber.d("onDetach")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomNavScreenBinding.inflate(inflater, container, false)
        Timber.d("onCreateView")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.d("onViewCreated")

        setUpBottomNavigation()
        setUpBottomMenuListeners()
    }

    override fun onStart() {
        super.onStart()
        Timber.d("onStart")
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume")
    }

    override fun onStop() {
        super.onStop()
        Timber.d("onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("onDestroy")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.d("onDestroyView")
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        Timber.d("onPause")
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
                Timber.d("Products screen hali qo'shilmagandi endi qo'shyabmiz")
                add(R.id.bottom_fragment_container_view, productsScreen!!)
            } else {
                Timber.d("Qo'shib bo'lignan products")
            }
            hide(productsScreen!!)

            if (!profileScreen!!.isAdded) {
                Timber.d("profile screen hali qo'shilmagandi endi qo'shyabmiz")
                add(R.id.bottom_fragment_container_view, profileScreen!!)
            } else {
                Timber.d("Qo'shib bo'lignan Profile")
            }
            hide(profileScreen!!)

            if (activeFragment != null) {
                show(activeFragment!!)
                Timber.d("Null emas active fragment va u: $activeFragment")
            } else {
                Timber.d("Active fragment null")
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
