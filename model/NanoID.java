package model;

import java.security.SecureRandom;

public class NanoID {

    private static final String ALFABETO = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-";
    private static final SecureRandom random = new SecureRandom();

    public static String gerarCodigo(int tamanho) {
        StringBuilder id = new StringBuilder(tamanho);

        for (int i = 0; i < tamanho; i++) {
            int index = random.nextInt(ALFABETO.length());
            id.append(ALFABETO.charAt(index));
        }

        return id.toString();
    }
}
