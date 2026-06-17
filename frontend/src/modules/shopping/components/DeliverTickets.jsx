import {useEffect, useState} from 'react';
import {useSelector, useDispatch} from 'react-redux';
import Card from 'react-bootstrap/Card';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import {FormattedMessage} from 'react-intl';

import {Errors, Success} from '../../common';
import * as actions from '../actions';
import * as selectors from '../selectors';

const DeliverTickets = () => {

    const dispatch = useDispatch();
    const delivery = useSelector(selectors.getDelivery);

    const [purchaseId, setPurchaseId] = useState('');
    const [creditCard, setCreditCard] = useState('');
    const [formValidated, setFormValidated] = useState(false);
    const [backendErrors, setBackendErrors] = useState(null);

    let form;

    useEffect(() => {
        dispatch(actions.clearDelivery());
        return () => dispatch(actions.clearDelivery());
    }, [dispatch]);

    const handleSubmit = event => {
        event.preventDefault();

        if (form.checkValidity()) {
            setBackendErrors(null);
            
            dispatch(actions.deliverTickets(purchaseId, creditCard,
                () => {
                    setPurchaseId('');
                    setCreditCard('');
                    setFormValidated(false);
                },
                errors => setBackendErrors(errors)));

        } else {
            setBackendErrors(null);
            setFormValidated(true);
        }
    };

    return (
        <div className="col-md-8 mx-auto">
            <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>

            {delivery && (
                <Success message={<FormattedMessage id="project.shopping.DeliverTickets.success" values={{movieTitle: delivery.movieTitle}}/>} 
                         onClose={() => dispatch(actions.clearDelivery())} />
            )}

            <Card className="bg-light border-dark mt-3">
                <Card.Header as="h5">
                    <FormattedMessage id="project.shopping.DeliverTickets.title"/>
                </Card.Header>
                <Card.Body>
                    <Form ref={node => form = node}
                        noValidate validated={formValidated}
                        onSubmit={handleSubmit}>
                        <Form.Group as={Row} className="mb-3" controlId="purchaseId">
                            <Form.Label column md={4}>
                                <FormattedMessage id="project.shopping.DeliverTickets.purchaseId"/>
                            </Form.Label>
                            <Col md={5}>
                                <Form.Control
                                    type="number"
                                    min={1}
                                    value={purchaseId}
                                    onChange={e => setPurchaseId(e.target.value)}
                                    autoComplete="off"
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
                                    inputMode="numeric"
                                    pattern="[0-9]{16}"
                                    maxLength="16"
                                    value={creditCard}
                                    onChange={e => setCreditCard(e.target.value.replace(/\D/g, '').slice(0, 16))}
                                    autoComplete="off"
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
                            <FormattedMessage id="project.shopping.DeliverTickets.deliver"/>
                        </Button>
                    </Form>
                </Card.Body>
            </Card>
        </div>
    );
};

export default DeliverTickets;
