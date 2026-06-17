import {useEffect, useState} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {useParams, Link} from 'react-router';
import {FormattedDate, FormattedTime, FormattedNumber, FormattedMessage} from 'react-intl';
import backend from '../../../backend';
import * as actions from '../actions';
import * as selectors from '../selectors';
import {Errors} from '../../common';
import users from '../../users';

const SessionDetails = () => {
    const {sessionId} = useParams();
    const dispatch = useDispatch();
    const session = useSelector(selectors.getSessionDetails);
    const loggedIn = useSelector(users.selectors.isLoggedIn);
    const isTicketSeller = useSelector(users.selectors.isTicketSeller);
    const [backendErrors, setBackendErrors] = useState(null);

    useEffect(() => {
        const getSessionDetails = async () => {
            dispatch(actions.clearSessionDetails());
            setBackendErrors(null);
            const response = await backend.catalogService.getSessionDetails(sessionId);
            if (response.ok) {
                dispatch(actions.getSessionDetailsCompleted(response.payload));
            } else {
                setBackendErrors(response.payload);
            }
        };

        getSessionDetails();

        return () => dispatch(actions.clearSessionDetails());
    }, [sessionId, dispatch]);

    return (
        <div>
            <Link to="/">&laquo; <FormattedMessage id="project.catalog.SessionDetails.backToBillboard"/></Link>

            <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>

            {session && (
                <div className="mt-3">
                    <h2><Link id="movie-title-link" to={`/catalog/movies/${session.movieId}`}>{session.movieTitle}</Link></h2>

                    <table className="table table-sm w-auto mt-3">
                        <tbody>
                            <tr>
                                <th scope="row"><FormattedMessage id="project.catalog.SessionDetails.room"/></th>
                                <td id="room-name">{session.roomName}</td>
                            </tr>
                            <tr>
                                <th scope="row"><FormattedMessage id="project.global.fields.date"/></th>
                                <td id="session-date-time">
                                    <FormattedDate value={new Date(session.date)}/>{' '}
                                    <FormattedTime value={new Date(session.date)}/>
                                </td>
                            </tr>
                            <tr>
                                <th scope="row"><FormattedMessage id="project.catalog.SessionDetails.price"/></th>
                                <td id="price">
                                    <FormattedNumber value={session.price} style="currency" currency="EUR"/>
                                </td>
                            </tr>
                            <tr>
                                <th scope="row"><FormattedMessage id="project.catalog.SessionDetails.emptySeats"/></th>
                                <td id="empty-seats">{session.emptySeats}</td>
                            </tr>
                        </tbody>
                    </table>

                    {loggedIn && !isTicketSeller && (
                        <Link
                            id="buy-tickets-link"
                            to={`/catalog/sessions/${session.id}/buy`}
                            className="btn btn-primary mt-2"
                        >
                            <FormattedMessage id="project.catalog.SessionDetails.buyTickets"/>
                        </Link>
                    )}
                </div>
            )}
        </div>
    );
};

export default SessionDetails;
