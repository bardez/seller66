import express from 'express';
import RouteClientsController from './route_clients.controller';
// import { isAuthenticated, isAuthorized } from '../../utils/authManager';
import { USER_ROLES } from '../../utils/constants';

const Router = express.Router();
// Router.route('/')
//     .get([],RouteClientsController.getAll)

Router.route('/:route_id')
    .get([], RouteClientsController.getById)

module.exports = Router;