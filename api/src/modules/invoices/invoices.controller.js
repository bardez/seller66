import {
    InvoicesModel
} from '../../models'
import HTTP from '../../utils/http_headers'
import {
    resultSuccess,
    resultError,
} from '../../utils/response';
// import schedulerController from '../scheduler/scheduler.controller'

const fields = [
    'client_id',
    'date',
    'status'
]

// const getAll = async (req, res) =>{
//   if(req.params.route_id > 0){
//     const result = await sequelize.query(`
//       SELECT
//           c.* 
//       FROM
//           invoice rc
//       INNER JOIN clients c ON
//         c.id = rc.client_id
//       WHERE
//           rc.route_id = :route_id
//         group by c.id
//     `,{
//         replacements:{
//           route_id: req.params.route_id
//         },
//         type: sequelize.QueryTypes.SELECT
//     });


//   if(result.length > 0){
//       resultSuccess('route', res)(result);
//   }else{
//       resultEmpty(res);
//   }
// }
// else{
//     resultEmpty(res);
// }
// }

// const getById = async (req, res) =>{
//     if(req.params.id > 0){
//         const data = await LotsModel.findOne({
//             where: { id: req.params.id }
//         });
//         if(data == null){
//             resultError(HTTP.BAD_REQUEST, 'Lote não encontrado.', res)(error);
//         } else{
//             resultSuccess('Listagem', res)(data);
//         }
//     }
// }

const save = async (req, res) =>{
    try {
        const invoiceData = req.body;

        invoiceData.date = new Date();

        const invoice = await InvoicesModel.create(invoiceData, { fields });

        resultSuccess('Dados salvos com sucesso.', res)(invoice);
    } catch (error) {
        console.log('InvoiceController.save - Erro ao criar registro.', error);
        resultError(HTTP.INTERNAL_SERVER_ERROR, 'Erro ao criar registro.', res)(error);
    }
}

const update = async (req, res) =>{
    try {
        const invoice = await InvoicesModel.findOne({ where: { id: req.params.id } });

        if(invoice) {
            let { data: invoiceData } = req.body;
            
            await invoice.update(invoiceData, { fields });
            resultSuccess('Dados atualizados com sucesso.', res)(invoice);
        }
        
    } catch (error) {
        console.log('LotsController.update - Erro ao criar registro.', error);
        resultError(HTTP.INTERNAL_SERVER_ERROR, 'Erro ao atualizar registro.', res)(error);
    }
}

// const remove = async (req, res) =>{
//     try {
//         await LotsModel.destroy({
//             where:{
//                 id: req.params.id
//             }
//         });
//         resultSuccess('Registro deletado com sucesso.', res)();
//     } catch (error) {
//         console.log('LotsController.remove - Erro ao deletar registro.', error);
//         resultError(HTTP.INTERNAL_SERVER_ERROR, 'Erro ao deletar registro.', res)(error);
//     }
// }

// const getProductDetailFromLot = async (req, res) =>{
//     const data = await LotsModel.findOne({
//         where: { id: req.params.id },
//         include:[
//             {
//                 model: ProductsModel,
//                 as: 'product',
//                 attributes:[ 'id', 'name' ],
//                 include: [
//                     { model: ProductMediaModel, as: 'product_media' },
//                     { 
//                         model: CustomFieldsProductModel, 
//                         as: 'product_custom_fields',
//                         include: [ { model: CustomFieldsModel, as: 'custom_field' }, ]
//                     },
//                 ]
//             },
//             {
//                 model: AuctionsModel,
//                 as: 'auction',
//                 attributes:[ 'id', 'name' ]
//             },
//             {
//                 model: BidsModel,
//                 as: 'bids',
//                 attributes:[
//                     'id',
//                     'bid_value',
//                     'lot_id',
//                     'current_value',
//                     'is_defense',
//                     'sold',
//                     'user_id'
//                 ],
//                 limit: 1,
//                 order:[['id', 'DESC']],
//             }
//         ]
//     });

