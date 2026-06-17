import {combineReducers} from 'redux';
import * as actionTypes from './actionTypes';

const initialState = {
    purchase: null,
    purchaseSearch: null,
    delivery: null
};

const purchase = (state = initialState.purchase, action) => {
    switch (action.type) {
        case actionTypes.BUY_TICKETS_COMPLETED:
            return action.purchase;
        case actionTypes.CLEAR_PURCHASE:
            return null;
        default:
            return state;
    }
};

const purchaseSearch = (state = initialState.purchaseSearch, action) => {
    switch (action.type) {
        case actionTypes.FIND_PURCHASES_COMPLETED:
            return action.purchaseSearch;
        case actionTypes.CLEAR_PURCHASE_SEARCH:
            return initialState.purchaseSearch;
        default:
            return state;
    }
};

const delivery = (state = initialState.delivery, action) => {
    switch (action.type) {
        case actionTypes.DELIVER_TICKETS_COMPLETED:
            return action.delivery;
        case actionTypes.CLEAR_DELIVERY:
            return null;
        default:
            return state;
    }
};

const reducer = combineReducers({
    purchase,
    purchaseSearch,
    delivery
});

export default reducer;
