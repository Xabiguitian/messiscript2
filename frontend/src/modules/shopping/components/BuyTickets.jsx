import {useEffect, useState} from 'react';
import {useDispatch} from 'react-redux';
import {FormattedMessage} from 'react-intl';
import {Link, useParams, useNavigate} from 'react-router';
import Card from 'react-bootstrap/Card';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';

import {Errors} from '../../common';
import * as actions from '../actions';

const BuyTickets = () => {

    const {sessionId} = useParams();
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const [quantity, setQuantity] = useState(1);
    const [creditCard, setCreditCard] = useState('');
    const [backendErrors, setBackendErrors] = useState(null);
    const [formValidated, setFormValidated] = useState(false);
    let form;

    useEffect(() => {
        dispatch(actions.clearPurchase());
    }, [dispatch]);

    const handleSubmit = event => {

        event.preventDefault();

        if (form.checkValidity()) {

            setBackendErrors(null);

            dispatch(actions.buyTickets(sessionId, 
                quantity, creditCard.trim(), 
                () => navigate('/shopping/purchase-completed'),
                errors => setBackendErrors(errors)));

        } else {
            setBackendErrors(null);
            setFormValidated(true);
        }

    };

    return (
        <div className="col-md-8 mx-auto">
            <Link to={`/catalog/sessions/${sessionId}`}>&laquo; <FormattedMessage id="project.shopping.BuyTickets.backToSession"/></Link>

            <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>

            <Card className="bg-light border-dark mt-3">
                <Card.Header as="h5">
                    <FormattedMessage id="project.shopping.BuyTickets.title"/>
                </Card.Header>
                <Card.Body>
                    <Form ref={node => form = node}
                        noValidate validated={formValidated} 
                        onSubmit={handleSubmit}>
                        <Form.Group as={Row} className="mb-3" controlId="quantity">
                            <Form.Label column md={4}>
                                <FormattedMessage id="project.shopping.BuyTickets.quantity"/>
                            </Form.Label>
                            <Col md={5}>
                                <Form.Control
                                    type="number"
                                    value={quantity}
                                    onChange={e => setQuantity(e.target.value)}
                                    autoFocus
                                    min="1"
                                    max="10"
                                    required
                                />
                                <Form.Control.Feedback type="invalid">
                                    <FormattedMessage id='project.global.validator.required'/>
                                </Form.Control.Feedback>
                            </Col>
                        </Form.Group>

                        <Form.Group as={Row} className="mb-3" controlId="creditCard">
                            <Form.Label column md={4}>
                                <FormattedMessage id="project.shopping.BuyTickets.creditCard"/>
                            </Form.Label>
                            <Col md={5}>
                                <Form.Control
                                    type="text"
                                    value={creditCard}
                                    onChange={e => setCreditCard(e.target.value)}
                                    pattern="[0-9]{16}"
                                    maxLength="16"
                                    required
                                />
                                <Form.Control.Feedback type="invalid">
                                    {creditCard ? 
                                        <FormattedMessage id='project.global.validator.creditCardNumber'/> :
                                        <FormattedMessage id='project.global.validator.required'/>
                                    }
                                </Form.Control.Feedback>
                            </Col>
                        </Form.Group>

                        <Button type="submit" variant="primary">
                            <FormattedMessage id="project.shopping.BuyTickets.buy"/>
                        </Button>
                    </Form>
                </Card.Body>
            </Card>
        </div>
    );

}

export default BuyTickets;
