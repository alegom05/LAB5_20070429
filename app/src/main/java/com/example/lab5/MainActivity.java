package com.example.lab5;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.lab5.ConfiguracionActivity;
import com.example.lab5.MedicamentosActivity;
import com.example.lab5.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView tvSaludo, tvMensaje;
    private ImageView imgPerfil;
    private Button btnMedicamentos, btnConfiguraciones;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MedicamentosPrefs";
    private static final String KEY_USUARIO = "usuario";
    private static final String KEY_MENSAJE = "mensaje";
    private static final String IMAGE_FILE_NAME = "perfil_image.jpg";

    private ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initSharedPreferences();
        setupGalleryLauncher();
        loadUserData();
        loadProfileImage();
        setupClickListeners();
    }

    private void initViews() {
        tvSaludo = findViewById(R.id.tv_saludo);
        tvMensaje = findViewById(R.id.tv_mensaje);
        imgPerfil = findViewById(R.id.img_perfil);
        btnMedicamentos = findViewById(R.id.btn_medicamentos);
        btnConfiguraciones = findViewById(R.id.btn_configuraciones);
    }

    private void initSharedPreferences() {
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    }

    private void setupGalleryLauncher() {
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {
                            saveImageToInternalStorage(imageUri);
                        }
                    }
                }
        );
    }

    private void loadUserData() {
        String usuario = sharedPreferences.getString(KEY_USUARIO, "Usuario");
        String mensaje = sharedPreferences.getString(KEY_MENSAJE, "¡Recuerda tomar tus medicamentos a tiempo!");

        // Saludo dinámico con hora
        String horaActual = new SimpleDateFormat("HH", Locale.getDefault()).format(new Date());
        int hora = Integer.parseInt(horaActual);
        String saludoHora;

        if (hora < 12) {
            saludoHora = "¡Buenos días";
        } else if (hora < 18) {
            saludoHora = "¡Buenas tardes";
        } else {
            saludoHora = "¡Buenas noches";
        }

        tvSaludo.setText(saludoHora + ", " + usuario + "!");
        tvMensaje.setText(mensaje);
    }

    private void loadProfileImage() {
        File imageFile = new File(getFilesDir(), IMAGE_FILE_NAME);
        if (imageFile.exists()) {
            try {
                FileInputStream fis = new FileInputStream(imageFile);
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                imgPerfil.setImageBitmap(bitmap);
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupClickListeners() {
        imgPerfil.setOnClickListener(v -> openGallery());

        btnMedicamentos.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MedicamentosActivity.class);
            startActivity(intent);
        });

        btnConfiguraciones.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ConfiguracionActivity.class);
            startActivity(intent);
        });
    }

    private void openGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryLauncher.launch(intent);
        }
    }

    private void saveImageToInternalStorage(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

            // Redimensionar imagen para ahorrar espacio
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true);

            File imageFile = new File(getFilesDir(), IMAGE_FILE_NAME);
            FileOutputStream fos = new FileOutputStream(imageFile);
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();

            imgPerfil.setImageBitmap(resizedBitmap);
            Toast.makeText(this, "Imagen guardada correctamente", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al guardar la imagen", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData(); // Recargar datos cuando se regrese de configuraciones
    }
}