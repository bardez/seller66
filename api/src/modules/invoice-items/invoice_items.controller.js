import { RouteClientsModel, ClientsModel, RoutesModel } from '../../models'
import jwt from 'jsonwebtoken'
import HTTP from '../../utils/http_headers'
import { paginatedResults } from '../../utils/paginatedResults';
import { sequelize } from '../../models'
import {
    resultSuccess,
    resultError,
    resultEmpty
} from '../../utils/response';
const fields = [
    'product_id',
    "invoice_id",
    "quantity"
]
// const getAll = async (req, res) =>{

//   const result = await sequelize.query(`
//           SELECT
//               * 
//           FROM
//             invoice_items it
//           INNER JOIN products p ON
//           p.id = it.product_id
//     `,{
//       type: sequelize.QueryTypes.SELECT
//     });
    

//     if(result.length > 0){
//         resultSuccess('route', res)(result);
//     }else{
//         resultEmpty(res);
//     }
// }

const getById = async (req, res) =>{
    if(req.params.invoice_id > 0){
        const result = await sequelize.query(`
          SELECT
              * 
          FROM
              invoice_items it
          INNER JOIN products p ON
            p.id = it.product_id
          WHERE
              it.invoice_id = :invoice_id
            group by p.id
        `,{
            replacements:{
              invoice_id: req.params.invoice_id
            },
            type: sequelize.QueryTypes.SELECT
        });
    

      if(result.length > 0){
          resultSuccess('invoice_items', res)(result);
      }else{
          resultEmpty(res);
      }
    }
    else{
        resultEmpty(res);
    }
}

const save = async (req, res) =>{
  try {
      const invoiceItemData = req.body;

      const invoiceItem = await InvoicesModel.create(invoiceItemData, { fields });

      resultSuccess('Dados salvos com sucesso.', res)(invoiceItem);
  } catch (error) {
      console.log('InvoiceItemsController.save - Erro ao criar registro.', error);
      resultError(HTTP.INTERNAL_SERVER_ERROR, 'Erro ao criar registro.', res)(error);
  }
}

const upsert = async (req, res) =>{
  try {
      const invoiceItemsData = req.body;
      const invoiceItems = await InvoicesItemsModel.findOne({ where: { invoice_id: req.params.id, product_id: invoiceItemsData.product_id } });

      if(invoiceItems) {    
          await invoiceItems.update(invoiceItemsData, { fields });
          resultSuccess('Dados atualizados com sucesso.', res)(invoiceItems);
      } else {
        const invoiceItem = await InvoicesModel.create(invoiceItemData, { fields });
        resultSuccess('Dados salvos com sucesso.', res)(invoiceItem);
      }
  } catch (error) {
      console.log('InvoicesItemsController.update - Erro ao criar registro.', error);
      resultError(HTTP.INTERNAL_SERVER_ERROR, 'Erro ao atualizar registro.', res)(error);
  }
}

export default{
    // getAll,
    getById,
    upsert
}