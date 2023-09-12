package com.progeto.topografiaa;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Double> points = new ArrayList<>();
    private EditText angleInput; //
    private EditText choiceInput;
    private EditText erropValue;
    private EditText azimuteValue;
    private TextView resultTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        angleInput = findViewById(R.id.angleInput);
        choiceInput = findViewById(R.id.choiceInput);
        erropValue = findViewById(R.id.erropValue);
        azimuteValue = findViewById(R.id.azimuteValue);
        resultTextView = findViewById(R.id.resultTextView);

        Button addAngleButton = findViewById(R.id.addAngleButton);
        addAngleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAngle();
            }
        });

        Button calculateButton = findViewById(R.id.calculateButton);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performCalculations();
            }
        });
    }

    private void addAngle() {
        String angleText = angleInput.getText().toString();

        if (!angleText.isEmpty()) {
            try {
                double angle = Double.parseDouble(angleText);
                points.add(angle);
                angleInput.setText(""); // Limpa o campo após adicionar o ângulo
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Ângulo inválido", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Digite um ângulo válido", Toast.LENGTH_SHORT).show();
        }
    }

    private void performCalculations() {
        int numAngles = points.size();
        double totalSumDegrees = 0;

        for (Double angle : points) {
            totalSumDegrees += angle;
        }

        String resultText = "Total de ângulos digitados: " + numAngles + "\nSoma dos ângulos inseridos: " + totalSumDegrees + "\n";

        String choice = choiceInput.getText().toString();

        if (choice.equalsIgnoreCase("i")) {
            double internalSum = 180 * (numAngles - 2);
            resultText += "Soma dos ângulos internos: " + internalSum + " Minutos\n";
            double aang = totalSumDegrees - internalSum;
            double erro_ACEITAVEL = aang - numAngles;
            String erropInput = erropValue.getText().toString();
            double errop = Double.parseDouble(erropInput);
            double erro = Math.sqrt(numAngles) * errop;
            double C = (-aang) / numAngles;

            List<Double> adjustedAngles = new ArrayList<>();

            for (Double angle : points) {
                adjustedAngles.add(angle - C);
            }

            double azimute = Double.parseDouble(azimuteValue.getText().toString());
            double testeFinal = azimute;
            List<Double> azimutes = new ArrayList<>();

            for (int i = 1; i < numAngles - 1; i++) {
                azimute = azimute + (180 - adjustedAngles.get(i + 1));

                if (azimute > 360) {
                    azimute -= 180;
                    azimute -= testeFinal;
                }

                azimutes.add(azimute);
            }

            resultText += "Azimutes calculados: " + azimutes + "\n";

            if (erro_ACEITAVEL > erro) {
                resultText += "Erro não aceitável\n";
            }

            resultText += "Diferença entre soma interna e total: " + aang + " graus";
        } else if (choice.equalsIgnoreCase("e")) {
            double externalSum = 180 * (numAngles + 2);
            resultText += "Soma dos ângulos externos: " + externalSum + "\n";
            double aang = totalSumDegrees - externalSum;
            double erro_ACEITAVEL = aang - numAngles;
            String erropInput = erropValue.getText().toString();
            double errop = Double.parseDouble(erropInput);
            double erro = Math.sqrt(numAngles) * errop;
            double C = (-aang) / numAngles;

            List<Double> adjustedAngles = new ArrayList<>();

            for (Double angle : points) {
                adjustedAngles.add(angle - C);
            }

            double azimute = Double.parseDouble(azimuteValue.getText().toString());
            double testeFinal = azimute;
            List<Double> azimutes = new ArrayList<>();

            for (int i = 1; i < numAngles - 1; i++) {
                azimute = azimute + (adjustedAngles.get(i + 1) - 180);

                if (azimute > 360) {
                    azimute -= 180;
                    azimute -= testeFinal;
                }

                azimutes.add(azimute);
            }

            resultText += "Azimutes calculados: " + azimutes + "\n";

            if (erro_ACEITAVEL > erro) {
                resultText += "Erro não aceitável\n";
            }

            resultText += "Diferença entre soma externa e total: " + aang + " graus";
        } else {
            resultText += "Escolha inválida. Os ângulos devem ser internos (i) ou externos (e).";
        }

        resultTextView.setText(resultText);
    }
}
