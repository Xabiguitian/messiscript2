import {appFetch} from './appFetch';

export const getBillboard = (date) => appFetch('GET', `/catalog/billboard?date=${date}`);

export const getMovieDetails = (movieId) => appFetch('GET', `/catalog/movies/${movieId}`);

export const getSessionDetails = (sessionId) => appFetch('GET', `/catalog/sessions/${sessionId}`);
