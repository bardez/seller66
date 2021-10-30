import appRoot from 'app-root-path'
import express from 'express';
import bodyParser from 'body-parser'
import dotEnvFlow from 'dotenv-flow'
dotEnvFlow.config({debug: true});

import cors from 'cors'
const app = express();
const corsOptions = {
    allowedHeaders: ['Origin', 'X-Requested-With', 'Content-Type', 'Accept', 'Authorization', 'Access-Control-Request-Method', 'Access-Control-Allow-Origin'],
    origin: '*',
    methods: 'GET,HEAD,PUT,PATCH,POST,DELETE',
};
app.use(cors(corsOptions));
app.use(bodyParser.json({limit: "50mb"}));

// logs
import { expressLogger, logger } from './src/utils/logger';
app.use(expressLogger);

import SellerRoutes from './src/modules/seller-routes/seller_routes.routes'
import RouteClients from './src/modules/route-clients/route_clients.routes'
import ProductsRoutes from './src/modules/products/products.routes'
import InvoicesRoutes from './src/modules/invoices/invoices.routes';
import InvoiceItemsRoutes from './src/modules/invoice-items/invoice_items.routes';
app.use('/routes', SellerRoutes);
app.use('/clients', RouteClients);
app.use('/products', ProductsRoutes);
app.use('/invoices', InvoicesRoutes);
app.use('/invoice-items', InvoiceItemsRoutes);
// app.use('/login', SellerRoutes);
// app.use('/assets/', express.static(`${appRoot}/assets`));

app.get('/', (req, res) => res.send(`API Seller66 - ${process.env.VERSION}`));
const server = app.listen(process.env.PORT, () => console.log(`Server runnning -> port: ${process.env.PORT}!`));