import {combineReducers} from 'redux';
import * as actionTypes from './actionTypes';

const initialState = {
    sessions: null,
    billboardDate: null,
    movieDetails: null,
    sessionDetails: null
};

const sessions = (state = initialState.sessions, action) => {
    switch (action.type) {
        case actionTypes.GET_BILLBOARD_COMPLETED:
            return action.sessions;
        case actionTypes.CLEAR_BILLBOARD:
            return null;
        default:
            return state;
    }
};

const billboardDate = (state = initialState.billboardDate, action) => {
    switch (action.type) {
        case actionTypes.CLEAR_BILLBOARD:
            return action.date;
        default:
            return state;
    }
};

const movieDetails = (state = initialState.movieDetails, action) => {
    switch (action.type) {
        case actionTypes.GET_MOVIE_DETAILS_COMPLETED:
            return action.movie;
        case actionTypes.CLEAR_MOVIE_DETAILS:
            return null;
        default:
            return state;
    }
};

const sessionDetails = (state = initialState.sessionDetails, action) => {
    switch (action.type) {
        case actionTypes.GET_SESSION_DETAILS_COMPLETED:
            return action.session;
        case actionTypes.CLEAR_SESSION_DETAILS:
            return null;
        default:
            return state;
    }
};

const reducer = combineReducers({
    sessions,
    billboardDate,
    movieDetails,
    sessionDetails
});

export default reducer;