//     if(data == null){
//         resultEmpty(res);
//     }else{
//         if(data.bids.length > 0) {
//             const bid = data.bids[0];
//             if(bid.current_value > 0) {
//                 data.price = bid.current_value;
//             }
//         }
//         resultSuccess('Detalhes do lote por ID', res)(data);
//     }
// }

// const getLotsFromAuction = async (req, res) =>{
//     if(req.params.id > 0){

//         const excludedFields = [
//             'created_at',
//             'updated_at',
//             'deleted_at'
//         ];
    
//         const searchFields = ['`product`.`name`', '`status`.`name`'];
    
//         const inclusions = [
//             { model: ProductsModel, as: 'product' },
//             { model: StatusModel, as: 'status' },
//             {
//                 model: BidsModel,
//                 as: 'bids',
//                 attributes:[
//                     'id',
//                     'bid_value',
//                     'lot_id',
//                     'current_value',
//                     'is_defense',
//                     'sold',
//                     'user_id'
//                 ],
//                 include: [
//                     { model: UsersModel, as: 'user', attributes: ['id', 'name'] }
//                 ],
//                 limit: 1,
//                 order:[['id', 'DESC']],
//             }
//         ];

//         req.query.extraFields = [
//             ['auction_id', req.params.id, Sequelize.Op.eq]
//         ];
    
//         const data = await paginatedResults(LotsModel, req.query, excludedFields, searchFields, inclusions);
    
//         const returnData = {
//             count: data.count,
//             rows: data.rows
//         }
    
//         if(returnData.rows.length > 0){
//             if(returnData.rows.length > 0){
//                 returnData.rows.forEach(item=>{                
//                     if(item.bids.length > 0){
//                         const bid = item.bids[0];
//                         if(bid.current_value > 0){
//                             item.price = bid.current_value;
//                         }
//                     }
//                 })
//             }
//             resultSuccess('Listagem de lotes', res)(returnData);
//         } else{
//             resultEmpty(res);
//         }
//     }
// }

// const setAsSold = async (req, res) =>{
//     try {
//         const lot = await LotsModel.findOne({ where: { id: req.params.id } });

//         if (!lot) {
//             resultError(HTTP.BAD_REQUEST, 'Lote não encontrado.', res)(error);
//             return;
//         }

//         const updatedFields = {
//             sold: true,
//             status_id: STATUS.FINISHED,
//             sold_at: new Date().getTime()
//         };
//         await lot.update(updatedFields);

//         const updatedLot = await getLotDetails(req.params.id);
//         req.app.get("socketService").emiter('lotUpdate', updatedLot);
//         resultSuccess('Lote marcado como vendido com sucesso.', res)(lot);        
//     } catch (error) {
//         console.log('LotsController.setAsSold - Erro ao atualizar registro.', error);
//         resultError(HTTP.INTERNAL_SERVER_ERROR, 'Erro ao atualizar registro.', res)(error);
//     }
// }

// const setFastBuy = async (req, res) =>{
//     let { data:fastBuyData } = req.body;
//     console.log('fastBuyData', fastBuyData, req.body)
//     try {
//         const lot = await LotsModel.findOne({ where: { id: fastBuyData.id } });

//         if (!lot) {
//             resultError(HTTP.BAD_REQUEST, 'Lote não encontrado.', res)(error);
//             return;
//         }

//         //--------------------------
//         // creating bid for fast buy
//         let dataModelBid = {
//             user_id: fastBuyData.userId,
//             auction_id: lot.getDataValue('auction_id'),
//             lot_id: lot.getDataValue('id'),
//             current_value: lot.getDataValue('target_price'),
//             bid_value: 0,
//             sold: true
//         }
//         const buyNowBid = await BidsModel.create(dataModelBid, { fields: ['user_id', 'auction_id', 'lot_id', 'current_value', 'bid_value', 'sold']})
//         req.app.get("socketService").emiter('newBid', buyNowBid);
//         //--------------------------

//         const updatedFields = {
//             status_id: STATUS.FINISHED,
//             sold_at: new Date().getTime(),
//             buy_user_id: fastBuyData.userId,
//             sold: true
//         };
//         await lot.update(updatedFields);

