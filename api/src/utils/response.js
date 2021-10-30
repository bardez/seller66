import HTTP from "./http_headers";
import { logger } from './logger';

export const resultSuccess = (message, res) => (data) => {
    res.status(HTTP.OK).json({
        message,
        status: true,
        data
    });
};

export const resultEmpty = (res) => {
    res.status(HTTP.OK).json({
        message: 'Nenhum registro encontrado.',
        status: true,
        data: []
    })
}

export const resultError = (errorCode=HTTP.OK, message, res) => (error) => {
    if(error){
        console.log(error || error.stack);
    }
    console.log(`Response error (${errorCode}) - ${message}`);
    res.status(errorCode).json({
        message,
        status: false,
        data: error ? error : []
    });
}