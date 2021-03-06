package com.ufpb.projetoAluguelVeiculo.utils;

import java.util.Base64;
import java.io.FileWriter;
import org.joda.time.DateTime;
import org.joda.time.Minutes;

public class Static {

    private static final String LOJA_DATABASE_URL = "src/main/java/com/ufpb/projetopoo/repositories/loja_database.txt";
    public static final String LN = System.lineSeparator();

    /**
     * input model "11/11/1111 11:11"
     * 
     * @return DataTime(aaaa, mm, dd, h, m)
     */
    public static final DateTime stringParseData(String data) {
        data.trim();
        String[] k = data.split(" ");
        String[] dataArr = k[0].split("/");
        String[] horario = k[1].split(":");

        int dia = Integer.parseInt(dataArr[0]);
        int mes = Integer.parseInt(dataArr[1]);
        int ano = Integer.parseInt(dataArr[2]);

        int horas = Integer.parseInt(horario[0]);
        int minutos = Integer.parseInt(horario[1]);

        DateTime retorno = new DateTime(ano, mes, dia, horas, minutos);

        return retorno;
    }
    

    public static int numHorasParaPagamento(int minutos) {
        int horas = minutos / 60;
        Boolean x = minutos % 60 == 0;
        int retorno = x == true ? horas : horas + 1;
        return retorno;
    }

    public static int minutosEntreDatas(DateTime data1, DateTime data2) {
        int min = Minutes.minutesBetween(data1, data2).getMinutes();
        return min;
    }

    public static String encode(String str) {
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encoded = encoder.encode(str.getBytes());
        return new String(encoded);
    }

    public static String decode(String str) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decoded = decoder.decode(str);
        return new String(decoded);
    }

    public static boolean isEmpty(String s) {
        if (s == null)
            return true;
        if (s.equals(""))
            return true;
        return false;
    }

    public static String fixed2f(double dinheiro) {
        return String.format("%.2f", dinheiro).replace(",", ".");
    }

    public static void cleanData() {
        try {
            FileWriter dataToSave = new FileWriter(LOJA_DATABASE_URL);
            dataToSave.write("");
            dataToSave.close();
        } catch (Exception e) {
        }
    }
}
