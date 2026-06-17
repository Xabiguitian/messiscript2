import {Link} from 'react-router';
import {FormattedMessage, FormattedTime} from 'react-intl';

const Movies = ({movies}) => {
    if (!movies || movies.length === 0) {
        return (
            <div className="alert alert-info">
                <FormattedMessage id="project.catalog.Billboard.noMoviesFound"/>
            </div>
        );
    }

    return (
        <div className="list-group">
            {movies.map(movie => (
                <div key={movie.movieId} className="list-group-item d-flex flex-column align-items-start">
                    <h5 className="mb-1">
                        <Link className="movie-link" to={`/catalog/movies/${movie.movieId}`}>
                            {movie.movieTitle}
                        </Link>
                    </h5>
                    <div>
                        <span className="me-2">Sesiones:</span>
                        {movie.sessions.map(session => (
                            <Link key={session.id} to={`/catalog/sessions/${session.id}`} className="session-link btn btn-sm btn-outline-primary me-2 mb-1">
                                <FormattedTime value={new Date(session.date)} />
                            </Link>
                        ))}
                    </div>
                </div>
            ))}
        </div>
    );
};

export default Movies;
