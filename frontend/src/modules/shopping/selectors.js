const getModuleState = state => state.shopping;

export const getPurchase = state => getModuleState(state).purchase;

export const getPurchaseSearch = state => getModuleState(state).purchaseSearch;

export const getDelivery = state => getModuleState(state).delivery;
