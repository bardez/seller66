// logs
const pino = require('pino');
const expressPino = require('express-pino-logger');

export const logger = pino({ level: process.env.LOG_LEVEL || 'debug' });
export const expressLogger = expressPino({ logger });