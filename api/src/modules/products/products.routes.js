import express from 'express';
import ProductsController from './products.controller';
import { isAuthenticated, isAuthorized } from '../../utils/authManager';
import { USER_ROLES } from '../../utils/constants';

const Router = express.Router();
Router.route('/')
    .get([], ProductsController.getAll)
    // .get([isAuthenticated, isAuthorized(USER_ROLES.ADMIN)], ProductsController.getAllByType)
    // .post([isAuthenticated, isAuthorized(USER_ROLES.ADMIN)], ProductsController.save);

Router.route('/:id')
    .get([], ProductsController.getById)
    // .put([isAuthenticated, isAuthorized(USER_ROLES.ADMIN)], ProductsController.update);

// Router.route('/deleteMedia/:id')
//     .delete([isAuthenticated, isAuthorized(USER_ROLES.ADMIN)], ProductsController.deleteMedia);
    
// Router.route('/type/:id')
//     .get([isAuthenticated, isAuthorized(USER_ROLES.ADMIN)], ProductsController.getAllByType);

module.exports = Router;