package com.example.pokeinfo.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.pokeinfo.databinding.ActivityLoginBinding
import com.example.pokemoninfo.model.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

// Implemtamos OnClickListener para poder gestionar las pulsaciones de los botones
class LoginActivity : AppCompatActivity(), View.OnClickListener {

    // Creamos un binding asociado a ActivityLoginBinding
    private lateinit var binding: ActivityLoginBinding

    // Creamos un user
    private var user: User? = null // La primera vez que se inicializa el usuario es nulo

    // Creamos un FirebaseAuth
    private lateinit var authFirebase: FirebaseAuth

    // Sobreescribimos la función onCreate(savedInstance: Bundle?) para iniciar la pantalla
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializamos el binding y lo "inflamos"
        binding = ActivityLoginBinding.inflate(layoutInflater)

        // Establecemos la vista inflada como el contenido de la pantalla
        setContentView(binding.root)

        instancias()

        // Recuperamos lo que nos ha pasado la pantalla de SignupActivity
        user = intent.getSerializableExtra("usuario") as User?

        // Si user no es null
        if (user != null) {
            // Rellenamos los atibutos de User
            binding.user.setText(user!!.nombre)
            binding.pass.setText(user!!.pass)
        }

        // Indicamos los botones con los que se puede interactuar
        binding.buttonSignUp.setOnClickListener(this) // Iniciar sesión
        binding.buttonLogin.setOnClickListener(this) // Registrar nueva cuenta
    }

    private fun instancias() {

        // Inicializamos authFirebase
        authFirebase = FirebaseAuth.getInstance()
    }

    // Sobreescribimos la función onClick(v: View?) para gestionar las pulsaciones de los botones
    override fun onClick(v: View?) {

        // Creamos un switch para saber qué botones se han pulsado
        when(v!!.id) {

            // Pulsación de SignUp -> iniciar sesión
            binding.buttonSignUp.id -> {

                // Pasar a una segunda pantalla -> Registro
                // Indicamos el origen (this o applicationContext) y el destino (<nombre>::class.java)
                // Creamos un intent para poder recuperar los datos en la siguiente pantalla
                val intent = Intent(this, SignupActivity::class.java)

                // Iniciamos la pantalla SignUp
                startActivity(intent)

                // Cada vez que se pase a la siguiente pantalla la otra se destruye
                finish()

            }
            // Pulsación de Login -> crear cuenta
            binding.buttonLogin.id -> {
                // Si los campos estan rellenos
                if (binding.user.text.isNotEmpty() && binding.pass.text.isNotEmpty()) {

                    // Pasar a una segunda pantalla -> User
                    // Indicamos el origen (this o applicationContext) y el destino (<nombre>::class.java)
                    // Creamos un intent para poder recuperar los datos en la siguiente pantalla
                    /* val intent = Intent(this, MainActivity::class.java)

                    // Pasamos el nombre
                    intent.putExtra("nombre", binding.user.text.toString())

                    // Iniciamos la pantalla Main
                    startActivity(intent) */

                    authFirebase.signInWithEmailAndPassword(binding.user.text.toString(), binding.pass.text.toString())
                        .addOnCompleteListener {
                            // Si ha sido existoso
                            if (it.isSuccessful) {
                                // Pasar a una segunda pantalla
                                val currentUser: FirebaseUser = authFirebase.currentUser!!

                                // Creamos el userId
                                val uid: String = currentUser.uid

                                // Pasar a una segunda pantalla -> User
                                // Indicamos el origen (this o applicationContext) y el destino (<nombre>::class.java)
                                // Creamos un intent para poder recuperar los datos en la siguiente pantalla
                                val intent = Intent(this, MainActivity::class.java)

                                // Pasamos el nombre
                                intent.putExtra("nombre", binding.user.text.toString())

                                // Pasamos el uid
                                intent.putExtra("uid", binding.user.text.toString())

                                // Iniciamos la pantalla Main
                                startActivity(intent)
                            }
                        }
                } else {
                    // Notificamos si ha habido algún fallo
                    Snackbar.make(binding.root, "Fallo en el login, ¿Quieres crear una cuenta?", Snackbar.LENGTH_SHORT)
                        .setAction("Crear") {
                            // Pasar a una segunda pantalla -> Registro
                            // Indicamos el origen (this o applicationContext) y el destino (<nombre>::class.java)
                            // Creamos un intent para poder recuperar los datos en la siguiente pantalla
                            val intent = Intent(this, SignupActivity::class.java)

                            // Iniciamos la pantalla SignUp
                            startActivity(intent)

                            // Cada vez que se pase a la siguiente pantalla la otra se destruye
                            finish()
                        }.show()
                }
            }
        }
    }
}