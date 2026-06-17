import {useSelector} from 'react-redux';
import {Route, Routes} from 'react-router';
import Container from 'react-bootstrap/Container';

import AppGlobalComponents from './AppGlobalComponents';
import Home from './Home';
import {Login, SignUp, UpdateProfile, ChangePassword, Logout} from '../../users';
import users from '../../users';
import {MovieDetails, SessionDetails} from '../../catalog';
import {BuyTickets, PurchaseCompleted, PurchaseHistory, DeliverTickets} from '../../shopping';

const Body = () => {

    const loggedIn = useSelector(users.selectors.isLoggedIn);
    
   return (

       <Container className="my-4 justify-content-center flex-grow-1">
            <AppGlobalComponents/>
            <Routes>
                <Route path="/*" element={<Home/>}/>
                <Route path="/catalog/movies/:movieId" element={<MovieDetails/>}/>
                {loggedIn && <Route path="/catalog/sessions/:sessionId/buy" element={<BuyTickets/>}/>}
                {loggedIn && <Route path="/shopping/purchase-completed" element={<PurchaseCompleted/>}/>}
                {loggedIn && <Route path="/shopping/purchases" element={<PurchaseHistory/>}/>}
                {loggedIn && <Route path="/shopping/deliver-tickets" element={<DeliverTickets/>}/>}
                <Route path="/catalog/sessions/:sessionId" element={<SessionDetails/>}/>
                {loggedIn && <Route path="/users/update-profile" element={<UpdateProfile/>}/>}
                {loggedIn && <Route path="/users/change-password" element={<ChangePassword/>}/>}
                {loggedIn && <Route path="/users/logout" element={<Logout/>}/>}
                {!loggedIn && <Route path="/users/login" element={<Login/>}/>}
                {!loggedIn && <Route path="/users/signup" element={<SignUp/>}/>}
            </Routes>
       </Container>

    );

};

export default Body;
