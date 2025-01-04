package data;

import business.SSHorarios.UCs.UC;

import java.sql.*;
import java.util.*;

public class UcDAO implements Map<String, UC>{
    private static UcDAO singleton = null;

    private UcDAO() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS ucs (" +
                "Nome VARCHAR(90) PRIMARY KEY, " +
                "Preferencias VARCHAR(255) NOT NULL, " +
                "Semestre INT NOT NULL, " + 
                "Vagas INT NOT NULL)";
            stm.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    public static UcDAO getInstance() {
        if (UcDAO.singleton == null) {
            UcDAO.singleton = new UcDAO();
        }
        return UcDAO.singleton;
    }

    @Override
    public int size() {
        int size = 0;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM ucs")) {
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
             PreparedStatement pstm = conn.prepareStatement("SELECT Nome FROM ucs WHERE Nome=?")) {
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
        if (value instanceof UC) {
            UC uc = (UC) value;
            return containsKey(uc.getNome());
        }
        return false;
    }

    public UC get(Object key) {
        UC uc = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM ucs WHERE Nome=?")) {
            pstm.setString(1, key.toString());
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("Nome");
                    String preferenciasString = rs.getString("Preferencias");
                    String[] preferencias = preferenciasString != null ? preferenciasString.split(",") : new String[0];
                    int semestre = rs.getInt("Semestre");
                    int vagas = rs.getInt("Vagas");
                    uc = new UC(nome, preferencias, semestre, vagas);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return uc;
    }

    @Override
    public UC put(String key, UC uc) {
        UC previous = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement(
                     "INSERT INTO ucs (Nome, Preferencias, Semestre, Vagas) " +
                             "VALUES (?, ?, ?, ?) " +
                             "ON DUPLICATE KEY UPDATE Nome=VALUES(Nome), Preferencias=VALUES(Preferencias), Semestre=VALUES(Semestre), Vagas=VALUES(Vagas)")) {
            pstm.setString(1, uc.getNome());
            pstm.setString(2, String.join(",", uc.getPreferencias()));
            pstm.setInt(3, uc.getSemestre());
            pstm.setInt(4, uc.getVagas());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return previous;
    }

    @Override
    public UC remove(Object key) {
        UC removed = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("DELETE FROM ucs WHERE Nome=?")) {
            pstm.setString(1, key.toString());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return removed;
    }

    @Override
    public void putAll(Map<? extends String, ? extends UC> m) {
        for (Entry<? extends String, ? extends UC> entry : m.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("TRUNCATE TABLE ucs");
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
         ResultSet rs = stm.executeQuery("SELECT Nome FROM ucs")) {
        while (rs.next()) {
            keys.add(rs.getString("Nome"));
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    return keys;
    }

    @Override
    public Collection<UC> values() {
    List<UC> ucs = new ArrayList<>();
    try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
         Statement stm = conn.createStatement();
         ResultSet rs = stm.executeQuery("SELECT * FROM ucs")) {
        while (rs.next()) {
            ucs.add(new UC(rs.getString("Nome"), rs.getString("Preferencias") != null ? rs.getString("Preferencias").split(",") : new String[0], rs.getInt("Semestre"), rs.getInt("Vagas")));
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    return ucs;
    }

    public Set<Entry<String, UC>> entrySet() {
        Set<Entry<String, UC>> entrySet = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM ucs")) {
            while (rs.next()) {
                String nome = rs.getString("Nome");
                String preferenciasString = rs.getString("Preferencias");
                String[] preferencias = preferenciasString != null ? preferenciasString.split(",") : new String[0];
                int semestre = rs.getInt("Semestre");
                int vagas = rs.getInt("Vagas");
                UC value = new UC(nome, preferencias, semestre, vagas);
                entrySet.add(new AbstractMap.SimpleEntry<>(nome, value));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return entrySet;
    }
}