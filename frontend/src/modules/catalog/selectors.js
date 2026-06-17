const getModuleState = state => state.catalog;

export const getBillboardDate = state => getModuleState(state).billboardDate;

export const getMovies = state => {
    const sessions = getModuleState(state).sessions;
    if (!sessions) {
        return null;
    }

    const moviesMap = new Map();
    sessions.forEach(session => {
        let movie = moviesMap.get(session.movieId);
        if (!movie) {
            movie = {
                movieId: session.movieId,
                movieTitle: session.movieTitle,
                sessions: []
            };
            moviesMap.set(session.movieId, movie);
        }
        // Save the whole session object so we can use price, roomName, etc. later if needed
        movie.sessions.push(session); 
    });

    const movies = Array.from(moviesMap.values());
    movies.sort((a, b) => a.movieTitle.localeCompare(b.movieTitle));
    
    // Sort sessions in each movie by date
    movies.forEach(movie => {
        movie.sessions.sort((a, b) => new Date(a.date) - new Date(b.date));
    });

    return movies;
};

export const getMovieDetails = state => getModuleState(state).movieDetails;

export const getSessionDetails = state => getModuleState(state).sessionDetails;
