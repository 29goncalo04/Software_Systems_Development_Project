package data;

import business.SSHorarios.Turnos.Turno;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

public class TurnoDAO implements Map<String, Turno>{
    private static TurnoDAO singleton = null;

    private TurnoDAO() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS turnos (" +
                "Id VARCHAR(160) PRIMARY KEY, " +
                "Nome VARCHAR(6) NOT NULL, " +
                "Vagas INT NOT NULL, " +
                "Tipo VARCHAR(10) NOT NULL, " +
                "Dia_semana INT NOT NULL, " +
                "HoraInicial TIME NOT NULL, " +
                "HoraFinal TIME NOT NULL, " +
                "Sala VARCHAR(40) NOT NULL, " +
                "Uc VARCHAR(150) NOT NULL)";
            stm.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    public static TurnoDAO getInstance() {
        if (TurnoDAO.singleton == null) {
            TurnoDAO.singleton = new TurnoDAO();
        }
        return TurnoDAO.singleton;
    }

    @Override
    public int size() {
        int size = 0;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM turnos")) {
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
             PreparedStatement pstm = conn.prepareStatement("SELECT Id FROM turnos WHERE Id=?")) {
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
        if (value instanceof Turno) {
            Turno turno = (Turno) value;
            return containsKey(turno.getChave());
        }
        return false;
    }

    public Turno get(Object key) {
        Turno turno = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM turnos WHERE Id=?")) {
            pstm.setString(1, key.toString());
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("Nome");
                    int vagas = rs.getInt("Vagas");
                    String tipo = rs.getString("Tipo");
                    DayOfWeek dia_semana = DayOfWeek.of(rs.getInt("Dia_semana"));
                    LocalTime horaInicial = rs.getTime("HoraInicial").toLocalTime();
                    LocalTime horaFinal = rs.getTime("HoraFinal").toLocalTime();
                    String sala = rs.getString("Sala");
                    String uc = rs.getString("Uc");
                    turno = new Turno(nome, vagas, tipo, dia_semana, horaInicial, horaFinal, sala, uc);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return turno;
    }

    @Override
    public Turno put(String key, Turno turno) {
        Turno previous = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement(
                     "INSERT INTO turnos (Id, Nome, Vagas, Tipo, Dia_semana, HoraInicial, HoraFinal, Sala, Uc) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                             "ON DUPLICATE KEY UPDATE Nome=VALUES(Nome), Vagas=VALUES(Vagas), Tipo=VALUES(Tipo), Dia_semana=VALUES(Dia_semana), HoraInicial=VALUES(HoraInicial), HoraFinal=VALUES(HoraFinal), Sala=VALUES(Sala), Uc=VALUES(Uc)")) {
            pstm.setString(1, key);
            pstm.setString(2, turno.getNome());
            pstm.setInt(3, turno.getVagas());
            pstm.setString(4, turno.getTipo());
            pstm.setInt(5, turno.getDiaSemana().getValue()); 
            pstm.setTime(6, Time.valueOf(turno.getHoraInicial()));
            pstm.setTime(7, Time.valueOf(turno.getHoraFinal()));
            pstm.setString(8, turno.getSala());
            pstm.setString(9, turno.getUC());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return previous;
    }

    @Override
    public Turno remove(Object key) {
        Turno removed = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("DELETE FROM turnos WHERE Id=?")) {
            pstm.setString(1, key.toString());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return removed;
    }

    @Override
    public void putAll(Map<? extends String, ? extends Turno> m) {
        for (Entry<? extends String, ? extends Turno> entry : m.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("TRUNCATE TABLE turnos");
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
         ResultSet rs = stm.executeQuery("SELECT Id FROM turnos")) {
        while (rs.next()) {
            keys.add(rs.getString("Id"));
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    return keys;
    }

    @Override
    public Collection<Turno> values() {
    List<Turno> turnos = new ArrayList<>();
    try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
         Statement stm = conn.createStatement();
         ResultSet rs = stm.executeQuery("SELECT * FROM turnos")) {
        while (rs.next()) {
            turnos.add(new Turno(rs.getString("Nome"), rs.getInt("Vagas"), rs.getString("Tipo"),  DayOfWeek.of(rs.getInt("Dia_semana")), rs.getTime("HoraInicial").toLocalTime(), rs.getTime("HoraFinal").toLocalTime(), rs.getString("Sala"), rs.getString("Uc")));
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    return turnos;
    }

    public Set<Entry<String, Turno>> entrySet() {
    Set<Entry<String, Turno>> entrySet = new HashSet<>();
    try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery("SELECT * FROM turnos")) {
        while (rs.next()) {
            String uc = rs.getString("Uc");
            String nome = rs.getString("Nome");
            String key = uc + "_" + nome;
            int vagas = rs.getInt("Vagas");
            String tipo = rs.getString("Tipo");
            DayOfWeek dia_semana = DayOfWeek.of(rs.getInt("Dia_semana"));
            LocalTime horaInicial = rs.getTime("HoraInicial").toLocalTime();
            LocalTime horaFinal = rs.getTime("HoraFinal").toLocalTime();
            String sala = rs.getString("Sala");
            Turno value = new Turno(nome, vagas, tipo, dia_semana, horaInicial, horaFinal, sala, uc);
            entrySet.add(new AbstractMap.SimpleEntry<>(key, value));
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    return entrySet;
    }
}