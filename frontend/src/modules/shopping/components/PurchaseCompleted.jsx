import {useSelector} from 'react-redux';
import {FormattedMessage} from 'react-intl';
import {Link} from 'react-router';

import * as selectors from '../selectors';

const PurchaseCompleted = () => {

    const purchase = useSelector(selectors.getPurchase);

    if (!purchase) {
        return null;
    }

    return (

        <div className="alert alert-success" role="alert">
            <h4 className="alert-heading">
                <FormattedMessage id="project.shopping.PurchaseCompleted.success"/>
            </h4>
            <p>
                <FormattedMessage id="project.shopping.PurchaseCompleted.purchaseId"/>
                <strong id="purchase-id"> {purchase.id}</strong>
            </p>
            <hr/>
            <p className="mb-0">
                <Link to="/" className="alert-link">
                    <FormattedMessage id="project.catalog.SessionDetails.backToBillboard"/>
                </Link>
            </p>
        </div>

    );

}

export default PurchaseCompleted;
