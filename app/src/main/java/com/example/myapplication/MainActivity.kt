package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.src.authusers.application.AuthUserServices
import com.example.myapplication.src.shared.domain.TokenProvider
import com.example.myapplication.stores.AuthUserStore
import com.example.myapplication.ui.shared.NavHeaderMainFragment
import com.example.myapplication.ui.shared.SharedViewModel
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    @Inject lateinit var tokenProvider: TokenProvider
    @Inject lateinit var authUserServices: AuthUserServices

    // ✅ ViewModel gestionado por Hilt (gracias al @AndroidEntryPoint)
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // 🔥 SUPER IMPORTANTE: llamar primero a super.onCreate
        super.onCreate(savedInstanceState)

        // ✅ Inflamos el layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ Configuramos toolbar y navegación
        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab)
                .show()
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow),
            drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // ✅ Cargamos el header del menú lateral
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_view, NavHeaderMainFragment())
            .commit()
    }
    override fun onStart() {
        super.onStart()

        lifecycleScope.launch {
            try {
                // Verificar nuevamente antes de usar
                if (!::tokenProvider.isInitialized) {
                    Log.e("MainActivity", "tokenProvider no inicializado en onStart")
                    return@launch
                }

                val token = tokenProvider.getToken()
                if (token.isNullOrEmpty()) {
                    Log.d("TokenCheck", "Token vacío")
                } else {
                    Log.d("TokenCheck", "Token encontrado: $token")
                    if (::authUserServices.isInitialized) {
                        AuthUserStore.setAuthUser(applicationContext, authUserServices.getUserInfo())
                    }
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error en onStart", e)
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}