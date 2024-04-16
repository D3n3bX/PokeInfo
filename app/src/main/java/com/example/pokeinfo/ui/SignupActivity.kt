package com.example.pokeinfo.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.pokeinfo.databinding.ActivitySignupBinding
import com.example.pokemoninfo.model.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

// Implemtamos OnClickListener para poder gestionar las pulsaciones de los botones
class SignupActivity : AppCompatActivity(), View.OnClickListener {

    // Creamos un binding asociado a ActivitySignupBinding
    private lateinit var binding: ActivitySignupBinding

    // Creamos un FirebaseAuth
    private lateinit var authFirebase: FirebaseAuth

    // Sobreescribimos la función onCreate(savedInstance: Bundle?) para iniciar la pantalla
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializamos el binding y lo "inflamos"
        binding = ActivitySignupBinding.inflate(layoutInflater)

        // Establecemos la vista inflada como el contenido de la pantalla
        setContentView(binding.root)

        instancias()

        // Indicamos los botones con los que se puede interactuar
        binding.buttonRegister.setOnClickListener(this)
    }

    private fun instancias() {

        // Inicializamos authFirebase
        authFirebase = FirebaseAuth.getInstance()
    }

    // Sobreescribimos la función onClick(v: View?) para gestionar las pulsaciones de los botones
    override fun onClick(v: View?) {

        // Creamos un switch para saber qué botones se han pulsado
        when(v!!.id) {

            // Pulsación de Register -> confirmar el registro
            binding.buttonRegister.id -> {

                // Comprobamos que estan todos los campos
                if (binding.editName.length() != 0 && binding.editEmail.length() != 0
                    && binding.editPassword.length() != 0 && binding.editConfirmPassword.length() != 0) {

                    // Comprobamos que la contraseña es la misma en ambos campos
                    if (binding.editPassword.text.toString().equals(binding.editConfirmPassword.text.toString())) {

                        // Creamos un usuario
                        // Es val ya que no van a cambiar los datos, si cambia es porque se crea un nuevo usuario
                        val usuario: User = User(
                            binding.editName.text.toString(),
                            binding.editEmail.text.toString(),
                            binding.editPassword.text.toString()
                        )

                        // Registrar Usuario
                        authFirebase.createUserWithEmailAndPassword(usuario.correo, usuario.pass)
                            .addOnCompleteListener {
                                // Si se ha creado el usuario con éxito
                                if (it.isSuccessful) {
                                    // Pasar a una segunda pantalla -> Registro
                                    // Indicamos el origen (this o applicationContext) y el destino (<nombre>::class.java)
                                    // Creamos un intent para poder recuperar los datos en la siguiente pantalla
                                    val intent = Intent(this, LoginActivity::class.java)

                                    // Pasamos el usuario creado
                                    intent.putExtra("usuario", usuario)

                                    // Iniciamos la pantalla Login
                                    startActivity(intent)

                                    // Cada vez que se pase a la siguiente pantalla la otra se destruye
                                    finish()
                                } else {
                                    Snackbar.make(binding.root, "Fallo en el registro", Snackbar.LENGTH_SHORT)
                                        .show()
                                }
                            }
                    } else {
                        // Notificamos si ha habido algún fallo
                        Snackbar.make(binding.root, "Las contraseñas no coinciden", Snackbar.LENGTH_SHORT).show()
                    }
                } else {
                    // Notificamos si ha habido algún fallo
                    Snackbar.make(binding.root, "Faltan campos por rellenar", Snackbar.LENGTH_SHORT).show()
                }

            }
        }
    }
}
