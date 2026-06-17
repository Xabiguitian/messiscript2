import PropTypes from 'prop-types';
import {FormattedDate, FormattedTime, FormattedMessage, FormattedNumber} from 'react-intl';

const Purchases = ({purchases}) => (

    <table className="table table-striped table-hover">

        <thead>
            <tr>
                <th scope="col"><FormattedMessage id="project.shopping.Purchases.purchaseId"/></th>
                <th scope="col"><FormattedMessage id="project.global.fields.purchaseDate"/></th>
                <th scope="col"><FormattedMessage id="project.global.fields.sessionDate"/></th>
                <th scope="col"><FormattedMessage id="project.shopping.Purchases.movie"/></th>
                <th scope="col"><FormattedMessage id="project.shopping.Purchases.room"/></th>
                <th scope="col"><FormattedMessage id="project.shopping.Purchases.tickets"/></th>
                <th scope="col"><FormattedMessage id="project.shopping.Purchases.totalPrice"/></th>
                <th scope="col"><FormattedMessage id="project.shopping.Purchases.status"/></th>
            </tr>
        </thead>

        <tbody>
            {purchases.map(purchase => 
                <tr key={purchase.id} className="purchase-row">
                    <td>{purchase.id}</td>
                    <td>
                        <FormattedDate value={new Date(purchase.date)}/>{' '}
                        <FormattedTime value={new Date(purchase.date)}/>
                    </td>
                    <td>
                        <FormattedDate value={new Date(purchase.sessionDate)}/>{' '}
                        <FormattedTime value={new Date(purchase.sessionDate)}/>
                    </td>
                    <td className="purchase-movie">{purchase.movieTitle}</td>
                    <td>{purchase.roomName}</td>
                    <td className="purchase-tickets">{purchase.tickets}</td>
                    <td>
                        <FormattedNumber value={purchase.totalPrice} style="currency" currency="EUR"/>
                    </td>
                    <td>
                        {purchase.delivered ? 
                            <span className="badge bg-success">
                                <FormattedMessage id="project.shopping.Purchases.delivered"/>
                            </span> : 
                            <span className="badge bg-warning text-dark">
                                <FormattedMessage id="project.shopping.Purchases.pending"/>
                            </span>
                        }
                    </td>
                </tr>
            )}
        </tbody>

    </table>

);

Purchases.propTypes = {
    purchases: PropTypes.array.isRequired
};

export default Purchases;