//         const updatedLot = await getLotDetails(fastBuyData.id);
//         req.app.get("socketService").emiter('lotUpdate', updatedLot);
//         resultSuccess('Lote marcado como vendido com sucesso.', res)(lot);        
//     } catch (error) {
//         console.log('LotsController.setFastBuy - Erro ao atualizar registro.', error);
//         resultError(HTTP.INTERNAL_SERVER_ERROR, 'Erro ao atualizar registro.', res)(error);
//     }
// }

// const reopen = async (req, res) =>{
//     try {
//         const lot = await LotsModel.findByPk(req.params.id);

//         if (!lot) {
//             return resultError(HTTP.BAD_REQUEST, 'Lote não encontrado.', res)({});
//         }

//         const now = new moment().format('x');
//         const lotAuction = await AuctionsModel.findOne({
//             where:{
//                 id: lot.auction_id
//             },
//             attributes:['rebound_time', 'end']
//         });

//         if (lot.status_id == STATUS.FINISHED) {
//             const updatedFields = {
//                 sold: false,
//                 status_id: STATUS.ACTIVE,
//                 sold_at: null,
//                 end: parseInt(now) + parseInt(lotAuction.rebound_time)
//             };

//             await lot.update(updatedFields);
//             const updatedLot = await getLotDetails(lot.id);

//             schedulerController.createLotEvent(updatedLot, req.app.get("socketService"));
//             req.app.get("socketService").emiter('lotUpdate', updatedLot);
//             resultSuccess('Lote reaberto com sucesso.', res)(lot);
//         }else{
//             resultSuccess('Este lote já está aberto.', res)(lot);
//         }
//     } catch (error) {
//         console.log('LotsController.reopen - Erro ao atualizar lote.', error);
//         resultError(HTTP.INTERNAL_SERVER_ERROR, 'Erro ao atualizar lote.', res)(error);
//     }
// }

// const revertBid = async (req, res) =>{
//     try {
//         const lot = await LotsModel.findOne({ where: { id: req.params.id } });
//         if (!lot) {
//             resultError(HTTP.BAD_REQUEST, 'Lote não encontrado.', res)(error);
//             return;
//         }

//         const bid = await BidsModel.findAll({
//             where: { lot_id: req.params.id },
//             limit: 1,
//             order:[['id', 'DESC']]
//         });
//         if (!bid || !bid.length) {
//             resultError(HTTP.BAD_REQUEST, 'Lance não encontrado.', res)(error);
//             return;
//         }

//         await BidsModel.destroy({ where: { id: bid[0].id } });
//         const updatedLot = await getLotDetails(req.params.id);
//         req.app.get("socketService").emiter('lotUpdate', updatedLot);
//         resultSuccess('Lance removido com sucesso.', res)(lot);
//     } catch (error) {
//         console.log('LotsController.revertBid - Erro ao atualizar registro.', error);
//         resultError(HTTP.INTERNAL_SERVER_ERROR, 'Erro ao atualizar registro.', res)(error);
//     }
// }

// const getLotDetails = async (id) => {
//     const lot = await LotsModel.findOne({
//         where: { id: id },
//         include:[
//             { model: StatusModel, as: 'status' },
//             {
//                 model: BidsModel,
//                 as: 'bids',
//                 attributes:[
//                     'id',
//                     'bid_value',
//                     'lot_id',
//                     'current_value',
//                     'is_defense',
//                     'sold',
//                     'user_id'
//                 ],
//                 include: [
//                     { model: UsersModel, as: 'user', attributes: ['id', 'name'] }
//                 ],
//                 limit: 1,
//                 order:[['id', 'DESC']],
//             }
//         ]
//     });

//     if(lot.bids.length > 0) {
//         const bid = lot.bids[0];
//         if(bid.current_value > 0) {
//             lot.price = bid.current_value;
//         }
//     }

//     return lot;
// }

export default{
    // getAll,
    // getById,
    save,
    update,
    // remove,
    // getProductDetailFromLot,
    // getLotsFromAuction,
    // setAsSold,
    // reopen,
    // revertBid,
    // getLotDetails,
    // setFastBuy
}