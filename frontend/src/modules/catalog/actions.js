import * as actionTypes from './actionTypes';

export const getBillboardCompleted = sessions => ({
    type: actionTypes.GET_BILLBOARD_COMPLETED,
    sessions
});

export const clearBillboard = date => ({
    type: actionTypes.CLEAR_BILLBOARD,
    date
});

export const getMovieDetailsCompleted = movie => ({
    type: actionTypes.GET_MOVIE_DETAILS_COMPLETED,
    movie
});

export const clearMovieDetails = () => ({
    type: actionTypes.CLEAR_MOVIE_DETAILS
});

export const getSessionDetailsCompleted = session => ({
    type: actionTypes.GET_SESSION_DETAILS_COMPLETED,
    session
});

export const clearSessionDetails = () => ({
    type: actionTypes.CLEAR_SESSION_DETAILS
});
