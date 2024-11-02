package org.parcial.services;

import org.parcial.entities.Dna;
import org.parcial.repositories.DnaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DnaService {

    private final DnaRepository dnaRepository;
    private static final int SEQUENCE_LENGTH = 4; // Longitud requerida de la secuencia de letras iguales para detectar un mutante
    private static final int MUTANT_THRESHOLD = 2; // Umbral para identificar a un mutante (2 secuencias mutantes)

    @Autowired
    public DnaService(DnaRepository dnaRepository) {
        this.dnaRepository = dnaRepository; // Inyección de dependencias del repositorio para acceder a la base de datos
    }


    public boolean checkIfMutant(String[] dna) { // Renombrado a "checkIfMutant" para evitar conflicto de nombres
        return countMutantSequences(dna) >= MUTANT_THRESHOLD; // Verifica si hay al menos 2 secuencias mutantes
    }

    /**
     * Analiza el ADN y lo almacena en la base de datos si aún no está registrado.
     * Retorna verdadero si el ADN pertenece a un mutante.
     */
    public boolean analyzeDna(String[] dna) {
        String dnaSequence = String.join(",", dna); // Convierte el arreglo ADN a una cadena para almacenamiento

        Optional<Dna> existingDna = dnaRepository.findByDna(dnaSequence); // Busca si el ADN ya está registrado
        if (existingDna.isPresent()) { // Si existe, retorna su valor de "mutante" guardado
            return existingDna.get().isMutant();
        }

        // Analiza si el ADN es mutante
        boolean isMutant = checkIfMutant(dna);

        // Crea un objeto ADN y lo guarda en la base de datos
        Dna dnaEntity = Dna.builder()
                .dna(dnaSequence)
                .isMutant(isMutant)
                .build();
        dnaRepository.save(dnaEntity);

        return isMutant;
    }

    /**
     * Cuenta las secuencias mutantes en filas, columnas y diagonales.
     * Devuelve la cantidad de secuencias mutantes encontradas.
     */
    private int countMutantSequences(String[] dna) {
        int n = dna.length;
        int mutantCount = 0;

        mutantCount += checkRows(dna, n); // Verifica secuencias en las filas
        if (mutantCount >= MUTANT_THRESHOLD) return mutantCount; // Salida anticipada si se cumple el umbral

        mutantCount += checkColumns(dna, n); // Verifica secuencias en las columnas
        if (mutantCount >= MUTANT_THRESHOLD) return mutantCount;

        mutantCount += checkDiagonals(dna, n); // Verifica secuencias en las diagonales
        return mutantCount;
    }

    /**
     * Verifica si existen secuencias mutantes en las filas del ADN.
     */
    private int checkRows(String[] dna, int n) {
        int count = 0;
        for (int i = 0; i < n; i++) {
            count += hasMutantSequence(dna[i]); // Verifica cada fila en busca de secuencias de longitud 4
            if (count >= MUTANT_THRESHOLD) return count; // Salida anticipada si se cumple el umbral
        }
        return count;
    }

    /**
     * Verifica si existen secuencias mutantes en las columnas del ADN.
     */
    private int checkColumns(String[] dna, int n) {
        int count = 0;
        for (int j = 0; j < n; j++) {
            StringBuilder column = new StringBuilder();
            for (int i = 0; i < n; i++) {
                column.append(dna[i].charAt(j)); // Construye la columna como una cadena
            }
            count += hasMutantSequence(column.toString()); // Verifica la columna en busca de secuencias mutantes
            if (count >= MUTANT_THRESHOLD) return count;
        }
        return count;
    }

    /**
     * Verifica secuencias mutantes en las diagonales principales y secundarias.
     */
    private int checkDiagonals(String[] dna, int n) {
        int count = 0;
        count += scanDiagonal(dna, n, 1, 1); // Diagonal principal (izquierda a derecha)
        if (count >= MUTANT_THRESHOLD) return count;

        count += scanDiagonal(dna, n, 1, -1); // Diagonal secundaria (derecha a izquierda)
        return count;
    }

    /**
     * Escanea una diagonal específica en una dirección y cuenta secuencias mutantes.
     */
    private int scanDiagonal(String[] dna, int n, int dx, int dy) {
        int count = 0;
        for (int i = 0; i <= n - SEQUENCE_LENGTH; i++) {
            for (int j = (dy == 1 ? 0 : SEQUENCE_LENGTH - 1); j <= n - SEQUENCE_LENGTH && j >= 0; j += (dy == 1 ? 1 : -1)) {
                if (hasMutantSequenceInDirection(dna, i, j, dx, dy, n)) { // Verifica diagonal con dirección especificada
                    count++;
                    if (count >= MUTANT_THRESHOLD) return count;
                }
            }
        }
        return count;
    }

    /**
     * Verifica si una secuencia diagonal contiene secuencias de longitud 4 en una dirección específica.
     */
    private boolean hasMutantSequenceInDirection(String[] dna, int x, int y, int dx, int dy, int n) {
        char first = dna[x].charAt(y); // Obtiene el primer carácter de la secuencia
        for (int i = 1; i < SEQUENCE_LENGTH; i++) {
            // Valida límites y compara caracteres en la dirección especificada
            if (x + i * dx >= n || y + i * dy >= n || y + i * dy < 0 || dna[x + i * dx].charAt(y + i * dy) != first) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifica si una cadena tiene una secuencia consecutiva de caracteres idénticos de longitud 4.
     */
    private int hasMutantSequence(String line) {
        int consecutiveCount = 1;
        for (int i = 1; i < line.length(); i++) {
            if (line.charAt(i) == line.charAt(i - 1)) { // Verifica si los caracteres son iguales
                consecutiveCount++;
                if (consecutiveCount == SEQUENCE_LENGTH) { // Si la secuencia es de longitud 4, incrementa el contador
                    return 1;
                }
            } else {
                consecutiveCount = 1; // Reinicia el contador si los caracteres no coinciden
            }
        }
        return 0; // Retorna 0 si no se encuentran secuencias de longitud 4
    }
}
