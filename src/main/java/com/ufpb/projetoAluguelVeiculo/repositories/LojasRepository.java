package com.ufpb.projetoAluguelVeiculo.repositories;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.util.ArrayList;

import com.ufpb.projetoAluguelVeiculo.entities.Aluguel;
import com.ufpb.projetoAluguelVeiculo.entities.Cliente;
import com.google.gson.Gson;

import java.util.Scanner;

import com.ufpb.projetoAluguelVeiculo.entities.Loja;

public class LojasRepository {
    private static final String LOJA_DATABASE_URL = "src/main/java/com/ufpb/projetoAluguelVeiculo/utils/loja_database.txt";
    private ArrayList<Loja> lojas;

    public LojasRepository() {
        this.lojas = new ArrayList<Loja>();
    }

    // Save and recovery

    public static String recoveryData() {
        try {
            BufferedReader myObj = new BufferedReader(
                new FileReader(LOJA_DATABASE_URL)
            );
            Scanner myReader = new Scanner(myObj);
            String data = "";
            
            while (myReader.hasNextLine()) {
                data += myReader.nextLine() + "\n";
            }

            myReader.close();
            return data;
        } catch (FileNotFoundException e) { 
            throw new RuntimeException("Arquivo não encontrado");
        }
    }

    public void importLojas() {
        try {
            String jsonImportado = recoveryData();

            Gson gson = new Gson();
            Scanner sc = new Scanner(jsonImportado);
            this.lojas = new ArrayList<Loja>();
            while (sc.hasNextLine()) {
                this.lojas.add(gson.fromJson(
                    sc.nextLine(), Loja.class
                ));
            }
            sc.close();

        } catch (Exception e) { }
    }

    private void updateDataBase() {
        Gson gson = new Gson();

        StringBuilder sb = new StringBuilder();

        for (Loja loja : this.lojas) {
            sb.append(gson.toJson(loja) + "\n");
        }
        saveData(sb.toString());
    }

    private void saveData(String json) {
        try {
            FileWriter dataToSave = new FileWriter(LOJA_DATABASE_URL);
            dataToSave.write(json);
            dataToSave.close();
        } catch (Exception e) { }
    }

    // Lojas

    public Loja save(Loja loja) {
        importLojas();
        if (!isDuplicadoLoja(loja)) {
            this.lojas.add(loja);

            updateDataBase();
            return loja;
        }
        throw new IndexOutOfBoundsException("Loja já cadastrada no sistema.");
    }

    public ArrayList<Loja> findAll() {
        importLojas();
        return this.lojas;
    }

    

    public Loja findByCnpj(String cnpj) {
        importLojas();
        for (Loja item : lojas) {
            if (item.getCnpj().equals(cnpj))
                return item;
        }
        throw new IndexOutOfBoundsException("cnpj não encontrado.");
    }

    private boolean isDuplicadoLoja(Loja loja) {
        importLojas();
        for (Loja item : this.lojas) {
            if (loja.getCnpj().equals(item.getCnpj())) {
                return true;
            }
        }
        return false;
    }

    public boolean deleteByCNPJ(String cnpj) {
        importLojas();
        for (Loja loja : this.lojas) {
            if (loja.getCnpj().equals(cnpj)) {
                this.lojas.remove(loja);
                updateDataBase();
                return true;
            }
        }
        return false;
    }

    // Cliente métodos

    public Cliente saveCliente(Cliente cliente, String cnpj) {
        importLojas();
        Gson gson = new Gson();
        for (Loja loja : this.lojas) {
            if (loja.getCnpj().equalsIgnoreCase(cnpj)) {
                
                Loja l = loja;
                ArrayList<Cliente> clientesLoja = loja.getClientes();
                clientesLoja.add(cliente);
                
                l.setClientes(clientesLoja);
                deleteByCNPJ(cnpj);
                save(l);

                updateDataBase();
                return cliente;
            }
        }
        throw new RuntimeException("Loja não cadastrada.");
    }

    public ArrayList<Cliente> findClientsByCnpj(String cnpj) {
        importLojas();

        for (Loja loja : this.lojas) {
            if (loja.getCnpj().equals(cnpj)) {
                return loja.getClientes();
            }
        }
        throw new RuntimeException("Loja não cadastrada.");
    }

    public Cliente findClienteByCpf(String cpf, String cnpj) {
        importLojas();
        ArrayList<Cliente> clientes = findClientsByCnpj(cnpj);
        for (Cliente c : clientes) {
            if (c.getCpf().equals(cpf)) {
                return c;
            }
        }
        throw new IndexOutOfBoundsException();
    }

    public boolean deleteClienteByCpf(String cpf, String cnpj) {
        importLojas();
        Loja l = findByCnpj(cnpj);
        for (Cliente c : l.getClientes()) {
            if (c.getCpf().equals(cpf)) {
                l.removeCliente(c);
                updateDataBase();
                return true;
            }
        }
        return false;
    }

    // Aluguel

    public Aluguel saveAluguel(Aluguel aluguel) {
        importLojas();
        Loja loja = findByCnpj(aluguel.getCnpjLoja());
        loja.addAluguel(aluguel);
        updateDataBase();

        return aluguel;
    }

    // findAluguelByCnpj
    public ArrayList<Aluguel> findAlugueisByCnpj(String cnpj) {
        importLojas();
        return findByCnpj(cnpj).getAlugueis();
    }

    // findAluguelById
    public Aluguel findAluguelById(String id, String cnpj) {
        importLojas();
        ArrayList<Aluguel> alugueis = findAlugueisByCnpj(cnpj);
        for (Aluguel aluguel : alugueis) {
            if (aluguel.getId().equals(id)) {
                return aluguel;
            }
        }
        throw new IndexOutOfBoundsException("aluguel nao encontrado");
    }

    // deleteAluguelById
    public boolean deleteAluguelById(String id, String cnpj) {
        importLojas();
        Loja l = findByCnpj(cnpj);
        for (Aluguel a : l.getAlugueis()) {
            if (a.getId().equals(id)) {
                l.removeAluguel(a);
                updateDataBase();
                return true;
            }
        }
        return false;
    }

    // findAluguelByCpf
    public ArrayList<Aluguel> findAlugueisByCpf(String cpf, String cnpj) {
        importLojas();
        Loja l = findByCnpj(cnpj);
        ArrayList<Aluguel> retorno = new ArrayList<Aluguel>();
        for (Aluguel alu : l.getAlugueis()) {
            if (alu.getCpfCliente().equals(cpf)) {
                retorno.add(alu);
            }
        }
        return retorno;
    }

    public void deleteAll() {

        this.lojas = new ArrayList<Loja>();
        updateDataBase();
    }
}
