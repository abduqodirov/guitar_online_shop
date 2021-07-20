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

    private lateinit var activeFragment: Fragment

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpBottomNavigation() {
        val fragmentManager = requireActivity().supportFragmentManager

        val productsScreen = ProductsListFragment.newInstance()
        val profileScreen = ProfileFragment.newInstance()

        activeFragment = productsScreen

        fragmentManager.beginTransaction().apply {
            add(R.id.bottom_fragment_container_view, productsScreen)
            add(R.id.bottom_fragment_container_view, profileScreen).hide(profileScreen)
        }.commit()

        binding.bottomFragmentMenu.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.productsListFragment -> switchFragmentTo(productsScreen, fragmentManager)

                R.id.profileFragment -> switchFragmentTo(profileScreen, fragmentManager)

                else -> false
            }
        }
    }

    private fun switchFragmentTo(
        selectedFragment: Fragment,
        fragmentManager: FragmentManager
    ): Boolean {
        fragmentManager.beginTransaction().also {
            it.hide(activeFragment)
            it.show(selectedFragment)
        }.commit()
        activeFragment = selectedFragment
        return true
    }
}
