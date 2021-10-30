import jwt from 'jsonwebtoken';
import HTTP from './http_headers';
import { resultError } from './response';
import { USER_ROLES } from './constants';

export const isAuthenticated = async (req, res, next) => {

    let authHeaderValue = (req.body && req.body.access_token) || (req.query && req.query.access_token) || req.headers.authorization;
    if (!authHeaderValue) {
        return resultError(HTTP.UNAUTHORIZED, 'Authentication - Token inválido.', res)();
    }
    let token = authHeaderValue.replace('Bearer ', '');
    try {
        jwt.verify(token, process.env.SECRET, function (err, payload) {

            if (err) {
                resultError(HTTP.UNAUTHORIZED, 'Authentication - Erro ao validar token.', res)(err);
            }
            req.user = payload;
            next();
        });
    } catch (err) {
        resultError(HTTP.UNAUTHORIZED, 'Authentication - Token inválido.', res)(err);
    }
}

export const isAuthorized = (authorizedRoles) => {

    return (req, res, next) => {
        
        if (req.user.role === USER_ROLES.SUPERUSER) {
            return next();
        }

        if (!Array.isArray(authorizedRoles)) {
            authorizedRoles = [authorizedRoles];
        }
  
        if (authorizedRoles.indexOf(req.user.role) !== -1) {
            return next();
        } else {
            resultError(HTTP.FORBIDDEN, 'Acesso não autorizado', res)();
        }
    }
}