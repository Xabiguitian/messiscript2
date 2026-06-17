import {useEffect} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {useParams, Link} from 'react-router';
import {FormattedTime, FormattedMessage} from 'react-intl';
import backend from '../../../backend';
import * as actions from '../actions';
import * as selectors from '../selectors';

const MovieDetails = () => {
    const {movieId} = useParams();
    const dispatch = useDispatch();
    const movie = useSelector(selectors.getMovieDetails);
    const movies = useSelector(selectors.getMovies);
    const billboardMovie = movies ? movies.find(m => m.movieId === Number(movieId)) : null;

    useEffect(() => {
        const getMovieDetails = async () => {
            dispatch(actions.clearMovieDetails());
            const response = await backend.catalogService.getMovieDetails(movieId);
            if (response.ok) {
                dispatch(actions.getMovieDetailsCompleted(response.payload));
            }
        };

        getMovieDetails();

        return () => dispatch(actions.clearMovieDetails());
    }, [movieId, dispatch]);

    if (!movie) {
        return null;
    }

    return (
        <div>
            <Link to="/">&laquo; <FormattedMessage id="project.catalog.SessionDetails.backToBillboard"/></Link>

            <h2 className="mt-3">{movie.title}</h2>

            <table className="table table-sm w-auto mt-3">
                <tbody>
                    <tr>
                        <th scope="row"><FormattedMessage id="project.catalog.MovieDetails.duration"/></th>
                        <td>{movie.duration} min</td>
                    </tr>
                </tbody>
            </table>

            {movie.summary && (
                <p className="mt-2">{movie.summary}</p>
            )}

            {billboardMovie && billboardMovie.sessions.length > 0 && (
                <div className="mt-3">
                    <span className="me-2 fw-bold"><FormattedMessage id="project.catalog.MovieDetails.sessions"/>:</span>
                    <div>
                        {billboardMovie.sessions.map(session => (
                            <Link key={session.id} to={`/catalog/sessions/${session.id}`} className="btn btn-sm btn-outline-primary me-2 mb-1">
                                <FormattedTime value={new Date(session.date)} />
                            </Link>
                        ))}
                    </div>
                </div>
            )}
        </div>
    );
};

export default MovieDetails;
