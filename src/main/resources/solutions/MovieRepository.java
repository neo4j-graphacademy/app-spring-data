import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

interface MovieRepository extends Neo4jRepository<Movie, String> {

    @Query("MATCH (m:Movie)<-[r:ACTED_IN]-(p:Person)" +
            "RETURN m, collect(r), collect(p) LIMIT 20;")
    Iterable<Movie> findMoviesSubset();

    @Query("MATCH (m:Movie)<-[r:ACTED_IN]-(p:Person {name: $name})" +
            "RETURN m, collect(r), collect(p);")
    Iterable<Movie> findMoviesByPerson(String name);

    @Query("MATCH (m:Movie {movieId: $movieId}) " +
            "SET m.imdbVotes = coalesce(m.imdbVotes+1, 1) " +
            "RETURN m;")
    Movie incrementImdbVotes(String movieId);

    Iterable<MovieProjection> findAllMovieProjectionsBy();

    @Query("MATCH (m:Movie)<-[r:ACTED_IN]-(p:Person) " +
            "RETURN m, COUNT(p) AS castSize LIMIT 10;")
    Iterable<MovieDTOProjection> findAllDTOProjectionsWithCustomQuery();
}