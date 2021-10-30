import express from 'express';
import InvoicesController from './invoices.controller';
// import { isAuthenticated, isAuthorized } from '../../utils/authManager';
// import { USER_ROLES } from '../../utils/constants';

const Router = express.Router();

// // site
// Router.route('/:id/product')
//     .get(InvoicesController.getProductDetailFromLot);

// admin
Router.route('/')
    // .get([], InvoicesController.getAll)
    .post([], InvoicesController.save);

// Router.route('/auction/:id')
//     .get([isAuthenticated, isAuthorized(USER_ROLES.ADMIN)], InvoicesController.getLotsFromAuction);
    
    Router.route('/:id')
    // .get(InvoicesController.getById)
    .put([], InvoicesController.update)
    // .delete([], InvoicesController.remove);
    
// socket updates
// Router.route('/fast-buy')
//     .post([isAuthenticated], InvoicesController.setFastBuy);

// Router.route('/:id/sold')
//     .post([isAuthenticated, isAuthorized(USER_ROLES.ADMIN)], InvoicesController.setAsSold);


// Router.route('/:id/reopen')
//     .post([isAuthenticated, isAuthorized(USER_ROLES.ADMIN)], InvoicesController.reopen);

// Router.route('/:id/revert-bid')
//     .post([isAuthenticated, isAuthorized(USER_ROLES.ADMIN)], InvoicesController.revertBid);

module.exports = Router;