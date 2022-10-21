package it.prova.gestioneimpiegatojdbc.dao.impiegato;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.prova.gestioneimpiegatojdbc.dao.AbstractMySQLDAO;
import it.prova.gestioneimpiegatojdbc.model.Compagnia;
import it.prova.gestioneimpiegatojdbc.model.Impiegato;

public class ImpiegatoDAOImpl extends AbstractMySQLDAO implements ImpiegatoDAO {

	public ImpiegatoDAOImpl(Connection connection) {
		super(connection);
	}

	@Override
	public List<Impiegato> list() throws Exception {

		if (isNotActive()) {
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");
		}
		ArrayList<Impiegato> result = new ArrayList<Impiegato>();

		try (Statement ps = connection.createStatement();
				ResultSet rs = ps
						.executeQuery("select * from impiegato i inner join compagnia c on c.id=i.compagnia_id ; ")) {

			while (rs.next()) {
				Impiegato temp = new Impiegato();
				temp.setNome(rs.getString("nome"));
				temp.setCognome(rs.getString("cognome"));
				temp.setCodiceFiscale(rs.getString("codicefiscale"));
				temp.setDataNascita(rs.getDate("datanascita"));
				temp.setDataAssunzione(rs.getDate("dataassunzione"));
				temp.setId(rs.getLong("i.id"));

				Compagnia tempo = new Compagnia();
				tempo.setRagioneSociale(rs.getString("ragionesociale"));
				tempo.setFatturatoAnnuo(rs.getInt("fatturatoannuo"));
				tempo.setDataFondazione(rs.getDate("datafondazione"));
				tempo.setId(rs.getLong("c.id"));

				temp.setCompagnia(tempo);
				result.add(temp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	// metodo simile a findById
	@Override
	public Impiegato get(Long idInput) throws Exception {

		if (isNotActive()) {
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");
		}
		if (idInput == null || idInput < 1) {
			throw new Exception("Valore di input non ammesso.");
		}
		Impiegato result = null;

		try (PreparedStatement ps = connection.prepareStatement("select * from impiegato where id=?")) {
			ps.setLong(1, idInput);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {

					Impiegato temp = new Impiegato();
					temp.setNome(rs.getString("nome"));
					temp.setCognome(rs.getString("cognome"));
					temp.setCodiceFiscale(rs.getString("codicefiscale"));
					temp.setDataNascita(rs.getDate("datanascita"));
					temp.setDataAssunzione(rs.getDate("dataassunzione"));
					temp.setId(rs.getLong("i.id"));

				} else {
					result = null;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int insert(Impiegato input) throws Exception {

		if (isNotActive()) {
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");
		}
		if (input == null) {
			throw new Exception("Valore di input non ammesso.");
		}
		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"insert into impiegato (nome, cognome, codicefiscale, datanascita, dataassunzione, compagnia_id) values (?, ?, ?, ?, ?, ?);")) {
			ps.setString(1, input.getNome());
			ps.setString(2, input.getCognome());
			ps.setString(3, input.getCodiceFiscale());
			ps.setDate(4, new java.sql.Date(input.getDataNascita().getTime()));
			ps.setDate(5, new java.sql.Date(input.getDataAssunzione().getTime()));
			ps.setLong(6, input.getCompagnia().getId());

			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int update(Impiegato input) throws Exception {

		if (isNotActive()) {
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");
		}
		if (input == null || input.getId() == null || input.getId() < 1) {
			throw new Exception("Valore di input non ammesso.");
		}
		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"update impiegato set nome=?, cognome=?, codiceFiscale=?, dataNascita=?, dataAssunzione=? where id=?;")) {
			ps.setString(1, input.getNome());
			ps.setString(2, input.getCognome());
			ps.setString(3, input.getCodiceFiscale());
			ps.setDate(4, new java.sql.Date(input.getDataNascita().getTime()));
			ps.setDate(5, new java.sql.Date(input.getDataAssunzione().getTime()));

			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int delete(Impiegato input) throws Exception {

		if (isNotActive()) {
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");
		}
		if (input == null || input.getId() == null || input.getId() < 1) {
			throw new Exception("Valore di input non ammesso.");
		}
		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement("delete from impiegato where id=?")) {
			ps.setLong(1, input.getId());

			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public List<Impiegato> findByExample(Impiegato input) throws Exception {
		if (isNotActive()) {
			throw new Exception("Connessione interrotta. Impossibile fare operazioni DAO");
		}
		String query = "select* from Impiegato  where 1=1 ";
		List<Impiegato> result = new ArrayList<Impiegato>();

		if (input == null) {
			throw new Exception("Non è ammesso un impiegato null");
		}

		if (input.getNome() != null && !input.getNome().isBlank()) {
			query += " and Nome like '" + input.getNome() + "%' ";
		}

		if (input.getCognome() != null && !input.getCognome().isBlank()) {
			query += " and cognome like '" + input.getCognome() + "%'";
		}

		if (input.getCodiceFiscale() != null && !input.getCodiceFiscale().isBlank()) {
			query += " and codicefiscale like '" + input.getCodiceFiscale() + "%'";
		}

		if (input.getDataNascita() != null) {
			query += "and datanascita  >'" + new java.sql.Date(input.getDataNascita().getTime()) + "'";
		}
		if (input.getDataAssunzione() != null) {
			query += "and dataassunzione  >'" + new java.sql.Date(input.getDataAssunzione().getTime()) + "'";
		}

		query += ";";

		try (Statement state = connection.createStatement()) {
			try (ResultSet rs = state.executeQuery(query);) {
				while (rs.next()) {
					Impiegato temp = new Impiegato();
					temp.setNome(rs.getString("nome"));
					temp.setCognome(rs.getString("cognome"));
					temp.setCodiceFiscale(rs.getString("codicefiscale"));
					temp.setDataNascita(rs.getDate("datanascita"));
					temp.setDataAssunzione(rs.getDate("dataassunzione"));
					temp.setId(rs.getLong("i.id"));

					result.add(temp);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;

	}

	@Override
	public List<Impiegato> findAllByCompagnia(Compagnia compagniaInput) throws Exception {
		if (isNotActive()) {
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");
		}
		if (compagniaInput == null) {
			throw new Exception("Valore di input non ammesso.");
		}

		List<Impiegato> result = new ArrayList<Impiegato>();

		try (PreparedStatement ps = connection
				.prepareStatement("select* from impiegato i left join compagnia c on i.compagnia_id=?;")) {
			ps.setLong(1, compagniaInput.getId());
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Impiegato temp = new Impiegato();
					temp.setNome(rs.getString("nome"));
					temp.setCognome(rs.getString("cognome"));
					temp.setCodiceFiscale(rs.getString("codicefiscale"));
					temp.setDataNascita(rs.getDate("datanascita"));
					temp.setDataAssunzione(rs.getDate("dataassunzione"));
					temp.setId(compagniaInput.getId());

					result.add(temp);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
			return result;

		}

	}

	@Override
	public int countByDataFondazioneCompagniaGreaterThan(Date dataInput) throws Exception {
		if (isNotActive()) {
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");
		}
		if (dataInput == null) {
			throw new Exception("Valore di input non ammesso.");
		}

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"select count(*) from impiegato i inner join compagnia c on c.id = i.compagnia_id where c.datafondazione > ?")) {

			ps.setDate(1, new java.sql.Date(dataInput.getTime()));
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = rs.getInt("count(*)");
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public List<Impiegato> findAllErroriAssunzione() throws Exception {

		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		ArrayList<Impiegato> result = new ArrayList<Impiegato>();
		try (PreparedStatement ps = connection.prepareStatement(
				"select * from impiegato i inner join compagnia c on c.id = i.compagnia_id where i.dataassunzione < c.datafondazione")) {

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Impiegato temp = new Impiegato();
					temp = new Impiegato();
					temp.setNome(rs.getString("nome"));
					temp.setCognome(rs.getString("cognome"));
					temp.setCodiceFiscale(rs.getString("codicefiscale"));
					temp.setDataNascita(rs.getDate("datanascita"));
					temp.setDataAssunzione(rs.getDate("dataassunzione"));

					result.add(temp);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

}
