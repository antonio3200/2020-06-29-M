package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Arco;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Director> getDirectorPerAnno(int anno,Map<Integer,Director> idMap){
		String sql="SELECT distinct d.id AS id,d.first_name AS nome, d.last_name AS cognome "
				+ "FROM directors d, movies m, movies_directors md "
				+ "WHERE d.id=md.director_id "
				+ "AND m.id=md.movie_id "
				+ "AND m.year=? "
				+ "GROUP BY id,nome,cognome "
				+ "ORDER BY id";
		List<Director> result= new ArrayList<>();
		Connection conn= DBConnect.getConnection();
		try {
			PreparedStatement st= conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs= st.executeQuery();
			while(rs.next()) {
				int id= rs.getInt("id");
				String nome= rs.getString("nome");
				String cognome= rs.getString("cognome");
				Director d = new Director(id,nome,cognome);
				result.add(d);
				idMap.put(id, d);
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("SQL ERROR");
		}
		return result;
	}
	
	public List<Arco> getArco(int anno, Map<Integer,Director> idMap){
		String sql="SELECT md1.director_id AS id1, md2.director_id AS id2 , COUNT(DISTINCT r1.actor_id) AS peso "
				+ "FROM movies_directors md1,movies_directors md2, roles r1, roles r2,movies m1, movies m2 "
				+ "WHERE md1.director_id<md2.director_id "
				+ "AND md1.movie_id=r1.movie_id "
				+ "AND md2.movie_id=r2.movie_id "
				+ "AND m1.id=md1.movie_id "
				+ "AND m2.id=md2.movie_id "
				+ "AND m1.year=m2.year "
				+ "AND m1.year=? "
				+ "AND r1.actor_id=r2.actor_id "
				+ "GROUP BY id1,id2";
		List<Arco> result= new ArrayList<>();
		Connection conn= DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs= st.executeQuery();
			while(rs.next()) {
				int id1=rs.getInt("id1");
				int id2=rs.getInt("id2");
				if(idMap.containsKey(id1) && idMap.containsKey(id2)) {
					int peso=rs.getInt("peso");
					Director d1= idMap.get(id1);
					Director d2= idMap.get(id2);
					Arco a = new Arco(d1,d2,peso);
					result.add(a);
				}
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("SQL ERROR");
		}
		return result;
	} 
	
	
}
