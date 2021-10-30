import express from 'express';
import InvoiceItemsController from './invoice_items.controller';
// import { isAuthenticated, isAuthorized } from '../../utils/authManager';
import { USER_ROLES } from '../../utils/constants';

const Router = express.Router();
// Router.route('/')
//     .get([],InvoiceItemsController.getAll)

Router.route('/:invoice_id')
    .get([], InvoiceItemsController.getById)

module.exports = Router;