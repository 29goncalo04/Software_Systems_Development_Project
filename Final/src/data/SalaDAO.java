package data;

import business.SSHorarios.Turnos.Sala;

import java.sql.*;
import java.util.*;

public class SalaDAO implements Map<String, Sala> {
    private static SalaDAO singleton = null;

    private SalaDAO() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS salas (" +
                         "Nome varchar(20) NOT NULL PRIMARY KEY, " +
                         "Ocupacao_Max int(4) DEFAULT 0)";
            stm.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    public static SalaDAO getInstance() {
        if (SalaDAO.singleton == null) {
            SalaDAO.singleton = new SalaDAO();
        }
        return SalaDAO.singleton;
    }

    @Override
    public int size() {
        int size = 0;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM salas")) {
            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        boolean exists;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT Nome FROM salas WHERE Nome=?")) {
            pstm.setString(1, key.toString());
            try (ResultSet rs = pstm.executeQuery()) {
                exists = rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return exists;
    }

    @Override
    public boolean containsValue(Object value) {
        if (value instanceof Sala) {
            Sala sala = (Sala) value;
            return containsKey(sala.getNome());
        }
        return false;
    }

    public Sala get(Object key) {
        Sala sala = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM salas WHERE Nome=?")) {
            pstm.setString(1, key.toString());
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("Nome");
                    int ocupacaoMaxima = rs.getInt("Ocupacao_Max");
                    sala = new Sala(nome, ocupacaoMaxima);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return sala;
    }   

    @Override
    public Sala put(String key, Sala sala) {
        Sala previous = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement(
                     "INSERT INTO salas (Nome, Ocupacao_Max) " +
                             "VALUES (?, ?) " +
                             "ON DUPLICATE KEY UPDATE Nome=VALUES(Nome), Ocupacao_Max=VALUES(Ocupacao_Max)")) {
            pstm.setString(1, sala.getNome());
            pstm.setInt(2, sala.getOcupacaoMaxima());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return previous;
    }

    @Override
    public Sala remove(Object key) {
        Sala removed = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("DELETE FROM salas WHERE Nome=?")) {
            pstm.setString(1, key.toString());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return removed;
    }

    @Override
    public void putAll(Map<? extends String, ? extends Sala> m) {
        for (Entry<? extends String, ? extends Sala> entry : m.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("TRUNCATE TABLE salas");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public Set<String> keySet() {
    Set<String> keys = new HashSet<>();
    try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
         Statement stm = conn.createStatement();
         ResultSet rs = stm.executeQuery("SELECT Nome FROM salas")) {
        while (rs.next()) {
            keys.add(rs.getString("Nome"));
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    return keys;
    }

    @Override
    public Collection<Sala> values() {
    List<Sala> salas = new ArrayList<>();
    try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
         Statement stm = conn.createStatement();
         ResultSet rs = stm.executeQuery("SELECT * FROM salas")) {
        while (rs.next()) {
            salas.add(new Sala(rs.getString("Nome"), rs.getInt("Ocupacao_Max")));
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    return salas;
    }

    public Set<Entry<String, Sala>> entrySet() {
    Set<Entry<String, Sala>> entrySet = new HashSet<>();
    try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
         Statement stm = conn.createStatement();
         ResultSet rs = stm.executeQuery("SELECT * FROM salas")) {
        while (rs.next()) {
            String key = rs.getString("Nome");
            Sala value = new Sala(key, rs.getInt("Ocupacao_Max"));
            entrySet.add(new AbstractMap.SimpleEntry<>(key, value));
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    return entrySet;
    }

}