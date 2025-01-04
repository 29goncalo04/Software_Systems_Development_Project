package data;

import business.SSUtilizadores.Utilizadores.Utilizador;

import java.sql.*;
import java.util.*;

public class UtilizadorDAO implements Map<Integer, Utilizador> {
    private static UtilizadorDAO singleton = null;

    private UtilizadorDAO() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS utilizadores (" +
                         "Email VARCHAR(50) NOT NULL, " +
                         "Password VARCHAR(50), " +
                         "Nome VARCHAR(100), " +
                         "Numero INT NOT NULL PRIMARY KEY, " +
                         "Media FLOAT, " +
                         "Estatuto VARCHAR(20), " +
                         "Situacao VARCHAR(20), " +
                         "Semestre INT, " +
                         "Ucs TEXT NOT NULL)";
            stm.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao inicializar a tabela de utilizadores", e);
        }
    }

    public static UtilizadorDAO getInstance() {
        if (singleton == null) {
            singleton = new UtilizadorDAO();
        }
        return singleton;
    }

    @Override
    public int size() {
        int size = 0;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT COUNT(*) FROM utilizadores")) {
            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao obter o tamanho da tabela de utilizadores", e);
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        boolean exists = false;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT Numero FROM utilizadores WHERE Numero = ?")) {
            pstm.setInt(4, (int) key);
            try (ResultSet rs = pstm.executeQuery()) {
                exists = rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar a existência do utilizador", e);
        }
        return exists;
    }

    @Override
    public boolean containsValue(Object value) {
        if (value instanceof Utilizador) {
            Utilizador utilizador = (Utilizador) value;
            return containsKey(utilizador.getNumero());
        }
        return false;
    }

    @Override
    public Utilizador get(Object key) {
        Utilizador utilizador = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM utilizadores WHERE Numero=?")) {
            pstm.setInt(1, (int) key);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    String email = rs.getString("Email");
                    String password = rs.getString("Password");
                    String nome = rs.getString("Nome");
                    int numero = rs.getInt("Numero");
                    float media = rs.getFloat("Media");
                    String estatuto = rs.getString("Estatuto");
                    String situacao = rs.getString("Situacao");
                    int semestre = rs.getInt("Semestre");
                    String ucsString = rs.getString("Ucs");
                    ArrayList<String> ucs = ucsString != null ? new ArrayList<>(Arrays.asList(ucsString.split(","))) : new ArrayList<>();
                    utilizador = new Utilizador(email, password, nome, numero, media, estatuto, situacao, semestre, ucs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao obter o utilizador", e);
        }
        return utilizador;
    }

    @Override
    public Utilizador put(Integer key, Utilizador utilizador) {
        Utilizador previous = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD)) {
            try (PreparedStatement pstm = conn.prepareStatement(
                    "INSERT INTO utilizadores (Email, Password, Nome, Numero, Media, Estatuto, Situacao, Semestre, Ucs) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                            "ON DUPLICATE KEY UPDATE Email=VALUES(Email), Password=VALUES(Password), Nome=VALUES(Nome), " +
                            "Numero=VALUES(Numero), Media=VALUES(Media), Estatuto=VALUES(Estatuto), " +
                            "Situacao=VALUES(Situacao), Semestre=VALUES(Semestre), Ucs=VALUES(Ucs)")) {
                pstm.setString(1, utilizador.getEmail());
                pstm.setString(2, utilizador.getPassword());
                pstm.setString(3, utilizador.getNome());
                pstm.setInt(4, utilizador.getNumero());
                pstm.setDouble(5, utilizador.getMedia());
                pstm.setString(6, utilizador.getEstatuto());
                pstm.setString(7, utilizador.getSituacao());
                pstm.setInt(8, utilizador.getSemestre());
                pstm.setString(9, String.join(",", utilizador.getUcs()));
                pstm.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return previous;
    }

    @Override
    public Utilizador remove(Object key) {
        Utilizador removed = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD)) {
            try (PreparedStatement pstm = conn.prepareStatement("DELETE FROM utilizadores WHERE Numero = ?")) {
                pstm.setString(1, key.toString());
                pstm.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return removed;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Utilizador> m) {
        for (Entry<? extends Integer, ? extends Utilizador> entry : m.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("TRUNCATE TABLE utilizadores");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao limpar a tabela de utilizadores", e);
        }
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> keys = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT Numero FROM utilizadores")) {
            while (rs.next()) {
                keys.add(rs.getInt("Numero"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao obter as chaves da tabela de utilizadores", e);
        }
        return keys;
    }

    @Override
    public Collection<Utilizador> values() {
        List<Utilizador> users = new ArrayList<>();
        for (Integer numero : keySet()) {
            users.add(this.get(numero));
        }
        return users;
    }

    @Override
    public Set<Entry<Integer, Utilizador>> entrySet() {
        Set<Entry<Integer, Utilizador>> entrySet = new HashSet<>();
        for (Integer numero : keySet()) {
            entrySet.add(new AbstractMap.SimpleEntry<>(numero, this.get(numero)));
        }
        return entrySet;
    }
}
