import {useEffect, useState} from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {FormattedMessage} from 'react-intl';

import * as actions from '../actions';
import * as selectors from '../selectors';
import Purchases from './Purchases';
import {Pager} from '../../common';

const PurchaseHistory = () => {

    const purchaseSearch = useSelector(selectors.getPurchaseSearch);
    const dispatch = useDispatch();
    const [page, setPage] = useState(0);

    useEffect(() => {

        dispatch(actions.findPurchases({page: page}));

        return () => dispatch(actions.clearPurchaseSearch());

    }, [page, dispatch]);

    if (!purchaseSearch) {
        return null;
    }

    if (purchaseSearch.result.items.length === 0) {
        return (
            <div className="alert alert-info" role="alert">
                <FormattedMessage id="project.shopping.PurchaseHistory.noPurchases"/>
            </div>
        );
    }

    return (
        <div>
            <h2><FormattedMessage id="project.shopping.PurchaseHistory.title"/></h2>

            <Purchases purchases={purchaseSearch.result.items}/>
            <Pager 
                back={{
                    enabled: page > 0,
                    onClick: () => setPage(page - 1)
                }}
                next={{
                    enabled: purchaseSearch.result.existMoreItems,
                    onClick: () => setPage(page + 1)
                }}/>
        </div>
    );
}

export default PurchaseHistory;
