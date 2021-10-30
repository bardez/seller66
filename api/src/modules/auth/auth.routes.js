import express from 'express';
import Auth from './auth.controller';
import { isAuthenticated } from '../../utils/authManager';

const Router = express.Router();
Router.route('/login')
    .post(Auth.login);

Router.route('/logout')
    .post(isAuthenticated, Auth.logout)

Router.route('/recoverPass')
    .post(Auth.recoverPass)
    
Router.route('/loadByHash/:hash')
    .get(Auth.loadByHash)

Router.route('/updatePassword')
    .put(isAuthenticated, Auth.updatePassword)

module.exports = Router;