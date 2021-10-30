import express from 'express';
import SellerRoutesController from './seller_routes.controller';
// import { isAuthenticated, isAuthorized } from '../../utils/authManager';
import { USER_ROLES } from '../../utils/constants';

const Router = express.Router();
// Router.route('/')
//     .get([],SellerRoutesController.getAll)

Router.route('/:seller_id')
    .get([], SellerRoutesController.getById)

module.exports = Router;